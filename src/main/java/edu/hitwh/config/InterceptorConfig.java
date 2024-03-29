package edu.hitwh.config;

import edu.hitwh.interceptor.LoginCheckInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/*
* 本类用于配置拦截器
* */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Autowired
    private LoginCheckInterceptor loginCheckInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginCheckInterceptor)
                .addPathPatterns("/**").excludePathPatterns(
                        "/login/**",
                        "/enroll",
                        "/swagger**/**",
                        "/webjars/**",
                        "/v3/**",
                        "/doc.html");
    }
}
