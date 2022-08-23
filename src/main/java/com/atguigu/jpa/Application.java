package com.atguigu.jpa;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    private static final Log log = LogFactory.getLog(Application.class);
    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
        log.info("spring boot data jpa");

    }
}