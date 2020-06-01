package com.goodperson.layered.service.impl;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import com.goodperson.layered.service.StateService;

import org.springframework.stereotype.Service;

@Service
public class StateServiceImpl implements StateService {

    // 새로운 쿠키를 전송하는 함수
    private void sendNewCookie(String key, String value, HttpServletResponse response) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60 * 60 * 24 * 365); // 1년 유효기간(같은 브라우저에 한해)
        cookie.setPath("/"); // 경로 이하에 모두 쿠키 적용.
        response.addCookie(cookie);
    }


     // 사용자가 사이트에 처음 방문했는지, 아닌지를 쿠키에서 넘어온 value값으로 판단한 뒤,
    // 처음 방문했다면 value가 false인 쿠키를 새로 만들어 보낸다. 
    @Override
    public boolean getUserIsFirstVisitFromCookieAndResendUpdatedCookie(String firstVisit, HttpServletResponse response) 
    {
        final String key = "firstVisit";
        boolean isFirstVisit = Boolean.parseBoolean(firstVisit);
        if(isFirstVisit)
        {
            sendNewCookie(key, "false", response);
        }
        return isFirstVisit;
    }
}