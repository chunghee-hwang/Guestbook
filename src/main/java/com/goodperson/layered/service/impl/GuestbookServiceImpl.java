package com.goodperson.layered.service.impl;

import java.util.Date;
import java.util.List;

import com.goodperson.layered.dao.GuestbookDao;
import com.goodperson.layered.dao.LogDao;
import com.goodperson.layered.dto.Guestbook;
import com.goodperson.layered.dto.Log;
import com.goodperson.layered.service.GuestbookService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GuestbookServiceImpl implements GuestbookService {

    @Autowired
    GuestbookDao guestbookDao;

    @Autowired
    LogDao logDao;

    @Override
    @Transactional(readOnly=false)
    public Guestbook addGuestbook(Guestbook guestbook, String ip) {
        guestbook.setRegdate(new Date());
        long insertedId = guestbookDao.insert(guestbook);
        guestbook.setId(insertedId);
        addLog(ip, "insert");
        return guestbook;
    }

    @Override
    @Transactional(readOnly=false)
    public int deleteGuestbook(long id, String ip) {
        int deleteCount = guestbookDao.deleteById(id);
        addLog(ip, "delete");
        return deleteCount;
    }

    @Override
    public int getCount() {
        return guestbookDao.selectCount();
    }

    @Override
    public List<Guestbook> getGuestbooks(int start) {
        return guestbookDao.selectAll(start, LIMIT);
    }
    
    private void addLog(String ip, String method){
        Log log = new Log();
        log.setIp(ip);
        log.setMethod(method);
        log.setRegdate(new Date());
        logDao.insert(log);
    }
}