package com.delicloud.app.miniprint.core.vo;

import com.delicloud.platform.common.lang.bo.RespBase;

import java.util.HashMap;

/**
 * @Author: dy
 * @Description:
 * @Date: 2018/9/21 10:26
 */
public class ResponseVo<T> {

    public static RespBase OK_RESPONSE () {
        RespBase respBase = new RespBase();
        respBase.setCode(0);
        respBase.setMsg("");
        respBase.setData(new HashMap());
        return respBase;
    }

    public RespBase<T> ok(T data) {
        RespBase respBase = new RespBase();
        respBase.setCode(0);
        respBase.setMsg("");
        respBase.setData(data);
        return respBase;
    }

}
