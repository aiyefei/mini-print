package com.delicloud.app.miniprint.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.delicloud.app.miniprint.core.dto.QueryWeiboPageDto;
import com.delicloud.app.miniprint.core.dto.SendWeiboDto;
import com.delicloud.app.miniprint.core.dto.WeiboDto;
import com.delicloud.app.miniprint.core.entity.*;
import com.delicloud.app.miniprint.core.util.Md5Util;
import com.delicloud.app.miniprint.core.vo.PageVo;
import com.delicloud.app.miniprint.core.vo.WeiboPageVo;
import com.delicloud.app.miniprint.server.Exception.AppException;
import com.delicloud.app.miniprint.server.repository.*;
import com.delicloud.app.miniprint.server.service.IWeiboService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: dy
 * @Description: 微博业务层
 * @Date: 2018/8/31 16:17
 */
@Service
@Slf4j
public class WeiboServiceImpl implements IWeiboService {

    @Autowired
    private WeiboRepository weiboRepository;

    @Autowired
    private WeiboTagsRepository weiboTagsRepository;

    @Autowired
    private WeiboFileRepository weiboFileRepository;

    @Autowired
    private GoodWeiboRepository goodWeiboRepository;

    @Autowired
    private UserFollowRepository userFollowRepository;

    @Autowired
    private CollectionGroupRepository collectionGroupRepository;

