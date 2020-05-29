package com.goodperson.layered.dto;

import java.util.Date;


public class Guestbook{
    private long id;
    private String name;
    private String content;
    private Date regdate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getRegdate() {
        return regdate;
    }

    public void setRegdate(Date regdate) {
        this.regdate = regdate;
    }

    @Override
    public String toString() {
        return "Guestbook [content=" + content + ", id=" + id + ", name=" + name + ", regdate=" + regdate + "]";
    }
    
}