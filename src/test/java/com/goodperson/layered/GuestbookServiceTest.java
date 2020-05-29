package com.goodperson.layered;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Date;

import com.goodperson.layered.dto.Guestbook;
import com.goodperson.layered.service.GuestbookService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class GuestbookServiceTest {
    @Autowired
    private GuestbookService gService;

    @Test
    public void addGuestbookTest(){
        Guestbook guestbook = new Guestbook();
        guestbook.setName("황충희");
        guestbook.setContent("반갑습니다. 여러분");
        guestbook.setRegdate(new Date());
        
        Guestbook result = gService.addGuestbook(guestbook, "127.0.0.1");
        assertNotNull(result);
    }

}