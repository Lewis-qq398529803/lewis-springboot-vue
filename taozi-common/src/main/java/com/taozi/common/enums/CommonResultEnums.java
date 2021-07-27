package com.taozi.common.enums;

/**
 * 通用返回result的枚举类型
 *
 * @author taozi - 2021年7月19日, 019 - 10:59:58
 */
public enum CommonResultEnums {

    OK(200, "success" , null, 0), FAIL(0, "fail" , null, 0);

    private final Integer code;

    private final String msg;

    private final Object data;

    private final Long total;

    CommonResultEnums(Integer status, String msg, Object data, long total) {
        this.code = status;
        this.msg = msg;
        this.data = data;
        this.total = total;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public Object getData() {
        return data;
    }

    public Long getTotal() {
        return total;
    }
}
