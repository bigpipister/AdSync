package com.cht.directory.connector.filter.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 將 /status/** 的請求映射到 classpath:/static/ 目錄
        registry.addResourceHandler("/status/**")
                .addResourceLocations("classpath:/static/");

        // 如果要映射到外部目錄，可以使用 file: 前綴
        // registry.addResourceHandler("/status/**")
        //         .addResourceLocations("file:/path/to/your/static/folder/");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 允許所有路徑
                .allowedOrigins("http://localhost:3000") // 允許的來源
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 允許的 HTTP 方法
                .allowedHeaders("*") // 允許的 Headers
                .allowCredentials(true); // 允許攜帶 Cookie
    }
}
