package com.datamon.datamon2.servcie.logic;

import com.datamon.datamon2.dto.repository.UserBaseDto;
import com.datamon.datamon2.servcie.repository.UserBaseService;
import com.datamon.datamon2.util.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

    public String userLogin(String userId, String password, HttpServletRequest request, HttpServletResponse response) throws Exception{
        JsonUtil jsonUtil = new JsonUtil();
        HttpSessionUtil httpSessionUtil = new HttpSessionUtil(request.getSession(true));
        IpUtil ipUtil = new IpUtil(request);

        UserBaseDto userBaseByUserId = userBaseService.getUserBaseByUserId(userId);
        if(userBaseByUserId.getIdx() == null){
            return "fail-userId";
        }

        EncryptionUtil encryptionUtil = new EncryptionUtil();
        String encriptPw = encryptionUtil.getSHA256WithSalt(password, userBaseByUserId.getSalt());

        if(encriptPw.equals(userBaseByUserId.getUserPw())){
            String token = jwtUtil.createToken(userId);

            httpSessionUtil.setAttribute("jwt", token);
            httpSessionUtil.setAttribute("loginIp", jsonUtil.toJsonStringByMap(ipUtil.getIp()));

            Cookie cookie = new Cookie("JSESSIONID", request.getSession(false).getId());
            response.addCookie(cookie);

            return request.getSession(false).getId();
        }else {
            return "fail-password";
        }
    }

    public String sessionCheck(HttpServletRequest request) throws Exception{
        Cookie[] cookies = request.getCookies();
        String sessionId = request.getSession().getId();
        HttpSession session = request.getSession(false);
        String httpSessionId = session.getId();

        HttpSessionUtil httpSessionUtil = new HttpSessionUtil(request.getSession(false));

        JsonUtil jsonUtil = new JsonUtil();
        IpUtil ipUtil = new IpUtil(request);


        Object jwt = httpSessionUtil.getAttribute("jwt");
        if(jwt == null){
            return "fail-token";
        }

        String token = jwt.toString();

        Map loginIp = jsonUtil.toMapByJsonString(httpSessionUtil.getAttribute("loginIp").toString());
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
