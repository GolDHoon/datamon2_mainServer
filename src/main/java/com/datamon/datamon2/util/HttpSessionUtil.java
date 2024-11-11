package com.datamon.datamon2.util;

import jakarta.servlet.http.HttpSession;

public class HttpSessionUtil {
    private HttpSession session;

    public HttpSessionUtil(HttpSession session) {
        this.session = session;
        session.setMaxInactiveInterval(86400);
    }

    public void setSession(HttpSession session){
        this.session = session;
    }

    public HttpSession getSession(){
        return this.session;
    }

    public void setAttribute(String key, String value){
        session.setAttribute(key, value);
    }

    public Object getAttribute(String key){
        return session.getAttribute(key);
    }

    public void sessionTimeReset(int minute){
        int inervalTime = minute * 60;
        session.setMaxInactiveInterval(inervalTime);
    }

    public void removeAttribute(String name) {
        session.removeAttribute(name);
    }

    public void invalidate() {
        session.invalidate();
    }
}
