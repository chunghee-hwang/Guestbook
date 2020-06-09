package com.goodperson.layered.config;

import java.util.List;

import com.goodperson.layered.argumentresolver.HeaderMapArgumentResolver;
import com.goodperson.layered.interceptor.LogInterceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
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

    // 파일 업로드를 위한 멀티파트 리졸버 등록
    @Bean
    public MultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(10485760); // 1024 * 1024 * 10(10MB)
        return multipartResolver;
    }
}