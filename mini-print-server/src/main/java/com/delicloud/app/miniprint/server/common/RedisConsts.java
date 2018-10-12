package com.delicloud.app.miniprint.server.common;

/**
 * 缓存key配置
 */
public class RedisConsts {

    private final static String APP_CACHE_KEY_PREFIX= "app-miniPrint";

    /**
     * 缓存登录用户Session数据
     */
    public final static String APP_CACHE_KEY_USER_SESSION = APP_CACHE_KEY_PREFIX + ":user:session";


}