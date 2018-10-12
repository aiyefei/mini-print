package com.delicloud.app.miniprint.core.dto;

import com.delicloud.app.miniprint.core.entity.TWeiboFile;
import lombok.Data;

import java.util.List;

/**
 * @Author: dy
 * @Description: 发微博入参
 * @Date: 2018/9/4 15:40
 */
@Data
public class SendWeiboDto {

    /**
     * 微博类型
     */
    private String weiboType;

    /**
     * 微博标题
     */
    private String title;

    /**
     * 微博内容
     */
    private String content;

    /**
     * 标签名
     */
    private String tagName;

    /**
     * 图片列表(详见文件信息)
     */
    private List<TWeiboFile> fileList;

}
