package com.goodperson.layered.service;

import java.util.List;

import com.goodperson.layered.dto.Guestbook;

public interface GuestbookService {
    public static final int LIMIT = 5;
    public List<Guestbook> getGuestbooks(int start);
    public int deleteGuestbook(long id, String ip);
    public Guestbook addGuestbook(Guestbook guestbook, String ip);
    public int getCount();
	public List<Integer> getPageStartList(int guestbookCount);
}