package com.taozi.common.core.domain.model;

import com.taozi.common.utils.CodeUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 自定义链式响应结构
 *
 * @author taozi - 2020/11/28 - 17:31
 */
@Data
@ApiModel("通用响应对象")
public class CommonResult {

	@ApiModelProperty(value = "成败情况")
	private Boolean success;

	@ApiModelProperty(value = "响应业务状态")
	private Integer code;

	@ApiModelProperty(value = "响应消息")
	private String msg;

	@ApiModelProperty(value = "响应中的数据")
	private Object data;

	public CommonResult(Boolean success) {
		this.success = success;
	}

	/**
	 * 基础成功方法
	 *
	 * @return CommonResult
	 */
	public static CommonResult ok() {
		return new CommonResult(true).setCode(200000);
	}

	/**
	 * 基础错误方法
	 *
	 * @return CommonResult
	 */
	public static CommonResult error() {
		return new CommonResult(false).setCode(500000);
	}

	/**
	 * 基础失败方法
	 *
	 * @return CommonResult
	 */
	public static CommonResult fail() {
		return new CommonResult(false);
	}

	/**
	 * 通过判断reason确定返回ok or fail
	 *
	 * @param reason boolean值
	 * @return CommonResult
	 */
	public static CommonResult okOrFail(Boolean reason) {
		return reason ? ok() : fail();
	}

	/**
	 * 设置code同时设置提示信息
	 *
	 * @param code 状态码
	 * @return CommonResult
	 */
	public CommonResult setCode(Integer code) {
		this.code = code;
		this.msg += CodeUtils.getMessage(code, null);
		return this;
	}

	public CommonResult setMsg(String msg) {
		this.msg = msg;
		return this;
	}

	/**
	 * 设置msg尾部新增参数
	 *
	 * @param params 尾部新增参数
	 * @return CommonResult
	 */
	public CommonResult addMsgParams(String params) {
		this.msg += params;
		return this;
	}

	public CommonResult setData(Object data) {
		this.data = data;
		return this;
	}
}
