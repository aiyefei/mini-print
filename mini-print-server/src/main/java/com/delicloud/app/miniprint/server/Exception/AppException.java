package com.delicloud.app.miniprint.server.Exception;

import lombok.Data;

/**
 * @Author: dy
 * @Description: 自定义异常
 * @Date: 2018/8/30 16:48
 */
@Data
public class AppException extends RuntimeException {


    public AppException(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private int code;
    private String msg;

}
