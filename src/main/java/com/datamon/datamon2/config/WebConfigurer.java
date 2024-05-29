package com.datamon.datamon2.config;

import com.datamon.datamon2.interceptor.PermissionCheckInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration		// 자동 호출되어 설정됨
public class WebConfigurer implements WebMvcConfigurer {

    @Autowired
    private PermissionCheckInterceptor permissionCheckInterceptor;

    @Override
    public void addCorsMappings(CorsRegistry registry) {

        // 모든 client 접속 허가
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000"
                                , "https://datamon2.xyz")
                .allowedOriginPatterns("*")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(permissionCheckInterceptor)
                .addPathPatterns("/sessionCheck");
    }
}