    @Autowired
    private EntityManager em;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = AppException.class)
    public void sendWeibo(Long uid, SendWeiboDto sendWeiboDto) {

        checkSendWeiboDto(sendWeiboDto);

        try {

            // 新增微博
            long currentTimeMillis = System.currentTimeMillis();
            TWeibo weibo = new TWeibo();
            weibo.setTitle(sendWeiboDto.getTitle());
            weibo.setCreateTime(currentTimeMillis);
            weibo.setContent(sendWeiboDto.getContent());
            weibo.setUpdateTime(currentTimeMillis);
            weibo.setUid(uid);
            weibo.setStatus("1");   // 状态：1正常、0删除
            weibo.setIsTop(0);   // 默认不置顶
            weibo.setWeiboType(sendWeiboDto.getWeiboType());
            String jsonString = "";
            // 新增微博文件
            List<TWeiboFile> fileList = sendWeiboDto.getFileList();
            if (null != fileList && fileList.size() > 0)
                jsonString = JSON.toJSONString(fileList);
            weibo.setFileListJson(jsonString);
            weiboRepository.save(weibo);

            // 新增微博标签
            TWeiboTags weiboTags = new TWeiboTags();
            weiboTags.setCreateTime(currentTimeMillis);
            weiboTags.setUpdateTime(currentTimeMillis);
            weiboTags.setWbId(weibo.getId());
            weiboTags.setTagName(sendWeiboDto.getTagName());
            weiboTagsRepository.save(weiboTags);

            log.info("微博文件: ={}", fileList);

            if (null != fileList && fileList.size() > 0) {
                List<TWeiboFile> files = new ArrayList<>();
                for (TWeiboFile weiboFile : fileList) {
                    weiboFile.setImageUrl(weiboFile.getImageUrl() == null ? "" : weiboFile.getImageUrl());
                    weiboFile.setFileUrl(weiboFile.getFileUrl() == null ? "" : weiboFile.getFileUrl());
                    weiboFile.setCreateTime(currentTimeMillis);
                    weiboFile.setUpdateTime(currentTimeMillis);
                    weiboFile.setWbId(weibo.getId());
                    weiboFile.setName(weiboFile.getName() == null ? "" : weiboFile.getName());
                    files.add(weiboFile);
                }
                weiboFileRepository.save(files);
            }

        } catch (Exception e) {
            throw new AppException(-200, "发微博失败");
        }

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public void goodWeibo(Long uid, WeiboDto dto) {
        Long wbId = dto.getWbId();
        Integer type = dto.getType();
        TWeibo weibo = weiboRepository.findOne(wbId);
        if (null == weibo || weibo.getStatus().equals("0"))
            throw new AppException(-1, "该微博不存在");

        long currentTimeMillis = System.currentTimeMillis();
        TGoodWeibo goodWeibos = goodWeiboRepository.findByUidAndWbId(uid, wbId);
        //点赞
        if (type == 1) {
            try {
                if (null != goodWeibos)
                    throw new AppException(-1, "请勿重复点赞");

                // 更新用户微博点赞数
                weibo.setId(wbId);
                weibo.setGoods(weibo.getGoods() + 1);
                weibo.setUpdateTime(currentTimeMillis);
                weiboRepository.save(weibo);

                // 新增用户点赞的微博
                TGoodWeibo goodWeibo = new TGoodWeibo();
                goodWeibo.setCreateTime(currentTimeMillis);
                goodWeibo.setUpdateTime(currentTimeMillis);
                goodWeibo.setUid(uid);
                goodWeibo.setWbId(weibo.getId());
                goodWeibo.setWbType(weibo.getWeiboType());
                goodWeiboRepository.save(goodWeibo);
            } catch (Exception e) {
                throw new AppException(-200, "点赞失败");
            }
        } else {//取消点赞
            try {
                if (null == goodWeibos)
                    throw new AppException(-1, "已取消点赞该微博, 请刷新重试");
                //更新用户微博点赞数
                weibo.setId(wbId);
                weibo.setGoods(weibo.getGoods() - 1);
                weibo.setUpdateTime(currentTimeMillis);
                weiboRepository.save(weibo);

                //删除点赞记录
                goodWeiboRepository.delete(goodWeibos);
            } catch (Exception e) {
                throw new AppException(-200, "取消点赞失败");
            }
        }
    }

    @Override
    public void deleteWeibo(WeiboDto dto) {
        Long wbId = dto.getWbId();
        if (null == wbId)
            throw new AppException(-1, "请选择要删除的微博");
        TWeibo weibo = weiboRepository.findOne(wbId);
        if ("0".equals(weibo.getStatus()))  // 状态：1正常、0删除
            throw new AppException(-1, "该微博已删除, 请刷新重试");

        try {
            weibo.setStatus("0");
            weibo.setUpdateTime(System.currentTimeMillis());
            weiboRepository.save(weibo);
        } catch (Exception e) {
            throw new AppException(-200, "微博删除异常");
        }

    }

    @Override
    public PageVo<WeiboPageVo> queryPage(Long uid, QueryWeiboPageDto queryWeiboPageDto) {

        Long checkedUid = queryWeiboPageDto.getCheckedUid() == null ? uid : queryWeiboPageDto.getCheckedUid();

        if (null == queryWeiboPageDto.getWeiboType() && !"".equals(queryWeiboPageDto.getWeiboType()))
            throw new AppException(-1, "微博类型不能为空");

        String weiboType = queryWeiboPageDto.getWeiboType();
        Integer pageNo = queryWeiboPageDto.getPageNo();
        Integer pageSize = queryWeiboPageDto.getPageSize();

        String filedSql = " wb.id wbId, wb.uid, wb.weibo_type weiboType, wb.title, wb.content, wb.collections, wb.views, " +
                          " wb.goods, wb.downloads, wb.can_share canShare, wb.is_top isTop, wb.`status`, wb.create_time createTime, " +
                          " wb.file_list_json fileListJson, wt.tag_name tagName, " +
                          " u.nick_name nickName, u.avatar_url avatarUrl, " +
                          " (SELECT count(*) FROM tl_good_weibo WHERE uid = '" + uid + "' AND wb_id = wb.id) whetherGood, " +
                          " (SELECT cg_id FROM tl_collection_weibo WHERE uid = '" + uid + "' AND wb_id = wb.id) cgId, " +
                          " (SELECT follow_status FROM tm_user_follow WHERE from_uid = '" + uid + "' AND to_uid = " +
                          " wb.uid) followStatus ";

        String fromSql = " from tm_weibo wb " +
                         " LEFT JOIN tm_user u ON wb.uid = u.id LEFT JOIN tm_weibo_tags wt ON wb.id = wt.wb_id " +
                         " where wb.`status` = 1 and wb.weibo_type = '" + weiboType + "' ";

        String sql = " and wb.uid = '" + checkedUid +  "' ";

        if (null != queryWeiboPageDto.getType() && !"".equals(queryWeiboPageDto.getType())) {  // 关注列表
            if ("follow".equals(queryWeiboPageDto.getType())) {
                List<BigInteger> toUids = userFollowRepository.findToUidByFromUid(uid);
                String queryUid = getQueryUid(toUids, uid);
                sql = " and wb.uid in ( " + queryUid +  ") ";
            } else {    // 推荐
                sql = "";
            }
        }

        fromSql += sql;

        StringBuffer dataSql = new StringBuffer(" select * from ( select " + filedSql + fromSql);
        StringBuffer countSql = new StringBuffer(" select count(*) from ( select " + filedSql + fromSql);

        dataSql.append(generateCondition(queryWeiboPageDto));
        countSql.append(generateCondition(queryWeiboPageDto));

        Query dataQuery = em.createNativeQuery(dataSql.toString());
        Query countQuery = em.createNativeQuery(countSql.toString());
        dataQuery.setFirstResult(pageSize * (pageNo -1));
        dataQuery.setMaxResults(pageSize);

        Integer count = Integer.parseInt(countQuery.getSingleResult().toString());
        PageVo<WeiboPageVo> ouput = new PageVo<>(pageNo, pageSize, count);
        ouput.setContent(toRows(dataQuery.getResultList(), uid));
        return ouput;
    }

    private String getQueryUid(List<BigInteger> toUids, Long curUid) {
        String queryUid = curUid.toString();
        if (toUids.size() > 0) {
            for (BigInteger uid : toUids) {
                queryUid += "," + uid;
            }
        }
        return queryUid;
    }

    @Override
    public List<TWeiboFile> queryByWbId(WeiboDto dto) {
        Long wbId = dto.getWbId();
        if (null == wbId)
            throw new AppException (-1, "微博 id 不能为空");
        return weiboFileRepository.findByWbId(wbId);
    }

    @Override
    public void updateViews(WeiboDto dto) {
        Long wbId = dto.getWbId();
        if (null == wbId)
            throw new AppException(-1, "微博 id 不能为空");
        try {
            TWeibo weibo = weiboRepository.findOne(wbId);
            weibo.setUpdateTime(System.currentTimeMillis());
            weibo.setViews(weibo.getViews() + 1);
            weiboRepository.save(weibo);
        } catch (Exception e) {
            throw new AppException(-200, "新增微博浏览次数失败");
        }
    }

    @Override
    public PageVo<WeiboPageVo> queryAllWeibo(Long uid, QueryWeiboPageDto dto) {

        Long checkedUid = dto.getCheckedUid() == null ? uid : dto.getCheckedUid();

        if (null == dto.getType() && !"".equals(dto.getType()))
            throw new AppException(-1, "微博类型不能为空");

        Integer pageNo = dto.getPageNo();
        Integer pageSize = dto.getPageSize();

        String filedSql = " wb.id wbId, wb.uid, wb.weibo_type weiboType, wb.title, wb.content, wb.collections, wb.views, " +
                " wb.goods, wb.downloads, wb.can_share canShare, wb.is_top isTop, wb.`status`, wb.create_time createTime, " +
                " wb.file_list_json fileListJson, wt.tag_name tagName, " +
                " u.nick_name nickName, u.avatar_url avatarUrl, " +
                " (SELECT count(*) FROM tl_good_weibo WHERE uid = '" + uid + "' AND wb_id = wb.id) whetherGood, " +
                " (SELECT cg_id FROM tl_collection_weibo WHERE uid = '" + uid + "' AND wb_id = wb.id) cgId," +
                " (SELECT follow_status FROM tm_user_follow WHERE from_uid = '" + uid + "' AND to_uid = " +
                " wb.uid) followStatus ";

        String fromSql = " from tm_weibo wb " +
                " LEFT JOIN tm_user u ON wb.uid = u.id LEFT JOIN tm_weibo_tags wt ON wb.id = wt.wb_id " +
                " where wb.`status` = 1 ";

        String sql = "";
        if (null != dto.getType() && !"".equals(dto.getType())) {  // 关注列表
            if ("follow".equals(dto.getType())) {
                List<BigInteger> toUids = userFollowRepository.findToUidByFromUid(uid);
                String queryUid = getQueryUid(toUids, uid);
                sql = " and wb.uid in ( " + queryUid +  ") ";
            } else {    // 推荐
                sql = "";
            }
        }

        fromSql += sql;

        StringBuffer dataSql = new StringBuffer(" select * from ( select " + filedSql + fromSql);
        StringBuffer countSql = new StringBuffer(" select count(*) from ( select " + filedSql + fromSql);

        dataSql.append(generateCondition(dto));
        countSql.append(generateCondition(dto));

        Query dataQuery = em.createNativeQuery(dataSql.toString());
        Query countQuery = em.createNativeQuery(countSql.toString());
        dataQuery.setFirstResult(pageSize * (pageNo -1));
        dataQuery.setMaxResults(pageSize);

        Integer count = Integer.parseInt(countQuery.getSingleResult().toString());
        PageVo<WeiboPageVo> ouput = new PageVo<>(pageNo, pageSize, count);
        ouput.setContent(toRows(dataQuery.getResultList(), uid));
        return ouput;
    }

    @Override
    public PageVo<WeiboPageVo> queryAllWeiboByUid(QueryWeiboPageDto dto) {
        Long checkedUid = dto.getCheckedUid();
        if (null == checkedUid)
            throw new AppException(-1,"查询id不能为空");

        Integer pageNo = dto.getPageNo();
        Integer pageSize = dto.getPageSize();

        String filedSql = " wb.id wbId, wb.uid, wb.weibo_type weiboType, wb.title, wb.content, wb.collections, wb.views, " +
                " wb.goods, wb.downloads, wb.can_share canShare, wb.is_top isTop, wb.`status`, wb.create_time createTime, " +
                " wb.file_list_json fileListJson, wt.tag_name tagName, " +
                " u.nick_name nickName, u.avatar_url avatarUrl, " +
                " (SELECT count(*) FROM tl_good_weibo WHERE uid = '" + checkedUid + "' AND wb_id = wb.id) whetherGood, " +
                " (SELECT cg_id FROM tl_collection_weibo WHERE uid = '" + checkedUid + "' AND wb_id = wb.id) cgId," +
                " (SELECT follow_status FROM tm_user_follow WHERE from_uid = '" + checkedUid + "' AND to_uid = " +
                " wb.uid) followStatus ";

        String fromSql = " from tm_weibo wb " +
                " LEFT JOIN tm_user u ON wb.uid = u.id LEFT JOIN tm_weibo_tags wt ON wb.id = wt.wb_id  " +
                " where wb.`status` = 1 and wb.uid = '" + checkedUid + "' ";

        StringBuffer dataSql = new StringBuffer("select " + filedSql + fromSql);
        StringBuffer countSql = new StringBuffer(" select count(*) from ( select " + filedSql + fromSql + ") a");

        Query dataQuery = em.createNativeQuery(dataSql.toString());
        Query countQuery = em.createNativeQuery(countSql.toString());
        dataQuery.setFirstResult(pageSize * (pageNo -1));
        dataQuery.setMaxResults(pageSize);

        Integer count = Integer.parseInt(countQuery.getSingleResult().toString());
        PageVo<WeiboPageVo> ouput = new PageVo<>(pageNo, pageSize, count);
        ouput.setContent(toRows(dataQuery.getResultList(), checkedUid));
        return ouput;
    }

    @Override
    public PageVo<WeiboPageVo> queryAllWeiboByCgId(Long uid, QueryWeiboPageDto dto) {

        Long cgId = dto.getCgId();
        if (null == cgId)
            throw new AppException(-1,"分组Id不能为空");

        TCollectionGroup tCollectionGroup = collectionGroupRepository.findOne(cgId);
        if (null == tCollectionGroup)
            throw new AppException(-1,"该收藏分组不存在或已被删除");

        String weiboType = dto.getWeiboType();
        Integer pageNo = dto.getPageNo();
        Integer pageSize = dto.getPageSize();

        String filedSql = " wb.id wbId, wb.uid, wb.weibo_type weiboType, wb.title, wb.content, wb.collections, wb.views, " +
                " wb.goods, wb.downloads, wb.can_share canShare, wb.is_top isTop, wb.`status`, wb.create_time createTime, " +
                " wb.file_list_json fileListJson, wt.tag_name tagName, " +
                " u.nick_name nickName, u.avatar_url avatarUrl, " +
                " (SELECT count(*) FROM tl_good_weibo WHERE uid = '" + uid + "' AND wb_id = wb.id) whetherGood, " +
                " cw.cg_id  cgId," +
                " (SELECT follow_status FROM tm_user_follow WHERE from_uid = '" + uid + "' AND to_uid = " +
                " wb.uid) followStatus ";

        String fromSql = " from tm_weibo wb " +
                " LEFT JOIN tm_user u ON wb.uid = u.id LEFT JOIN tm_weibo_tags wt ON wb.id = wt.wb_id LEFT JOIN tl_collection_weibo cw ON wb.id = cw.wb_id " +
                " where wb.`status` = 1 and cw.cg_id = '" + cgId + "' ";

        StringBuffer dataSql = new StringBuffer("select " + filedSql + fromSql);
        StringBuffer countSql = new StringBuffer(" select count(*) from ( select " + filedSql + fromSql + ") a");

        Query dataQuery = em.createNativeQuery(dataSql.toString());
        Query countQuery = em.createNativeQuery(countSql.toString());
        dataQuery.setFirstResult(pageSize * (pageNo -1));
        dataQuery.setMaxResults(pageSize);

        Integer count = Integer.parseInt(countQuery.getSingleResult().toString());
        PageVo<WeiboPageVo> ouput = new PageVo<>(pageNo, pageSize, count);
        ouput.setContent(toRows(dataQuery.getResultList(), uid));
        return ouput;
    }

    private List<WeiboPageVo> toRows(List<Object[]> results, Long uid) {

        List<WeiboPageVo> data = new ArrayList<>();
        for (Object[] o : results) {
            long checkedUid = Long.parseLong(o[1].toString());
            WeiboPageVo vo = new WeiboPageVo();
            vo.setWbId(Long.parseLong(o[0].toString()));
            vo.setUid(checkedUid);
            vo.setWeiboType(o[2].toString());
            vo.setTitle(o[3] == null ? "" : o[3].toString());
            vo.setContent(o[4] == null ? "" : o[4].toString());
            vo.setCollections(Long.parseLong(o[5].toString()));
            vo.setViews(Long.parseLong(o[6].toString()));
            vo.setGoods(Long.parseLong(o[7].toString()));
            vo.setDownloads(Long.parseLong(o[8].toString()));
            vo.setCanShare(Integer.parseInt(o[9].toString()));
            vo.setIsTop(Integer.parseInt(o[10].toString()));
            vo.setStatus(o[11] == null ? "" : o[11].toString());
            vo.setCreateTime(Long.parseLong(o[12].toString()));
            vo.setFileListJson(o[13] == null ? "" : o[13].toString());
            vo.setTagName(o[14] == null ? "" : o[14].toString());
            vo.setNickName(o[15] == null ? "" : o[15].toString());
            vo.setAvatarUrl(o[16] == null ? "" : o[16].toString());
            vo.setWhetherGood(Integer.parseInt(o[17].toString()) > 0 ? 1 : 0);
            vo.setCgId(o[18] == null ? -1 : Long.parseLong(o[18].toString()));
            if (uid.equals(checkedUid))
                vo.setFollowStatus(o[19] == null ? 4 : Integer.parseInt(o[19].toString()));
            else
                vo.setFollowStatus(o[19] == null ? 2 : Integer.parseInt(o[19].toString()));
            data.add(vo);
        }
        return data;
    }

    private StringBuffer generateCondition(QueryWeiboPageDto queryWeiboPageDto) {

        StringBuffer sql = new StringBuffer();
        boolean orderByCollections = queryWeiboPageDto.isOrderByCollections();
        boolean orderByDownloads = queryWeiboPageDto.isOrderByDownloads();
        boolean orderByGoods = queryWeiboPageDto.isOrderByGoods();
        boolean orderByViews = queryWeiboPageDto.isOrderByViews();
        String searchParam = queryWeiboPageDto.getSearchParam();
        String content = queryWeiboPageDto.getContent();
        String title = queryWeiboPageDto.getTitle();
        String tagName = queryWeiboPageDto.getTagName();
        Long cgId = queryWeiboPageDto.getCgId();
        if (null != content && !"".equals(content))
            sql.append(" and wb.content like '%" + content + "%' ");

        if (null != title && !"".equals(title))
            sql.append(" and wb.title like '%" + title + "%' ");

        if (null != tagName && !"".equals(tagName))
            sql.append(" and wt.tag_name like '%" + tagName + "%' ");

        if (null != searchParam && !"".equals(searchParam))
            sql.append(" and (u.nick_name like '%" + searchParam + "%' or wt.tag_name LIKE '%" + searchParam + "%') ");

        String finalSql = " ORDER BY tab.createTime DESC ";
        Integer count = 0;
        if (orderByCollections || orderByDownloads || orderByGoods || orderByViews) {
            sql.append(" , ");
        } else {
            sql.append(" ) tab ");
            if (null != cgId)
                return sql.append(" WHERE tab.cgId = ' " + cgId +  "' ").append(finalSql);
            else
                return sql.append(finalSql);
        }

        if (orderByCollections) {
            count += 1;
            if (count > 0) {
                sql.append(" wb.collections ");
            }
        }

        if (orderByDownloads) {
            count += 1;
            if (count > 0) {
                if (sql.toString().contains("collections")) {
                    sql.append(" ,wb.downloads ");
                } else {
                    sql.append(" wb.downloads ");
                }
            }
        }

        if (orderByGoods) {
            count += 1;
            if (count > 0) {
                if (sql.toString().contains("collections") || sql.toString().contains("downloads")) {
                    sql.append(" ,wb.goods ");
                } else {
                    sql.append(" wb.goods ");
                }
            }
        }

        if (orderByViews) {
            count += 1;
            if (count > 0) {
                if (sql.toString().contains("collections") || sql.toString().contains("downloads")
                        || sql.toString().contains("goods")) {
                    sql.append(" ,wb.views ");
                } else {
                    sql.append(" wb.views ");
                }
            }
        }

        sql.append(" ) tab ");
        if (null != cgId)
            return sql.append(" WHERE tab.cgId = ' " + cgId +  "' ");

        return sql.append(finalSql);
    }

    /**
     * 校验入参
     * @param sendWeiboDto 入参
     */
    private void checkSendWeiboDto(SendWeiboDto sendWeiboDto) {

        if (StringUtils.isBlank(sendWeiboDto.getTagName()))
            throw new AppException(-1, "标签不能为空");

        if (null == sendWeiboDto.getFileList() || sendWeiboDto.getFileList().size() == 0)
            throw new AppException(-1, "文件信息不能为空");

//        if (StringUtils.isBlank(sendWeiboDto.getTitle()))
//            throw new AppException(-1, "微博标题不能为空");

        if (StringUtils.isBlank(sendWeiboDto.getContent()))
            throw new AppException(-1, "微博内容不能为空");
    }

    public static void main (String[] args) {
        String md5 = "M2500ADNW_L1050ADNW-1WXACNKC9200020-M2500ADNW-uXM4cQDHsvEKCuxsu2ABoFHC";
        System.out.println(Md5Util.encode(md5));
    }
}
