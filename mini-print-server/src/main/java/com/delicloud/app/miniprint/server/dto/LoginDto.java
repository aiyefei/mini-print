package com.delicloud.app.miniprint.server.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户登录返回 dto
 */
@Data
public class LoginDto implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long uid;
	
	private String token;

}
