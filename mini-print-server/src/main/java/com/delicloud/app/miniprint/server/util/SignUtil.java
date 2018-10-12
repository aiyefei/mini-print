package com.delicloud.app.miniprint.server.util;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

public class SignUtil {

	public static byte[] toBytes(String str) {
        if(str == null || str.trim().equals("")) {
            return new byte[0];
        }

        byte[] bytes = new byte[str.length() / 2];
        for(int i = 0; i < str.length() / 2; i++) {
            String subStr = str.substring(i * 2, i * 2 + 2);
            bytes[i] = (byte) Integer.parseInt(subStr, 16);
        }

        return bytes;
    }
	
	public static String bytesToHexString(byte[] bArr) {
	    StringBuffer sb = new StringBuffer(bArr.length);
	    String sTmp;

	    for (int i = 0; i < bArr.length; i++) {
	        sTmp = Integer.toHexString(0xFF & bArr[i]);
	        if (sTmp.length() < 2)
	            sb.append(0);
	        sb.append(sTmp);
	    }

	    return sb.toString();
	}

	public static void main(String[] args) throws UnsupportedEncodingException {
		System.out.println(bytesToHexString(Base64.getEncoder().encode(new String("355672617635545088|55555555555555555|qwedfghbnmkloiuytgfrewsdcvbgrtyt").getBytes())));
		toBytes("3561576c357079763561536e356269493561536e356f6d413561536e");
        Base64.getDecoder().decode(toBytes("3561576c357079763561536e356269493561536e356f6d413561536e"));
        new String( Base64.getDecoder().decode(toBytes("3561576c357079763561536e356269493561536e356f6d413561536e")) , "utf-8");
        System.out.println(new String( Base64.getDecoder().decode(toBytes("3561576c357079763561536e356269493561536e356f6d413561536e")) , "utf-8"));
	}

}
