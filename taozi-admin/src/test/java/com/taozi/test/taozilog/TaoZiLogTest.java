package com.taozi.test.taozilog;

import com.taozi.common.utils.log.BaseLog;
import org.junit.Test;

/**
 * 全局日志类测试
 *
 * @author taozi - 2021年7月16日, 016 - 16:17:43
 */
//@RunWith(SpringRunner.class)
//@SpringBootTest
public class TaoZiLogTest {

    @Test
    public void a() {
        BaseLog.debug("debug");
    }

    @Test
    public void b() {
        BaseLog.info("info");
    }

    @Test
    public void c() {
        BaseLog.warn("warn");
        BaseLog.warn("warn");
        BaseLog.warn("warn");
    }

    @Test
    public void d() {
        BaseLog.error("error");
    }

}
