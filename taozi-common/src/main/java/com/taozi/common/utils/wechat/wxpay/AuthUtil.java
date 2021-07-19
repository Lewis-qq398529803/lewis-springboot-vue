package com.taozi.common.utils.wechat.wxpay;

import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class AuthUtil {
    public static final String APPID = "wx651a9a8f752e6b0b";//平台ID
    public static final String APPSECRET = "14fe4a49984a190d4e45a2afa322b90b";//平台密钥
    public static final String MCHID = "1558146031";//商家ID || 微信支付商户号
    public static final String PATERNERKEY = "14fe4a49984a190d4e45a2afa322b90b";//商家密钥


    public static JSONObject doGetJson(String url) throws ClientProtocolException, IOException {
        JSONObject jsonObject = null;
        // 首先初始化HttpClient对象
        DefaultHttpClient client = new DefaultHttpClient();
        // 通过get方式进行提交
        HttpGet httpGet = new HttpGet(url);
        // 通过HTTPclient的execute方法进行发送请求
        HttpResponse response = client.execute(httpGet);
        // 从response里面拿自己想要的结果
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            String result = EntityUtils.toString(entity, "UTF-8");
            jsonObject = jsonObject.fromObject(result);
        }
        // 把链接释放掉
        httpGet.releaseConnection();
        return jsonObject;
    }
}
