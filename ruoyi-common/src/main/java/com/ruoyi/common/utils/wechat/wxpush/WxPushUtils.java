package com.ruoyi.common.utils.wechat.wxpush;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.common.utils.wechat.wxpush.vo.WxPushVo;

/**
 * 微信公众号推送utils
 *
 * @author chenyitao - 2021年6月29日, 029 - 10:52:01
 */
public class WxPushUtils {

    /**
     * 发送信息
     *
     * @param wxPushVo
     * @return
     */
    public static boolean sendWxMsg2User(WxPushVo wxPushVo) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("touser", wxPushVo.getOpenid());
        jsonObject.put("template_id", wxPushVo.getTemplate_id());
        jsonObject.put("url", wxPushVo.getToUrl());
        jsonObject.put("data", wxPushVo.getData());
        String sendPostResultT = HttpUtils.sendPost(wxPushVo.getUrl(), jsonObject.toJSONString());
        JSONObject sendPostResult = JSONObject.parseObject(sendPostResultT);
        return sendPostResult.getInteger("errcode") == 0;
    }
}
