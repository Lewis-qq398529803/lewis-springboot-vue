package com.lewis.core.base.domain;

import com.lewis.core.constant.HttpStatus;
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

    @ApiModelProperty(value = "响应业务状态")
    private Integer code;

    @ApiModelProperty(value = "响应消息")
    private String msg;

    @ApiModelProperty(value = "响应中的数据")
    private Object data;

    /**
     * 初始化一个新创建的 BaseResult 对象，使其表示一个空消息。
     */
    public BaseResult() {
    }

    /**
     * 初始化一个新创建的 BaseResult 对象
     *
     * @param code 状态码
     * @param msg  返回内容
     */
    public BaseResult(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 初始化一个新创建的 BaseResult 对象
     *
     * @param code 状态码
     * @param msg  返回内容
     * @param data 数据对象
     */
    public BaseResult(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 基础成功方法
     *
     * @return BaseResult
     */
    public static BaseResult ok() {
        return BaseResult.ok(null);
    }

    /**
     * 基础成功方法 + data
     *
     * @return BaseResult
     */
    public static BaseResult ok(Object data) {
        return BaseResult.ok("操作成功" , data);
    }

    /**
     * 基础成功方法 + msg
     *
     * @param msg 返回内容
     * @return BaseResult
     */
    public static BaseResult ok(String msg) {
        return BaseResult.ok(msg, null);
    }

    /**
     * 基础成功方法 + msg + data
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return BaseResult
     */
    public static BaseResult ok(String msg, Object data) {
        return new BaseResult(HttpStatus.SUCCESS, msg, data);
    }

    /**
     * 基础失败方法
     *
     * @return BaseResult
     */
    public static BaseResult fail() {
        return BaseResult.fail("操作失败");
    }

    /**
     * 基础失败方法
     * @param msg 返回内容
     * @return BaseResult
     */
    public static BaseResult fail(String msg) {
        return BaseResult.fail(msg, null);
    }

    /**
     * 基础失败方法 + msg + data
     * @param msg 返回内容
     * @param data 数据对象
     * @return BaseResult
     */
    public static BaseResult fail(String msg, Object data) {
        return new BaseResult(HttpStatus.ERROR, msg, data);
    }

    /**
     * 基础失败方法 + code + msg
     *
     * @param code 状态码
     * @param msg  返回内容
     * @return BaseResult
     */
    public static BaseResult fail(Integer code, String msg) {
        return new BaseResult(code, msg, null);
    }

    /**
     * 设置code同时设置提示信息
     *
     * @param code 状态码
     * @return BaseResult
     */
    public BaseResult setCode(Integer code) {
        this.code = code;
        return this;
    }

    public BaseResult setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public BaseResult setData(Object data) {
        this.data = data;
        return this;
    }
}
