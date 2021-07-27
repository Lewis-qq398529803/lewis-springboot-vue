package com.taozi.common.core.domain.model;

import com.taozi.common.enums.CommonResultEnums;
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

    @ApiModelProperty(value = "响应业务状态")
    private Integer code;

    @ApiModelProperty(value = "响应消息")
    private String msg;

    @ApiModelProperty(value = "响应中的数据")
    private Object data;

    @ApiModelProperty(value = "响应的条数")
    private Long total;

    public CommonResult() {

    }

    public CommonResult(Integer code, String msg, Object data, Long total) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.total = total;
    }

    /**
     * 基础成功对象
     *
     * @return CommonResult
     */
    public static CommonResult ok() {
        CommonResultEnums ok = CommonResultEnums.OK;
        return new CommonResult(ok.getCode(), ok.getMsg(), ok.getData(), ok.getTotal());
    }

    /**
     * 基础失败对象
     *
     * @return CommonResult
     */
    public static CommonResult fail() {
        CommonResultEnums fail = CommonResultEnums.FAIL;
        return new CommonResult(fail.getCode(), fail.getMsg(), fail.getData(), fail.getTotal());
    }

    /**
     * 根据Boolean判断是否操作成功
     *
     * @param reason -- 操作状态
     * @return CommonResult
     */
    public static CommonResult okOrFail(boolean reason) {
        if (!reason) {
            return CommonResult.fail();
        }
        return CommonResult.ok();
    }

    public CommonResult setCode(Integer code) {
        this.code = code;
        return this;
    }

    public CommonResult setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public CommonResult setData(Object data) {
        this.data = data;
        return this;
    }

    public CommonResult setTotal(long total) {
        this.total = total;
        return this;
    }
}
