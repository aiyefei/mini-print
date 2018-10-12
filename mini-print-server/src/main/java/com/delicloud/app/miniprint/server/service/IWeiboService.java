package com.delicloud.app.miniprint.server.service;

import com.delicloud.app.miniprint.core.dto.QueryWeiboPageDto;
import com.delicloud.app.miniprint.core.dto.SendWeiboDto;
import com.delicloud.app.miniprint.core.dto.WeiboDto;
import com.delicloud.app.miniprint.core.entity.TWeiboFile;
import com.delicloud.app.miniprint.core.vo.PageVo;
import com.delicloud.app.miniprint.core.vo.WeiboPageVo;

import java.util.List;

/**
 * @Author: dy
 * @Description:
 * @Date: 2018/8/31 16:16
 */
public interface IWeiboService {

    /**
     * 发微博
     * @param sendWeiboDto 微博入参
     */
    void sendWeibo(Long uid, SendWeiboDto sendWeiboDto);

    /**
     * 点赞微博
     * @param dto 入参
     */
    void goodWeibo(Long uid, WeiboDto dto);

    /**
     * 删除微博
     * @param dto 入参
     */
    void deleteWeibo(WeiboDto dto);

    /**
     * 分页查询微博列表
     * @param queryWeiboPageDto 查询条件
     * @return
     */
    PageVo<WeiboPageVo> queryPage(Long uid, QueryWeiboPageDto queryWeiboPageDto);

    /**
     * 根据微博 id 查询文件列表
     * @param dto 入参
     * @return
     */
    List<TWeiboFile> queryByWbId(WeiboDto dto);

    /**
     * 更新微博浏览次数
     * @param dto 入参
     */
    void updateViews(WeiboDto dto);

    PageVo<WeiboPageVo> queryAllWeibo(Long uid, QueryWeiboPageDto dto);

    PageVo<WeiboPageVo> queryAllWeiboByUid(QueryWeiboPageDto dto);

    PageVo<WeiboPageVo> queryAllWeiboByCgId(Long uid, QueryWeiboPageDto dto);
}
