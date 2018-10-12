package com.delicloud.app.miniprint.server.Exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: dy
 * @Description: 自定义异常通知
 * @Date: 2018/8/30 16:48
 */
@ControllerAdvice
public class AppExceptionAdvice {

    /**
     * 全局异常捕捉处理
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Map errorHandler(Exception ex) {
        Map map = new HashMap();
        map.put("code", 100);
        map.put("msg", ex.getMessage());
        return map;
    }

    /**
     * 拦截捕捉自定义异常 MyException.class
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = AppException.class)
    public Map myErrorHandler(AppException ex) {
        Map map = new HashMap();
        map.put("code", ex.getCode());
        map.put("msg", ex.getMsg());
        return map;
    }

}
