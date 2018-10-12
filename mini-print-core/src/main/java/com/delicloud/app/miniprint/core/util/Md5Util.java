package com.delicloud.app.miniprint.core.util;

import org.apache.commons.codec.binary.Hex;

import java.security.MessageDigest;

/**
 * @Author: dy
 * @Description:
 * @Date: 2018/8/31 17:25
 */
public class Md5Util {

    public static String encode(String src) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] bs = md5.digest(src.getBytes());
            return new String(new Hex().encode(bs));
        } catch (Exception e) {
            return null;
        }
    }

}
