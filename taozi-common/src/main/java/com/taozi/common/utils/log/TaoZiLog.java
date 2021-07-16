package com.taozi.common.utils.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志工具类
 *
 * @author taozi - 2021年7月16日, 016 - 16:07:26
 */
public class TaoZiLog {

    private static Logger logger = null;

    private static int row = 0;

    static {
        StackTraceElement[] stackTraceElements = new Throwable().getStackTrace();
        for (StackTraceElement stackTraceElement : stackTraceElements) {
            if (!TaoZiLog.class.getName().equals(stackTraceElement.getClassName())) {
                logger = LoggerFactory.getLogger(stackTraceElement.getClassName());
                row = stackTraceElement.getLineNumber();
            }
        }
    }

    public static void debug(String msg) {
        logger.debug("第" + row + "行 - " + msg);
    }

    public static void info(String msg) {
        logger.info("第" + row + "行 - " + msg);
    }

    public static void warn(String msg) {
        logger.warn("第" + row + "行 - " + msg);
    }

    public static void error(String msg) {
        logger.error("第" + row + "行 - " + msg);
    }

}
