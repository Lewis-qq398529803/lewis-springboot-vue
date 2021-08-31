package com.taozi.common.utils.ip;

import com.alibaba.fastjson.JSONObject;
import com.taozi.common.config.TaoZiConfig;
import com.taozi.common.constant.Constants;
import com.taozi.common.utils.StringUtils;
import com.taozi.common.utils.http.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 获取地址类
 *
 * @author taozi
 */
public class AddressUtils {
    // IP地址查询
    public static final String IP_URL = "http://whois.pconline.com.cn/ipJson.jsp";
    // 未知地址
    public static final String UNKNOWN = "XX XX";
    private static final Logger log = LoggerFactory.getLogger(AddressUtils.class);

    public static String getRealAddressByIp(String ip) {
        String address = UNKNOWN;
        // 内网不查询
        if (IpUtils.internalIp(ip)) {
            return "内网IP";
        }
        if (TaoZiConfig.isAddressEnabled()) {
            try {
                String rspStr = HttpUtils.doGet(IP_URL, "ip=" + ip + "&json=true" , Constants.GBK);
                if (StringUtils.isEmpty(rspStr)) {
                    log.error("获取地理位置异常 {}" , ip);
                    return UNKNOWN;
                }
                JSONObject obj = JSONObject.parseObject(rspStr);
                String region = obj.getString("pro");
                String city = obj.getString("city");
                return String.format("%s %s" , region, city);
            } catch (Exception e) {
                log.error("获取地理位置异常 {}" , ip);
            }
        }
        return address;
    }
}
