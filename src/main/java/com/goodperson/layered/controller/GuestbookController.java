package com.goodperson.layered.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.goodperson.layered.dto.Guestbook;
import com.goodperson.layered.service.GuestbookService;
import com.goodperson.layered.service.StateService;

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
    
    @Autowired
    private StateService stateService;

    @GetMapping(path = "list")
    public String list(@RequestParam(required = false, defaultValue = "0") int start, 
                        Model model,
                        @CookieValue(name="firstVisit", defaultValue = "true",required = true) String firstVisit, 
                        HttpServletResponse response) 
    {
        List<Guestbook> list = guestbookService.getGuestbooks(start);
        int guestbookCount = guestbookService.getCount();
        List<Integer> pageStartList = guestbookService.getPageStartList(guestbookCount);
        boolean isFirstVisit = stateService.getUserIsFirstVisitFromCookieAndResendUpdatedCookie(firstVisit, response);
        model.addAttribute("list", list);
        model.addAttribute("count", guestbookCount);
        model.addAttribute("pageStartList", pageStartList);
        model.addAttribute("firstVisit", isFirstVisit); // 모델에 처음 방문했는지 아닌지 확인
        return "list";
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