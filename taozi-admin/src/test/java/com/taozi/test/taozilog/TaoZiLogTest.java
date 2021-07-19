package com.taozi.test.taozilog;

import com.taozi.common.utils.log.TaoZiLog;
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
        TaoZiLog.debug("debug");
    }

    @Test
    public void b() {
        TaoZiLog.info("info");
    }

    @Test
    public void c() {
        TaoZiLog.warn("warn");
        TaoZiLog.warn("warn");
        TaoZiLog.warn("warn");
    }

    @Test
    public void d() {
        TaoZiLog.error("error");
    }

}
