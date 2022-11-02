package com.oldlane.cheeseblog.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author: oldlane
 * @date: 2022/10/31 10:43
 */
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {
        "com.oldlane.cheeseblog.xo.service",
        "com.oldlane.cheeseblog.admin",
        "com.oldlane.cheeseblog.utils",
        "com.oldlane.cheeseblog.commons.config"
})
public class AdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
    }
}
