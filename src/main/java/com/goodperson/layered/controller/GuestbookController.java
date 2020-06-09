package com.goodperson.layered.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.goodperson.layered.argumentresolver.HeaderInfo;
import com.goodperson.layered.dto.Guestbook;
import com.goodperson.layered.service.GuestbookService;
import com.goodperson.layered.service.StateService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class GuestbookController {
    @Autowired
    private GuestbookService guestbookService;

    @Autowired
    private StateService stateService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping(path = "list")
    public String list(@RequestParam(required = false, defaultValue = "0") int start, Model model,
            @CookieValue(name = "firstVisit", defaultValue = "true", required = true) String firstVisit,
            HttpServletResponse response, HeaderInfo headerInfo/* 아규먼트 리졸버로 등록한 게 이걸 처리 넘겨준다 */
    ) {
        List<Guestbook> list = guestbookService.getGuestbooks(start);
        int guestbookCount = guestbookService.getCount();
        List<Integer> pageStartList = guestbookService.getPageStartList(guestbookCount);
        boolean isFirstVisit = stateService.getUserIsFirstVisitFromCookieAndResendUpdatedCookie(firstVisit, response);
        logger.debug("\n-----------user-agent-------------\n{}\n---------------------------------",
                headerInfo.get("user-agent"));
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

    // 방명록 작성 함수
    @PostMapping("/write")
    public String write(@ModelAttribute Guestbook guestbook, HttpServletRequest request) {
        if (guestbook == null)
            return "redirect:list";
        String clientIp = request.getRemoteAddr();
        logger.debug("clientIp: {}", clientIp);
        guestbookService.addGuestbook(guestbook, clientIp);
        return "redirect:list";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam(required = false) Long id, @SessionAttribute(required = false) String isAdmin,
            HttpServletRequest request, RedirectAttributes redirectAttr) {
        if (id == null) {
            return "redirect:list";
        }
        if (isAdmin == null || !"true".equals(isAdmin)) {
            redirectAttr.addFlashAttribute("errorMessage", "로그인을 하지 않았습니다.");
            return "redirect:loginform";
        }
        String clientIp = request.getRemoteAddr();
        logger.debug("clientIp: {}", clientIp);
        guestbookService.deleteGuestbook(id, clientIp);
        return "redirect:list";
    }
}