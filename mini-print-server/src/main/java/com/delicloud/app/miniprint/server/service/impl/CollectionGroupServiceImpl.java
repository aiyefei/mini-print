package com.delicloud.app.miniprint.server.service.impl;

import com.delicloud.app.miniprint.core.constants.ErrorCode;
import com.delicloud.app.miniprint.core.dto.AddGroupDto;
import com.delicloud.app.miniprint.core.dto.CreateGroupDto;
import com.delicloud.app.miniprint.core.dto.UpdateGroupNameDto;
import com.delicloud.app.miniprint.core.entity.TCollectionGroup;
import com.delicloud.app.miniprint.core.entity.TCollectionWeibo;
import com.delicloud.app.miniprint.core.entity.TWeibo;
import com.delicloud.app.miniprint.core.vo.CollectionGroupVo;
import com.delicloud.app.miniprint.server.Exception.AppException;
import com.delicloud.app.miniprint.server.repository.CollectionGroupRepository;
import com.delicloud.app.miniprint.server.repository.CollectionWeiboRepository;
import com.delicloud.app.miniprint.server.repository.WeiboRepository;
import com.delicloud.app.miniprint.server.service.ICollectionGroupService;
import com.delicloud.platform.common.lang.util.MyFastBeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import java.util.ArrayList;
import java.util.List;

@Service
public class CollectionGroupServiceImpl implements ICollectionGroupService {

    @Autowired
    private CollectionGroupRepository collectionGroupRepository;

    @Autowired
    private CollectionWeiboRepository collectionWeiboRepository;

    @Autowired
    private WeiboRepository weiboRepository;

    @Override
    public void createGroup(Long uid, CreateGroupDto dto) {
        String groupName = dto.getGroupName();
        String groupRemark = dto.getGroupRemark();

        checkCreateGroupParam(uid, groupName, groupRemark);

        if (StringUtils.isBlank(groupRemark))
            groupRemark = groupName;

        //先判断该分组是否已存在
        TCollectionGroup tGroup = collectionGroupRepository.findByUidAndGroupName(Long.valueOf(uid), groupName);

        if (tGroup != null)
            throw  new AppException(ErrorCode.ERROR, "该分组名已存在");

        try {
            long currentTimeMillis = System.currentTimeMillis();

            TCollectionGroup tCollectionGroup = new TCollectionGroup();
            tCollectionGroup.setUid(Long.valueOf(uid));
            tCollectionGroup.setGroupName(groupName);
            tCollectionGroup.setGroupRemark(groupRemark);
            tCollectionGroup.setCreateTime(currentTimeMillis);

            collectionGroupRepository.save(tCollectionGroup);
        }catch (Exception e) {
            throw new AppException(ErrorCode.ERROR, "创建分组失败");
        }

    }

