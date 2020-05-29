package com.dadalong.autotest.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 转路由
 */
@Configuration
public class RouterUtils implements WebMvcConfigurer {

    @Value("${html-path}")
    String htmlPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/htmlReports/**").addResourceLocations( "file:" + htmlPath);
    }
}
