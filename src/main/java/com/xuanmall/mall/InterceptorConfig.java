package com.xuanmall.mall;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
//拦截器配置
public class InterceptorConfig implements WebMvcConfigurer {
    //
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UserLoginInterceptor())/*拦截器注册*/
                .addPathPatterns("/**")
                .excludePathPatterns("/error","/user/login","user/register","/categories","/products","/products/*");
    }
}
