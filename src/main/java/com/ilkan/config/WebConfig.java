package com.ilkan.config;

import com.ilkan.auth.RoleCheckInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final RoleCheckInterceptor roleCheckInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(roleCheckInterceptor)
                .addPathPatterns("/api/**")   // 보호할 경로
                .excludePathPatterns("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html");
    }
}
