package com.taozi.common.utils.log;

/**
 * 处理并记录日志文件
 * 
 * @author taozi
 */
public class LogUtils
{
    public static String getBlock(Object msg)
    {
        if (msg == null)
        {
            msg = "";
        }
        return "[" + msg.toString() + "]";
    }
}
