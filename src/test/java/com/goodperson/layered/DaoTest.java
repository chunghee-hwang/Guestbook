package com.goodperson.layered;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;

import com.goodperson.layered.dao.GuestbookDao;
import com.goodperson.layered.dao.LogDao;
import com.goodperson.layered.dto.Guestbook;
import com.goodperson.layered.dto.Log;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DaoTest {
    @Autowired
    private GuestbookDao guestbookDao;

    @Autowired
    private LogDao logDao;

    @Test
    public void guestbookInsertTest() {
        Guestbook guestbook = new Guestbook();
        guestbook.setName("황충희");
        guestbook.setContent("반갑습니다. 여러분.");
        guestbook.setRegdate(new Date());

        long id = guestbookDao.insert(guestbook);
        assertTrue(id > 0);
    }

    @Test
    public void logInsertTest() {
        Log log = new Log();
        log.setIp("127.0.0.1");
        log.setMethod("insert");
        log.setRegdate(new Date());
        long id = logDao.insert(log);

        assertTrue(id > 0);
    }

}