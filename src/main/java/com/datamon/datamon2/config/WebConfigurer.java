package com.datamon.datamon2.config;

import com.datamon.datamon2.common.CommonCodeCache;
import com.datamon.datamon2.dto.repository.LpgeCodeDto;
import com.datamon.datamon2.interceptor.PermissionCheckInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
import java.util.stream.Collectors;

@Configuration		// 자동 호출되어 설정됨
public class WebConfigurer implements WebMvcConfigurer {

    @Autowired
    private PermissionCheckInterceptor permissionCheckInterceptor;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        List<String> domainList = CommonCodeCache.getLpgeCodes().stream()
                .filter(LpgeCodeDto::getUseYn)
                .filter(dto -> !dto.getDelYn())
                .map(LpgeCodeDto::getCodeValue)
                .toList();
        // 모든 client 접속 허가
        registry.addMapping("/**")
//                .allowedOrigins("http://localhost:3000"
//                                , "http://localhost:63344/"
//                                , "https://datamon2.xyz")
                .allowedOrigins("-")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);

        registry.addMapping("/landingPage/**")
                .allowedOrigins(domainList.toArray(new String[0]))
                .allowedOriginPatterns("*")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true);

        registry.addMapping("/developer/checkServer")
                .allowedOrigins("*")
                .allowedOriginPatterns("*")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true);

        registry.addMapping("/swagger-ui/**")
                .allowedOrigins("-")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
        registry.addMapping("/v3/api-docs/**")
                .allowedOrigins("-")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(permissionCheckInterceptor)
                .addPathPatterns("/sessionCheck");
    }
}