    /**
     * 校验创建分组参数
     * @param uid 创建人用户 id
     * @param groupName 分组名称
     * @param groupRemark 分组备注
     */
    private void checkCreateGroupParam(Long uid, String groupName, String groupRemark) {

        if (null == uid)
            throw new AppException(-1, "用户 id 不得为空");

        if (StringUtils.isBlank(groupName))
            throw new AppException(-1, "分组名称不得为空");

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public void operate(Long uid, AddGroupDto dto) {
        Integer type = dto.getType();
        Long cgId = dto.getCgId();
        Long wbId = dto.getWbId();
        checkAddGroupParam(uid, cgId, wbId, type);

        TWeibo weibo = weiboRepository.findOne(wbId);
        if (null == weibo || "0".equals(weibo.getStatus()))
            throw new AppException(-1, "该微博不存在或已被删除");

        TCollectionWeibo collectionWeibo = collectionWeiboRepository.findByUidAndWbId(uid, wbId);

        if( 2 == type)
            cgId = collectionWeibo.getCgId();

        TCollectionGroup collectionGroup = collectionGroupRepository.findOne(cgId);
        if (null == collectionGroup)
            throw new AppException(-1, "该收藏分组不存在或已被删除");

        long currentTimeMillis = System.currentTimeMillis();

        if (1 == type) {    // 加入分组

            if (null != collectionWeibo)
                throw new AppException(ErrorCode.ERROR, "该微博已被收藏");

            try {
                TCollectionWeibo tCollectionWeibo = new TCollectionWeibo();
                tCollectionWeibo.setUid(uid);
                tCollectionWeibo.setCgId(cgId);
                tCollectionWeibo.setWbId(wbId);
                tCollectionWeibo.setCreateTime(currentTimeMillis);
                tCollectionWeibo.setUpdateTime(currentTimeMillis);

                collectionWeiboRepository.save(tCollectionWeibo);

                weibo.setUpdateTime(currentTimeMillis);
                weibo.setCollections(weibo.getCollections() + 1);
                weiboRepository.save(weibo);

                collectionGroup.setUpdateTime(currentTimeMillis);
                collectionGroup.setContentCount(collectionGroup.getContentCount() + 1);
                collectionGroupRepository.save(collectionGroup);
            } catch (Exception e) {
                throw new AppException(-200, "加入分组失败");
            }

        } else if (2 == type) { // 取消分组

            if (null == collectionWeibo)
                throw new AppException(ErrorCode.ERROR, "该微博尚未加入分组");

            try {
                // 删除分组信息
                collectionWeiboRepository.delete(collectionWeibo);

                weibo.setUpdateTime(currentTimeMillis);
                if (weibo.getCollections() > 0)
                    weibo.setCollections(weibo.getCollections() - 1);
                weiboRepository.save(weibo);

                collectionGroup.setUpdateTime(currentTimeMillis);
                if (collectionGroup.getContentCount() > 0)
                    collectionGroup.setContentCount(collectionGroup.getContentCount() - 1);
                collectionGroupRepository.save(collectionGroup);
            } catch (Exception e) {
                throw new AppException(-200, "取消分组失败");
            }

        } else {    // 移动分组

            if (null == collectionWeibo)
                throw new AppException(ErrorCode.ERROR, "该微博尚未加入分组");

            if (collectionWeibo.getCgId().toString().equals(cgId.toString()))
                throw new AppException(-1, "该微博已存在该分组");

            try {

                collectionGroup.setContentCount(collectionGroup.getContentCount() + 1);
                collectionGroup.setUpdateTime(currentTimeMillis);
                collectionGroupRepository.save(collectionGroup);

                TCollectionGroup tCollection = collectionGroupRepository.findOne(collectionWeibo.getCgId());
                if (tCollection.getContentCount() > 0)
                    tCollection.setContentCount(tCollection.getContentCount() - 1);
                tCollection.setUpdateTime(currentTimeMillis);
                collectionGroupRepository.save(tCollection);


                collectionWeibo.setCgId(cgId);
                collectionWeibo.setUpdateTime(currentTimeMillis);
                collectionWeiboRepository.save(collectionWeibo);

            } catch (Exception e) {
                throw new AppException(-200, "移动分组失败");
            }

        }

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public void deleteGroup(AddGroupDto dto) {
        Long cgId = dto.getCgId();
        if (null == cgId)
            throw new AppException(-1, "用户收藏分组 id 不能为空");
        try {
            collectionGroupRepository.delete(cgId);

            // 删除该分组下面的所有微博
            List<TCollectionWeibo> collectionWeibos = collectionWeiboRepository.findByCgId(cgId);
            if (!CollectionUtils.isEmpty(collectionWeibos))
                collectionWeiboRepository.deleteInBatch(collectionWeibos);

        } catch (Exception e) {
            throw new AppException(-200, "删除用户收藏分组异常");
        }

    }

    @Override
    public void updateGroupName(Long uid, UpdateGroupNameDto dto) {
        Long cgId = dto.getCgId();
        String groupName = dto.getGroupName();

        checkUpdateGroupNameParam(uid, cgId, groupName);

        //判断新输入的名字是否已存在
        TCollectionGroup tGroup = collectionGroupRepository.findOne(cgId);

        if (null == tGroup)
            throw new AppException(ErrorCode.ERROR, "分组信息不存在或已被删除");

        try {
            String beforeGroupName = tGroup.getGroupName();
            if (null != beforeGroupName && groupName.equals(beforeGroupName))
                throw new AppException(ErrorCode.ERROR, "该分组名称已存在");
            long currentTimeMillis = System.currentTimeMillis();
            tGroup.setGroupName(groupName);
            tGroup.setUpdateTime(currentTimeMillis);
            tGroup.setUpdateBy(uid);
            collectionGroupRepository.save(tGroup);

        }catch (Exception e){
            throw new AppException(-200,"分组重命名异常");
        }
    }

    @Override
	public List<CollectionGroupVo> getMyGroups(Long uid, UpdateGroupNameDto dto) {
		if (null == uid)
			throw new AppException(-1, "用户 id 不能为空");

		List<CollectionGroupVo> groupVoList = new ArrayList<>();
		List<TCollectionGroup> collectionGroups = collectionGroupRepository.findByUid(uid);
		if (!CollectionUtils.isEmpty(collectionGroups)) {
			for (TCollectionGroup collectionGroup : collectionGroups) {
				CollectionGroupVo groupVo = new CollectionGroupVo();
				groupVo.setId(collectionGroup.getId());
				groupVo.setGroupConetntCount(collectionGroup.getContentCount());
				groupVo.setGroupName(collectionGroup.getGroupName() == null ? "" : collectionGroup.getGroupName());
				groupVo.setGroupRemark(collectionGroup.getGroupRemark() == null ? "" : collectionGroup.getGroupRemark());
				groupVoList.add(groupVo);
			}
		}
		return groupVoList;
	}

    /**
     * 校验分组重命名入参
     * @param uid 用户id
     * @param cgId 新分组名称
     * @param groupName 旧分组名称
     */
    private void checkUpdateGroupNameParam(Long uid, Long cgId, String groupName){
        if (null == uid)
            throw new AppException(ErrorCode.ERROR,"用户 id 不能为空");
        if (null == cgId)
            throw new AppException(ErrorCode.ERROR,"分组 id 不能为空");
        if (StringUtils.isBlank(groupName))
            throw new AppException(ErrorCode.ERROR,"分组名称不能为空");
    }
    /**
     * 校验将微博加入分组入参
     * @param uid 用户 id
     * @param cgId 收藏分组 id
     * @param wbId 微博 id
     */
    private void checkAddGroupParam(Long uid, Long cgId, Long wbId, Integer type) {

        if (null == uid)
            throw new AppException(-1, "用户 id 不得为空");

        if (null == type)
            throw new AppException(-1, "请选择操作类型  1: 加入分组, 2: 取消分组, 3: 移动分组");

        if (2 != type)
            if (null == cgId)
                throw new AppException(-1, "收藏分组 id 不得为空");

        if (null == wbId)
            throw new AppException(-1, "微博 id 不得为空");

    }
}
