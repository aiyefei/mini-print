package com.delicloud.app.miniprint.core.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: dy
 * @Description:
 * @Date: 2018/9/20 19:14
 */
@Data
public class FileDto {

    private String type;

    private MultipartFile imageFile;

    private MultipartFile projectFile;

}
