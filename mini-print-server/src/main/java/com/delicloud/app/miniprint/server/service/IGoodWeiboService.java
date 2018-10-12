package com.delicloud.app.miniprint.server.service;

public interface IGoodWeiboService {

    /**
     * 点赞微博
     * @param  uid 点赞人的id
     * @param touid 被点赞的微博人的id
     * @param weiboid 被点赞微博的id
     */
    void goodWeiBo(String uid,String touid,String weiboid);

}
