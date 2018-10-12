package com.delicloud.app.miniprint.server.service.impl;

import com.delicloud.app.miniprint.core.dto.FollowDto;
import com.delicloud.app.miniprint.core.dto.GetFollowsOrFollowersDto;
import com.delicloud.app.miniprint.core.dto.QueryFansOrFollowByNickNameDto;
import com.delicloud.app.miniprint.core.entity.TUser;
import com.delicloud.app.miniprint.core.entity.TUserFollow;
import com.delicloud.app.miniprint.core.vo.PageVo;
import com.delicloud.app.miniprint.core.vo.UserFollowPageVo;
import com.delicloud.app.miniprint.server.Exception.AppException;
import com.delicloud.app.miniprint.server.repository.UserFollowRepository;
import com.delicloud.app.miniprint.server.repository.UserRepository;
import com.delicloud.app.miniprint.server.repository.WeiboRepository;
import com.delicloud.app.miniprint.server.service.IUserFollowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: dy
 * @Description: 微博业务层
 * @Date: 2018/8/31 16:17
 */
@Service
@Slf4j
public class UserFollowServiceImpl implements IUserFollowService {

    @Autowired
    private UserFollowRepository userFollowRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WeiboRepository weiboRepository;

    @Autowired
    private EntityManager em;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public void follow(Long uid, FollowDto followDto) {
    	Long checkedUid = followDto.getCheckedUid();
        checkFollowParam(uid, checkedUid);
        TUserFollow userFollow = userFollowRepository.findByFromUidAndToUid(uid, checkedUid);
        if (null != userFollow)
            throw new AppException(-1, "已关注该用户, 请刷新重试");

        try {

            long currentTimeMillis = System.currentTimeMillis();

            // 新增关注信息
            userFollow = new TUserFollow();
            userFollow.setCreateTime(currentTimeMillis);
            userFollow.setUpdateTime(currentTimeMillis);
            userFollow.setFromUid(uid);
            userFollow.setToUid(checkedUid);
            TUserFollow userFollow_ = userFollowRepository.findByFromUidAndToUid(checkedUid, uid);
            if (null == userFollow_) {  // 关注状态 1: 已关注, 对方未关注 2: 未关注, 对方已关注, 3: 互相关注
                userFollow.setFollowStatus(1);
            } else {
                userFollow.setFollowStatus(3);
                userFollow_.setFollowStatus(3);
                userFollow_.setUpdateTime(currentTimeMillis);
                userFollowRepository.save(userFollow_);
            }

            // 保存关注信息
            userFollowRepository.save(userFollow);

            // 新增用户关注数信息
            TUser user = userRepository.findOne(uid);
            user.setUpdateTime(currentTimeMillis);
            user.setFollows(user.getFollows() + 1);
            userRepository.save(user);

            // 新增用户粉丝数信息
            TUser followedUser = userRepository.findOne(checkedUid);
            followedUser.setUpdateTime(currentTimeMillis);
            followedUser.setFollowers(user.getFollowers() + 1);
            userRepository.save(followedUser);

        } catch (Exception e) {
            throw new AppException(-1, "关注异常");
        }

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public void cancel(Long uid, FollowDto followDto) {
        // uid 发起取消关注请求用户 id
    	Long checkedUid = followDto.getCheckedUid();
        checkFollowParam(uid, checkedUid);
        TUserFollow userFollow = userFollowRepository.findByFromUidAndToUid(uid, checkedUid);
        if (null == userFollow)
            throw new AppException(-1, "已取消关注该用户, 请刷新重试");

        try {

            long currentTimeMillis = System.currentTimeMillis();

            TUserFollow userFollow_ = userFollowRepository.findByFromUidAndToUid(checkedUid, uid); // 对方是否关注了我
            if (null != userFollow_) {  // 对方已关注了我
                // 关注状态 1: 已关注, 对方未关注 2: 未关注, 对方已关注, 3: 互相关注
                userFollow_.setFollowStatus(1);
                userFollow_.setUpdateTime(currentTimeMillis);
                userFollowRepository.save(userFollow_);
            }

            // 删除关注信息
            userFollowRepository.delete(userFollow);

            // 更新用户关注数信息
            TUser user = userRepository.findOne(uid);
            user.setUpdateTime(currentTimeMillis);
            if (user.getFollows() > 0)
                user.setFollows(user.getFollows() - 1);
            userRepository.save(user);

            // 更新用户粉丝数信息
            TUser followedUser = userRepository.findOne(checkedUid);
            followedUser.setUpdateTime(currentTimeMillis);
            if (followedUser.getFollowers() > 0)
                followedUser.setFollowers(followedUser.getFollowers() - 1);
            userRepository.save(followedUser);

        } catch (Exception e) {
            throw new AppException(-1, "取消关注异常");
        }
    }

    @Override
    public PageVo<UserFollowPageVo> getFollowsOrFollowers(Long uid, GetFollowsOrFollowersDto getFollowsOrFollowersDto) {
        //Long checkedUid = getFollowsOrFollowersDto.getCheckedUid() == null ? uid : getFollowsOrFollowersDto.getCheckedUid();
    	Long checkedUid = getFollowsOrFollowersDto.getCheckedUid();
    	Integer type = getFollowsOrFollowersDto.getType();
    	checkGetFollowsOrFollowers(uid, checkedUid, type);
        Integer pageNo = getFollowsOrFollowersDto.getPageNo();
        Integer pageSize = getFollowsOrFollowersDto.getPageSize();


        StringBuffer dataSql = new StringBuffer(" select ");
        StringBuffer countSql = new StringBuffer(" select count(*) ");

        String filedSql = " u.id as uid, u.avatar_url as avatarUrl, u.nick_name as nickName, " +
                          " u.individuality_signature as individualitySignature, uf.follow_status as followStatus ";

        dataSql.append(filedSql);
        String fromSql = " from tm_user_follow uf left join tm_user u on ";
        String whereSql;
        if (1 == type)     // 我的粉丝列表
            whereSql = "uf.from_uid = u.id where uf.to_uid = ";
         else  // 我的关注列表
            whereSql = "uf.to_uid = u.id where uf.from_uid = ";

        dataSql.append(fromSql).append(whereSql).append("'" + checkedUid + "'");
        countSql.append(fromSql).append(whereSql).append("'" + checkedUid + "'");

        Query dataQuery = em.createNativeQuery(dataSql.toString());
        Query countQuery = em.createNativeQuery(countSql.toString());

        dataQuery.setFirstResult(pageSize * (pageNo -1));
        dataQuery.setMaxResults(pageSize);

        Integer count = Integer.parseInt(countQuery.getSingleResult().toString());
        PageVo<UserFollowPageVo> ouput = new PageVo<>(pageNo, pageSize, count);
        ouput.setContent(toList(dataQuery.getResultList(), uid, type));

        return ouput;
    }

    /**
     * 列表转换
     * @param results Object[] 列表
     * @param type 1: 我的粉丝 2: 我的关注
     * @return
     */
    private List<UserFollowPageVo> toList(List<Object[]> results, Long uid, Integer type) {
        List<UserFollowPageVo> data = new ArrayList<>();

        if (results.size() == 0)
            return data;

        if (type == 1) { // 我的粉丝列表
            for (Object[] o : results) {
                UserFollowPageVo vo = new UserFollowPageVo();
                vo.setUid(o[0] == null ? 0L : Long.parseLong(o[0].toString()));
                vo.setAvatarUrl(o[1] == null ? "" : o[1].toString());
                vo.setNickName(o[2] == null ? "" : o[2].toString());
                vo.setIndividualitySignature(o[3] == null ? "" : o[3].toString());
                System.out.println(o[4]);
                if (vo.getUid().toString().equals(uid)) {   // 是否是我
                    vo.setWhetherMe(true);
                } else {
                    vo.setWhetherMe(false);
                }
                if (null != o[4] && Integer.parseInt(o[4].toString()) == 1) {
                    vo.setFollowStatus(2);  // 未关注 对方已关注
                } else if (null != o[4] && Integer.parseInt(o[4].toString()) == 3) {
                    vo.setFollowStatus(3);
                } else if (null != o[4] && Integer.parseInt(o[4].toString()) == 2) {
                    vo.setFollowStatus(1);
                }
                data.add(vo);
            }
        } else {    // 该用户的关注列表
            for (Object[] o : results) {
                UserFollowPageVo vo = new UserFollowPageVo();
                vo.setUid(o[0] == null ? 0L : Long.parseLong(o[0].toString()));
                vo.setAvatarUrl(o[1] == null ? "" : o[1].toString());
                vo.setNickName(o[2] == null ? "" : o[2].toString());
                vo.setIndividualitySignature(o[3] == null ? "" : o[3].toString());
                vo.setFollowStatus(o[4].toString() == null ? 4 : Integer.parseInt(o[4].toString()));
                if (vo.getUid().toString().equals(uid)) {   // 是否是我
                    vo.setWhetherMe(true);
                } else {
                    vo.setWhetherMe(false);
                }
                data.add(vo);
            }
        }
        return data;
    }

    /**
     * 校验入参
     * @param uid
     * @param checkedUid
     * @param type
     */
    private void checkGetFollowsOrFollowers(Long uid, Long checkedUid, Integer type) {
        if (null == uid)
            throw new AppException(-1, "用户 id 不能为空");
        if (null == checkedUid) {
        	log.info("checkedUid不能为空");
        	throw new AppException(-1, "选中用户 id 不能为空");	
        }
        if (null == type)
            throw new AppException(-1, "type 不能为空");
    }

    @Override
    public List<UserFollowPageVo> queryFansOrFollowByNickName(Long uid, QueryFansOrFollowByNickNameDto queryFansOrFollowByNickNameDto) {

        Integer type = queryFansOrFollowByNickNameDto.getType();
        String nickName = queryFansOrFollowByNickNameDto.getNickName();

        if (null == uid)
            throw new AppException(-1, "用户 id 不能为空");

        StringBuffer dataSql = new StringBuffer(" select ");
        String filedSql = " u.id as uid, u.avatar_url as avatarUrl, u.nick_name as nickName, " +
                          " u.individuality_signature as individualitySignature, uf.follow_status as followStatus ";
        dataSql.append(filedSql);
        String fromSql;
        if (1 == type) {
            fromSql = " from tm_user_follow uf left join tm_user u on uf.from_uid = u.id where uf.to_uid = '" + uid + "' ";
        } else {
            fromSql = " from tm_user_follow uf left join tm_user u on uf.from_uid = u.id where uf.from_uid = '" + uid + "' ";
        }
        dataSql.append(fromSql);
        if (null != nickName && !"".equals(nickName))
            dataSql.append(" and u.nick_name like '%" + nickName + "%' ");

        Query dataQuery = em.createNativeQuery(dataSql.toString());
        List results = dataQuery.getResultList();
        System.out.println(results);

        return toUserFollowPageVo(results);
    }

    private List<UserFollowPageVo> toUserFollowPageVo(List results) {
        List<UserFollowPageVo> data = new ArrayList<>();
        for (Object o : results) {
            Object[] objectArr = (Object[]) o;
            UserFollowPageVo vo = new UserFollowPageVo();
            vo.setUid(objectArr[0] == null ? 0L : Long.parseLong(objectArr[0].toString()));
            vo.setAvatarUrl(objectArr[1] == null ? "" : objectArr[1].toString());
            vo.setNickName(objectArr[2] == null ? "" : objectArr[2].toString());
            vo.setIndividualitySignature(objectArr[3] == null ? "" : objectArr[3].toString());
            data.add(vo);

        }

        return data;
    }

    private void checkFollowParam(Long userId, Long checkedUid) {
        if (null == userId)
            throw new AppException(-1, "发起人 id 不能为空");

        if (null == checkedUid) {
        	log.info("checkedUid不能为空");
        	throw new AppException(-1, "请选择关注/取消关注的用户 id");
        }
    }
}
