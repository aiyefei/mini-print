package com.delicloud.app.miniprint.server.dto;

import org.springframework.web.multipart.MultipartFile;

import com.delicloud.app.miniprint.core.vo.FileRespVo;

import lombok.Data;
@Data
public class FileTaskDto {
	
	private Long uId; 
	
	private MultipartFile multipartFile;
	
	private String type;
	
	private FileRespVo fileRespVo;
 
}
