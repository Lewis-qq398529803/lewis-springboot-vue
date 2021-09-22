package com.lewis.common.core.domain.model;

import com.lewis.common.utils.CodeUtils;
import com.lewis.common.utils.StringUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 自定义链式响应结构
 *
 * @author Lewis - 2020/11/28 - 17:31
 */
@Data
@ApiModel("通用响应对象")
public class BaseResult {

	@ApiModelProperty(value = "成败情况")
	private Boolean success;

	@ApiModelProperty(value = "响应业务状态")
	private Integer code;

	@ApiModelProperty(value = "响应消息")
	private String msg;

	@ApiModelProperty(value = "响应中的数据")
	private Object data;

	public BaseResult(Boolean success) {
		this.success = success;
	}

	/**
	 * 基础成功方法
	 *
	 * @return CommonResult
	 */
	public static BaseResult ok() {
		return new BaseResult(true).setCode(200000);
	}

	/**
	 * 基础错误方法
	 *
	 * @return CommonResult
	 */
	public static BaseResult error() {
		return new BaseResult(false).setCode(500000);
	}

	/**
	 * 基础失败方法
	 *
	 * @return CommonResult
	 */
	public static BaseResult fail() {
		return new BaseResult(false);
	}

	/**
	 * 设置code同时设置提示信息
	 *
	 * @param code 状态码
	 * @return CommonResult
	 */
	public BaseResult setCode(Integer code) {
		this.code = code;
		if (StringUtils.isNotNull(this.msg)) {
			this.msg += CodeUtils.getMessage(code, null);
		} else {
			this.msg = CodeUtils.getMessage(code, null);
		}
		return this;
	}

	public BaseResult setMsg(String msg) {
		this.msg = msg;
		return this;
	}

	/**
	 * 设置msg尾部新增参数
	 *
	 * @param params 尾部新增参数
	 * @return CommonResult
	 */
	public BaseResult addMsgParams(String params) {
		if (StringUtils.isNotNull(this.msg)) {
			this.msg += params;
		} else {
			this.msg = params;
		}
		return this;
	}

	public BaseResult setData(Object data) {
		this.data = data;
		return this;
	}
}
