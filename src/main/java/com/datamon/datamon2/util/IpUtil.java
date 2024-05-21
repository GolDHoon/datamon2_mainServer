package com.datamon.datamon2.util;

import jakarta.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.Map;

public class IpUtil {
    private HttpServletRequest request;

    public IpUtil(HttpServletRequest request) {
        this.request = request;
    }

    public Map<String, String> getIp(){
        HashMap<String, String> ipMap = new HashMap<String, String>();
        String ip = request.getHeader("X-Forwarded-For");

        if(ip == null){
            ip = request.getRemoteAddr();
            ipMap.put("ExtractingMethod", "getRemoteAddr");
        }else{
            ipMap.put("ExtractingMethod", "X-Forwarded-For");
        }
        ipMap.put("ip", ip);

        return ipMap;
    }
}
