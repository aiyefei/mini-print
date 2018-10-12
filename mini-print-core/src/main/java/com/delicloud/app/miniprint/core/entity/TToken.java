package com.delicloud.app.miniprint.core.entity;

import com.delicloud.platform.common.data.entity.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
/***
 * token信息
 */
@EqualsAndHashCode(callSuper = true)
@Data

public class TToken  extends BaseEntity {
	
	protected static final String TABLE_NAME = "tm_token";
	
	private String uId;
	
	private String token;
	
	private Long createTime;
	
    private String timestamp;
    
    private String sign;
	

}
