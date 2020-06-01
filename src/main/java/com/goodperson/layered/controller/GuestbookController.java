package com.goodperson.layered.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.goodperson.layered.dto.Guestbook;
import com.goodperson.layered.service.GuestbookService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GuestbookController {
    @Autowired
    private GuestbookService guestbookService;

    @GetMapping(path = "list")
    public String list(@RequestParam(required = false, defaultValue = "0") int start, 
                        Model model,
                        @CookieValue(name="firstVisit", defaultValue = "true",required = true) String firstVisit, 
                        HttpServletResponse response) 
    {
        List<Guestbook> list = guestbookService.getGuestbooks(start);
        int guestbookCount = guestbookService.getCount();
        List<Integer> pageStartList = guestbookService.getPageStartList(guestbookCount);

        getUserIsFirstVisitFromCookie(firstVisit, response, model);
        model.addAttribute("list", list);
        model.addAttribute("count", guestbookCount);
        model.addAttribute("pageStartList", pageStartList);
        return "list";
    }

    // 새로운 쿠키를 전송하는 함수
    private void sendNewCookie(String key, String value, HttpServletResponse response) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60 * 60 * 24 * 365); // 1년 유효기간(같은 브라우저에 한해)
        cookie.setPath("/"); // 경로 이하에 모두 쿠키 적용.
        response.addCookie(cookie);
    }

    // 사용자가 사이트에 처음 방문했는지, 아닌지를
    // 모델에 추가하고
    // 처음 방문했다면 value가 false인 쿠키를 새로 만들어 보낸다. 
    private void getUserIsFirstVisitFromCookie(String firstVisit, HttpServletResponse response, Model model) {
        final String key = "firstVisit";
        boolean isFirstVisit = Boolean.parseBoolean(firstVisit);
        model.addAttribute("firstVisit", isFirstVisit); // 모델에 처음 방문했는지 아닌지 확인
        if(isFirstVisit)
        {
            sendNewCookie(key, "false", response);
        }
    }

    @GetMapping(path = "list2")
    public String list2() {
        return "list2";
    }

    //방명록 작성 함수
    @PostMapping("/write")
    public String write(@ModelAttribute Guestbook guestbook, HttpServletRequest request) {
        String clientIp = request.getRemoteAddr();
        System.out.println("clientIp: " + clientIp);
        guestbookService.addGuestbook(guestbook, clientIp);
        return "redirect:list";
    }
}