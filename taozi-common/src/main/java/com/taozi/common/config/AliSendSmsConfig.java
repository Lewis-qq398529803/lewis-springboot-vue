package com.taozi.common.config;

import lombok.Data;

/**
 * 阿里云短信服务配置类
 *
 * @author taozi - 2021年6月15日, 015 - 18:24:00
 */
@Data
public class AliSendSmsConfig {

    private String phoneNumbers;

    private String templateCode;

    private String templateParam;

}
