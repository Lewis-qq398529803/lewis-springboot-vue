package com.lewis.core.enums;

/**
 * 用户状态
 *
 * @author Lewis
 */
public enum UserStatus {

	OK("0", "正常"),
	DISABLE("1", "停用"),
	DELETED("2", "删除");

	/**
	 * 状态码
	 */
	private final String code;

	/**
	 * 信息
	 */
	private final String info;

	UserStatus(String code, String info) {
		this.code = code;
		this.info = info;
	}

	public String getCode() {
		return code;
	}

	public String getInfo() {
		return info;
	}
}
