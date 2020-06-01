package com.goodperson.layered.controller;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.goodperson.layered.dto.Guestbook;
import com.goodperson.layered.service.GuestbookService;
import com.goodperson.layered.service.StateService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/guestbooks")
public class GuestbookApiController {
    @Autowired
    private GuestbookService guestbookService;

    @Autowired
    private StateService stateService;

    @GetMapping
    public Map<String, Object> getGuestbookList(@RequestParam(required = false, defaultValue = "0") int start, 
    @CookieValue(name="firstVisit", defaultValue = "true",required = true) String firstVisit, 
    HttpServletResponse response) {
        List<Guestbook> list = guestbookService.getGuestbooks(start);
        int guestbookCount = guestbookService.getCount();
        List<Integer> pageStartList = guestbookService.getPageStartList(guestbookCount);
        boolean isFirstVisit = stateService.getUserIsFirstVisitFromCookieAndResendUpdatedCookie(firstVisit, response);

        Map<String, Object> map = new HashMap<>();
        map.put("list", list);
        map.put("count", guestbookCount);
        map.put("pageStartList", pageStartList);
        map.put("firstVisit", isFirstVisit);
        return map;
    }

    @PostMapping
    public Guestbook writeGuestbook(@RequestBody Guestbook guestbook, HttpServletRequest request){
        String clientIp = request.getRemoteAddr();
        Guestbook resultGuestbook = guestbookService.addGuestbook(guestbook, clientIp);
        return resultGuestbook;
    }

    @DeleteMapping("/{id}")
    public Map<String, Boolean> delete(@PathVariable long id, HttpServletRequest request){
        String clientIp = request.getRemoteAddr();
        int deleteCount = guestbookService.deleteGuestbook(id, clientIp);
        return Collections.singletonMap("success", deleteCount > 0);
    }
}