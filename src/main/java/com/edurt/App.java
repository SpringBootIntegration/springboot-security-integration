package com.edurt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Hello world!
 */
@SpringBootApplication
// 设置扫描路径
@ComponentScan(value = "com.edurt")
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

}