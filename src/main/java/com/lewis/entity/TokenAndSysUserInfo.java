package com.lewis.entity;

import com.lewis.common.core.domain.entity.SysUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 手机验证码返回token同时返回用户信息对象数据实体类封装
 *
 * @author Lewis - 2021年6月18日, 018 - 15:15:47
 */
@Data
@ApiModel(value = "TokenAndSysUserInfo" , description = "手机验证码返回token同时返回用户信息对象数据实体类封装")
public class TokenAndSysUserInfo {

    @ApiModelProperty(value = "token")
    private String token;

    @ApiModelProperty(value = "用户信息")
    private SysUser sysUser;

}
