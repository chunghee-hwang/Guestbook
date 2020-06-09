package com.goodperson.layered.config;

import java.util.List;

import com.goodperson.layered.argumentresolver.HeaderMapArgumentResolver;
import com.goodperson.layered.interceptor.LogInterceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    // 인터셉터 등록
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        logger.debug("인터셉터 등록합니다.");
        registry.addInterceptor(new LogInterceptor()).addPathPatterns("/**");
    }

    // 아규먼트 리졸버 등록
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        logger.debug("아규먼트 리졸버 등록합니다.");
        resolvers.add(new HeaderMapArgumentResolver());
    }

}