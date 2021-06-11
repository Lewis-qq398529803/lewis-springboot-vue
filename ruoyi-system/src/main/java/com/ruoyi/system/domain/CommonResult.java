package com.ruoyi.system.domain;

import lombok.Data;

/**
 * 自定义响应结构
 *
 * @author taozi - 2020/11/28 - 17:31
 */
@Data
public class CommonResult {

    // 响应业务状态 默认0：失败 200：成功
    private Integer status;

    // 响应消息
    private String msg;

    // 响应中的数据
    private Object data;

    //响应的条数
    private long total;

    public CommonResult setStatus(Integer status) {
        this.status = status;
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

    /**
     * 基础成功对象
     *
     * @return CommonResult
     */
    public static CommonResult ok() {
        return new CommonResult().setStatus(200).setMsg("ok");
    }

    /**
     * 基础失败对象
     *
     * @return CommonResult
     */
    public static CommonResult fail() {
        return new CommonResult().setStatus(0).setMsg("fail");
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
}
