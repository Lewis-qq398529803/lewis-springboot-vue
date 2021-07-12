package com.taozi.common.utils.wechat.wxpush.vo;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * 推送租约信息
 *
 * @author chenyitao - 2021年6月29日, 029 - 14:32:27
 */
@Data
public class WxPushVo {
    private String template_id;//模板id
    private String openid;//接收者openid
    private String url;//调用url
    private String toUrl;//跳转url
    private JSONObject data;//data数据
    private String ACCESS_TOKEN;//凭证
    private String masterName;//房东名
    private String tenantName;//租客名
    private String houseNum;//房号
    private String leaseTime;//租约时间
}
