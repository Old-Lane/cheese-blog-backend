package com.oldlane.cheeseblog.creator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author: oldlane
 * @date: 2022/11/2 16:04
 */
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {
        "com.oldlane.cheeseblog.xo.service",
        "com.oldlane.cheeseblog.creator",
        "com.oldlane.cheeseblog.utils",
        "com.oldlane.cheeseblog.commons.config"
})
public class CreatorApplication {
    public static void main(String[] args) {
        SpringApplication.run(CreatorApplication.class, args);
    }
}
