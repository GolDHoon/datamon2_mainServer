package com.datamon.datamon2.servcie.logic;

import com.datamon.datamon2.dto.repository.UserBaseDto;
import com.datamon.datamon2.servcie.repository.UserBaseService;
import com.datamon.datamon2.util.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserSignService {
    private UserBaseService userBaseService;
    private JwtUtil jwtUtil;

    public UserSignService(UserBaseService userBaseService, JwtUtil jwtUtil) {
        this.userBaseService = userBaseService;
        this.jwtUtil = jwtUtil;
    }

    public String userLogin(String userId, String password, HttpServletRequest request) throws Exception{
        JsonUtil jsonUtil = new JsonUtil();
        HttpSessionUtil httpSessionUtil = new HttpSessionUtil(request.getSession());
        IpUtil ipUtil = new IpUtil(request);

        UserBaseDto userBaseByUserId = userBaseService.getUserBaseByUserId(userId);
        if(userBaseByUserId.getIdx() == null){
            return "fail-userId";
        }

        EncryptionUtil encryptionUtil = new EncryptionUtil();
        String encriptPw = encryptionUtil.getSHA256WithSalt(password, userBaseByUserId.getSalt());

        if(encriptPw.equals(userBaseByUserId.getUserPw())){
            String token = jwtUtil.createToken(userId);

            httpSessionUtil.setSession("jwt", token);
            httpSessionUtil.setSession("loginIp", jsonUtil.toJsonStringByMap(ipUtil.getIp()));
            return request.getSession().getId();
        }else {
            return "fail-password";
        }
    }

    public String sessionCheck(HttpServletRequest request) throws Exception{
        HttpSessionUtil httpSessionUtil = new HttpSessionUtil(request.getSession());
        
        JsonUtil jsonUtil = new JsonUtil();
        IpUtil ipUtil = new IpUtil(request);

        Cookie[] cookies = request.getCookies();
        String sessionId = request.getSession().getId();
        HttpSession session = request.getSession();

        Object jwt = httpSessionUtil.getSession("jwt");
        if(jwt == null){
            return "fail-token";
        }

        String token = jwt.toString();

        Map loginIp = jsonUtil.toMapByJsonString(httpSessionUtil.getSession("loginIp").toString());
        Map<String, String> ip = ipUtil.getIp();

        if(ip.get("ExtractingMethod").equals(loginIp.get("ExtractingMethod"))){
            if(ip.get("ip").equals(loginIp.get("ip"))){
                if(jwtUtil.validateToken(jwtUtil.getClaims(token))){
                    return "success";
                }
            }
        }

        return "fail-time";
    }
}
