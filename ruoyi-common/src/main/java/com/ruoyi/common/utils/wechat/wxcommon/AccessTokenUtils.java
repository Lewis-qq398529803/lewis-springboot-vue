package com.ruoyi.common.utils.wechat.wxcommon;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.config.WxConfig;
import com.ruoyi.common.utils.http.HttpUtils;

/**
 * access_token工具类
 *
 * @author chenyitao - 2021年7月7日, 007 - 14:21:02
 */
public class AccessTokenUtils {

    public static String getAccessToken(String CODE) {
        String APPID = WxConfig.APPID;
        String SECRET = WxConfig.APPSECRET;
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?";
        String param = "appid=" + APPID + "&secret=" + SECRET + "&code=" + CODE + "&grant_type=authorization_code";
        String dataT = HttpUtils.sendGet(url, param);
        JSONObject jsonObject = JSONObject.parseObject(dataT);
        if (jsonObject == null) {
            return "获取json数据包时出现异常";
        }
        return jsonObject.toJSONString();
    }
}
