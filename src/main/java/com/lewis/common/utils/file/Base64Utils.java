package com.lewis.common.utils.file;

import org.apache.commons.codec.binary.Base64;

import java.nio.charset.StandardCharsets;

/**
 * Base64工具类 - 所需jar包：commons-codec.jar
 *
 * @author taozi - 2021年4月1日, 001 - 9:31:17
 */
public class Base64Utils {
    /**
     * base64编码
     *
     * @param str 待编码字符串
     * @return 编码字符串
     */
    public static String encode(String str) {
        return Base64.encodeBase64String(str.getBytes(StandardCharsets.UTF_8)).replaceAll("\r\n" , "");
    }

    /**
     *  * base64解码
     * <p>
     *  * @param base64Str 待解码字符串
     *  * @return 解码字符串
     *  
     */
    public static String decode(String base64Str) {
        return new String(Base64.decodeBase64(base64Str), StandardCharsets.UTF_8);
    }
}
