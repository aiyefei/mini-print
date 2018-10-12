package com.delicloud.app.miniprint.server.Exception;

/**
 * app异常信息，可提示给用户
 *
 */
public enum AppExceptionEnum {
	SERVICE_EXCEPTION(-100, "服务器异常");


	private String msg;
	private int code;

	private AppExceptionEnum(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public String getMsg() {
		return this.msg;
	}

	public int getCode() {
		return this.code;
	}

}
