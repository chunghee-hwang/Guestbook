package com.goodperson.layered.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Component
public class LogInterceptor extends HandlerInterceptorAdapter {
    // controller의 메소드가 실행된 후 호출
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        if (handler != null && modelAndView != null) {
            System.out.println(handler.toString() + " 가 종료되었습니다." + modelAndView.getViewName() + "을 view로 사용합니다.");
        }
    }

    // controller의 메소드가 실행되기 전 호출
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (handler != null) {
            System.out.println(handler.toString() + " 를 호출했습니다.");
        }
        return true;
    }

}