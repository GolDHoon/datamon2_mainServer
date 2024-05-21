package com.datamon.datamon2.util;

import jakarta.servlet.http.HttpSession;

public class HttpSessionUtil {
    private HttpSession session;

    public HttpSessionUtil(HttpSession session) {
        this.session = session;
    }

    public void setSession(String key, String value){
        session.setAttribute(key, value);
    }

    public Object getSession(String key){
        return session.getAttribute(key);
    }
}
