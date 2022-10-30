package com.oldlane.cheeseblog.commons.config.mybatis;

import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: oldlane
 * @date: 2022/10/31 1:05
 */
@Configuration
@MapperScan("com.oldlane.cheeseblog.xo.mapper*")
public class MybatisPlusConfig {
    /**
     * mybatis-plus分页插件
     * 文档：http://mp.baomidou.com
     */
    @Bean
    public PaginationInnerInterceptor paginationInterceptor() {
        PaginationInnerInterceptor paginationInterceptor = new PaginationInnerInterceptor();
        return paginationInterceptor;
    }
}
