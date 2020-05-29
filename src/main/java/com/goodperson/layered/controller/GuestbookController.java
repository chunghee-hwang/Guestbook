package com.goodperson.layered.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.goodperson.layered.dto.Guestbook;
import com.goodperson.layered.service.GuestbookService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GuestbookController {
    @Autowired
    private GuestbookService guestbookService;

    @GetMapping(path = "list")
    public String list(@RequestParam(required = false, defaultValue = "0") int start, Model model) {
        List<Guestbook> list = guestbookService.getGuestbooks(start);

        int count = guestbookService.getCount();
        int pageCount = count / GuestbookService.LIMIT;
        if(count % GuestbookService.LIMIT > 0) pageCount++;

        List<Integer> pageStartList = new ArrayList<>();
        for(int i = 0; i < pageCount; i++){
            pageStartList.add(i*GuestbookService.LIMIT);
        }
        model.addAttribute("list", list);
        model.addAttribute("count", count);
        model.addAttribute("pageStartList", pageStartList);
        return "list";
    }

    @PostMapping("/write")
    public String write(@ModelAttribute Guestbook guestbook, 
    HttpServletRequest request){
        String clientIp = request.getRemoteAddr();
        System.out.println("clientIp: " + clientIp);
        guestbookService.addGuestbook(guestbook, clientIp);
        return "redirect:list";
    }
}