package com.taozi.common.utils.dingding.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 签名结果vo
 *
 * @author TAOZI
 */
@Data
@ApiModel(value="SignVO对象", description="签名vo")
public class SignResultVO implements Serializable {

    @ApiModelProperty(value = "nonceStr")
    private String nonceStr;

    @ApiModelProperty(value = "timeStamp")
    private Long timeStamp;

    @ApiModelProperty(value = "signature")
    private String signature;

    @ApiModelProperty(value = "微应用ID")
    private String agentId;

    @ApiModelProperty(value = "企业ID")
    private String corpId;
}
