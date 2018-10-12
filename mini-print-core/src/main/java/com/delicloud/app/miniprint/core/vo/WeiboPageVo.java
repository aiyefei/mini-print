package com.delicloud.app.miniprint.core.vo;

import lombok.Data;

/**
 * @Author: dy
 * @Description:
 * @Date: 2018/9/17 20:13
 */
@Data
public class WeiboPageVo {

    private Long createTime;

    private Long wbId;

    private Long uid;

    private String weiboType;

    private String title;

    private String content;

//    private Long sortNo;

    private Long collections;

    private Long views;

    private Long goods;

    private Long downloads;

    private Integer canShare;

    private Integer isTop;

//    private String referees;

    private String status;

    private String fileListJson;

    private String tagName;

    private String nickName;

    private String avatarUrl;

    /**
     * 关注状态 1: 已关注, 2: 未关注, 3:互相关注, 4: 自己
     */
    private Integer followStatus;

    /**
     * 是否已点赞 1: 已点赞 0: 未点赞
     */
    private Integer whetherGood;

    /**
     * 收藏 id
     */
    private Long cgId;

}
