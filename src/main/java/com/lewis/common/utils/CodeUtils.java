package com.lewis.common.utils;import lombok.Data;import java.nio.charset.StandardCharsets;import java.text.MessageFormat;import java.util.ResourceBundle;/** * 读取配置文件返回状态码信息 * * @author Lewis * @date 2021/07/27 05:39 **/@Datapublic class CodeUtils {    /**     * 获得状态码对应的提示信息     *     * @param code 状态码     * @param params  匹配参数     * @return 返回提示信息     */    public static String getMessage(Integer code, String... params) {        ResourceBundle bundle = ResourceBundle.getBundle("message/code");        String msg = bundle.getString(String.valueOf(code));        msg = new String(msg.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);        if (null != params && params.length > 0) {            MessageFormat messageFormat = new MessageFormat(msg);            msg = messageFormat.format(params);        }        return msg;    }}