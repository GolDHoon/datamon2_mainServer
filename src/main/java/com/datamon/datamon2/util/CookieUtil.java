package com.datamon.datamon2.util;

import jakarta.servlet.http.Cookie;

public class CookieUtil {
    private Cookie[] cookies;

    public CookieUtil(Cookie[] cookies) {
        this.cookies = cookies;
    }

    public Cookie[] getCookies(){
        return this.cookies;
    }

    public void setCookies(Cookie[] cookies){
        this.cookies = cookies;
    }

    public Cookie getCookieByKey(String key) {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(key)) {
                    return cookie;
                }
            }
        }
        return null;
    }

    public void addCookie(Cookie cookie){
        Cookie[] newCookies = new Cookie[cookies.length + 1];
        System.arraycopy(cookies, 0, newCookies, 0, cookies.length);
        newCookies[cookies.length] = cookie;
        cookies = newCookies;
    }
}
