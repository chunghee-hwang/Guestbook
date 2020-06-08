package com.goodperson.layered.config;

import java.util.List;

import com.goodperson.layered.argumentresolver.HeaderMapArgumentResolver;
import com.goodperson.layered.interceptor.LogInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    // 인터셉터 등록
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        System.out.println("인터셉터 등록합니다.");
        registry.addInterceptor(new LogInterceptor()).addPathPatterns("/**");
    }

    // 아규먼트 리졸버 등록
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        System.out.println("아규먼트 리졸버 등록합니다.");
        resolvers.add(new HeaderMapArgumentResolver());
    }

}