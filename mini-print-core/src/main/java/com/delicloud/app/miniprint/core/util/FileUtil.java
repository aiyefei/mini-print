package com.delicloud.app.miniprint.core.util;

/**
 * @Author: dy
 * @Description:
 * @Date: 2018/9/14 17:42
 */
public class FileUtil {

    public static String getContentType(String fileName) {
        int index = fileName.lastIndexOf(".");
        if (index < 0)
            return "text/html";

        String fileExtension = fileName.substring(index + 1);
        if ("bmp".equalsIgnoreCase(fileExtension))
            return "image/bmp";
        if ("gif".equalsIgnoreCase(fileExtension))
            return "image/gif";
        if ("jpeg".equalsIgnoreCase(fileExtension) || "jpg".equalsIgnoreCase(fileExtension)
                || "png".equalsIgnoreCase(fileExtension))
            return "image/jpeg";
        if ("html".equalsIgnoreCase(fileExtension))
            return "text/html";
        if ("txt".equalsIgnoreCase(fileExtension))
            return "text/plain";
        if ("vsd".equalsIgnoreCase(fileExtension))
            return "application/vnd.visio";
        if ("ppt".equalsIgnoreCase(fileExtension) || "pptx".equalsIgnoreCase(fileExtension))
            return "application/vnd.ms-powerpoint";
        if ("doc".equalsIgnoreCase(fileExtension) || "docx".equalsIgnoreCase(fileExtension))
            return "application/msword";
        if ("xml".equalsIgnoreCase(fileExtension))
            return "text/xml";
        if ("pdf".equalsIgnoreCase(fileExtension))
            return "application/pdf";
        return "text/html";
    }

    public static boolean isPicture(String fileName) {

        int index = fileName.lastIndexOf(".");

        String fileExtension = fileName.substring(index + 1);

        String picSuffixs = "png, jpg, jpeg, bmp";

        if (picSuffixs.contains(fileExtension.toLowerCase()))
            return true;
        else
            return false;

    }

}
