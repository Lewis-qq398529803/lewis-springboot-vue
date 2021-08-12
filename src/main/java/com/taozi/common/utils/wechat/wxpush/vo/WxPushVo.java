package com.taozi.common.utils.wechat.wxpush.vo;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 微信公众号模板消息推送vo
 *
 * @author taozi - 2021年6月29日, 029 - 14:32:27
 */
@Data
@ApiModel("微信公众号模板消息推送vo")
public class WxPushVo {

    @ApiModelProperty(value = "模板id")
    private String template_id;

    @ApiModelProperty(value = "接收者openid")
    private String openid;

    @ApiModelProperty(value = "data数据")
    private JSONObject data;

    @ApiModelProperty(value = "微信code")
    private String code;

    @ApiModelProperty(value = "凭证")
    private String ACCESS_TOKEN;

    @ApiModelProperty(value = "房东名")
    private String masterName;

    @ApiModelProperty(value = "租客名")
    private String tenantName;

    @ApiModelProperty(value = "房号")
    private String houseNum;

    @ApiModelProperty(value = "租约时间")
    private String leaseTime;

}
