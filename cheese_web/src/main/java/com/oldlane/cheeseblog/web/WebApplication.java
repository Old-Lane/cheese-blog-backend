package com.oldlane.cheeseblog.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * Date: 2022/10/27 19:57
 * Description:
 */
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {
        "com.oldlane.cheeseblog.xo.service",
        "com.oldlane.cheeseblog.web",
        "com.oldlane.cheeseblog.utils",
        "com.oldlane.cheeseblog.commons.config"
})
public class WebApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }
}
