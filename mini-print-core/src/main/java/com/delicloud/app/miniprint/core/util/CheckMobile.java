package com.delicloud.app.miniprint.core.util;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: dy
 * @Description: 正则表达式校验手机号
 * @Date: 2018/8/31 16:23
 */
public class CheckMobile {
    public static boolean isMobileNO(String mobiles){

        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,3-9]))\\d{8}$");

        Matcher m = p.matcher(mobiles);

        System.out.println(m.matches()+"---");

        return m.matches();

    }

    public static void main(String[] args) throws IOException {

        System.out.println(CheckMobile.isMobileNO("13971649205"));

    }

}
