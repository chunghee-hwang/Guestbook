package com.goodperson.layered.service.impl;

import java.util.ArrayList;
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


    // 페이징을 위한 인덱스 가져오기. ex) /list?start=0, /list?start=5 즉, 0, 5 리스트를 가져옴
    @Override
    public List<Integer> getPageStartList(int guestbookCount){
        int pageCount = guestbookCount / GuestbookService.LIMIT;
        if (guestbookCount % LIMIT > 0)
            pageCount++;
        List<Integer> pageStartList = new ArrayList<>();
        for (int i = 0; i < pageCount; i++) {
            pageStartList.add(i * GuestbookService.LIMIT);
        }
        return pageStartList;
    }
    
}