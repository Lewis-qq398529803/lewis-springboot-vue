package com.taozi.common.utils.http.entity;

import lombok.Data;

/**
 * httputils请求头实体类
 *
 * @author taozi - 2021年8月31日, 031 - 9:28:46
 */
@Data
public class RequestPropertyEntity {
	private String accept;
	private String connection;
	private String userAgent;
	private String acceptCharset;
	private String contentType;
	private String authorization;
}
