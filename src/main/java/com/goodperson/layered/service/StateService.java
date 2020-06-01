package com.goodperson.layered.service;

import javax.servlet.http.HttpServletResponse;
public interface StateService {
    public boolean getUserIsFirstVisitFromCookieAndResendUpdatedCookie(String firstVisit, HttpServletResponse response);
}