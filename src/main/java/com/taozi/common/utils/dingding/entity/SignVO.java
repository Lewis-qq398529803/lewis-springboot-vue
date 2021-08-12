package com.taozi.common.utils.dingding.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 签名vo
 *
 * @author TAOZI
 */
@Data
@ApiModel(value="SignVO对象", description="签名vo")
public class SignVO implements Serializable {

    @ApiModelProperty(value = "jsTicket")
    private String jsTicket;

    @ApiModelProperty(value = "nonceStr")
    private String nonceStr;

    @ApiModelProperty(value = "timeStamp")
    private Long timeStamp;

    @ApiModelProperty(value = "url")
    private String url;
}
