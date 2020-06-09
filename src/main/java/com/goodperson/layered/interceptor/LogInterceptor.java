package com.goodperson.layered.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Component
public class LogInterceptor extends HandlerInterceptorAdapter {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    // controller의 메소드가 실행된 후 호출
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        if (handler != null && modelAndView != null) {
            logger.debug("{} 종료. {}를 view로 사용.", handler.toString(), modelAndView.getViewName());
        }
    }

    // controller의 메소드가 실행되기 전 호출
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (handler != null) {
            logger.debug("{} 호출.", handler.toString());
        }
        return true;
    }

}