package com.taozi.quartz.task;

import com.alibaba.fastjson.JSONObject;
import com.taozi.common.config.WxConfig;
import com.taozi.common.core.redis.RedisCache;
import com.taozi.common.utils.http.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 微信接口定时任务
 *
 * @author chenyitao - 2021年6月29日, 029 - 11:04:57
 */
@Component("WxTask")
public class WxTask {

    @Autowired
    private RedisCache redisCache;

    /**
     * 每两小时获取微信的access_token，并存入redis中
     * 0 0 0/2 * * ?  -- 从 0 小时开始,每 2 小时执行一次
     */
    public void getAccessToken(){
        String params = "grant_type=client_credential&appid=" + WxConfig.APPID + "&secret=" + WxConfig.APPSECRET;
        String url = "https://api.weixin.qq.com/cgi-bin/token";
        String ACCESS_TOKEN_t = HttpUtils.sendGet(url, params);
        JSONObject jsonObject = JSONObject.parseObject(ACCESS_TOKEN_t);
        String ACCESS_TOKEN = jsonObject.getString("access_token");
        //redis缓存
        redisCache.setCacheObject("ACCESS_TOKEN", ACCESS_TOKEN,7200, TimeUnit.SECONDS);
    }

}
