package com.delicloud.app.miniprint.core.dto;

import lombok.Data;

/**
 * @Author: dy
 * @Description: 微博分页入参
 * @Date: 2018/9/4 15:40
 */
@Data
public class QueryWeiboPageDto extends PageDto {

    /**
     * 选择的用户 id
     */
    private Long checkedUid;

    /**
     * 收藏分组 id
     */
    private Long cgId;

    /**
     * 标签名称
     */
    private String tagName;

    /**
     * 标签 id
     */
    private Long tagId;

    /**
     * 微博类型
     */
    private String weiboType;

    /**
     * 微博标题
     */
    private String title;

    /**
     * 微博内容
     */
    private String content;

    /**
     * 搜索框里面的内容, 用户昵称/标签名称
     */
    private String searchParam;

    /**
     * 是否根据点赞数倒序
     */
    private boolean orderByGoods;

    /**
     * 是否根据收藏数倒序
     */
    private boolean orderByCollections;

    /**
     * 是否根据浏览数倒序
     */
    private boolean orderByViews;

    /**
     * 是否根据下载数倒序
     */
    private boolean orderByDownloads;

    /**
     * 查询类型: follow 查看关注 recommend 查看推荐
     */
    private String type;

}
