package com.ruoyi.common.config;

import org.springframework.stereotype.Component;

/**
 * 微信参数config
 *
 * @author chenyitao - 2021年6月29日, 029 - 11:06:27
 */
@Component
public class WxConfig {

    public static final String APPID = "APPID";
    public static final String APPSECRET = "APPSECRET";
    public static final String getAccessTokenUrl = "https://api.weixin.qq.com/cgi-bin/token";//获取access_token的api地址
    public static final String sendMsgUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";//发送模板消息url（缺ACCESS_TOKEN）

}
