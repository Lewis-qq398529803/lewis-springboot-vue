package com.lewis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 启动程序
 *
 * @author Lewis
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class LewisApplication {
    public static void main(String[] args) {
        SpringApplication.run(LewisApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  Lewis系统启动成功   ლ(´ڡ`ლ)ﾞ\n");
    }
}
