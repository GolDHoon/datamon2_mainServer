package com.datamon.datamon2.servcie.logic;

import com.datamon.datamon2.common.CommonCodeCache;
import com.datamon.datamon2.dto.repository.PaatCodeDto;
import com.datamon.datamon2.dto.repository.PageCodeDto;
import com.datamon.datamon2.dto.repository.PagePermissionInfomationDto;
import com.datamon.datamon2.dto.repository.UserBaseDto;
import com.datamon.datamon2.servcie.repository.PageCodeService;
import com.datamon.datamon2.servcie.repository.PagePermissionInfomationService;
import com.datamon.datamon2.servcie.repository.UserBaseService;
import com.datamon.datamon2.util.*;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserSignService {
    private UserBaseService userBaseService;
    private PagePermissionInfomationService pagePermissionInfomationService;
    private JwtUtil jwtUtil;

    public UserSignService(UserBaseService userBaseService, PagePermissionInfomationService pagePermissionInfomationService, JwtUtil jwtUtil) {
        this.userBaseService = userBaseService;
        this.pagePermissionInfomationService = pagePermissionInfomationService;
        this.jwtUtil = jwtUtil;
    }

    public String userLogin(String userId, String password, HttpServletRequest request, HttpServletResponse response) throws Exception{
        JsonUtil jsonUtil = new JsonUtil();
        HttpSessionUtil httpSessionUtil = new HttpSessionUtil(request.getSession(true));
        IpUtil ipUtil = new IpUtil(request);

        UserBaseDto userBaseByUserId = userBaseService.getUserBaseByUserId(userId);
        if(userBaseByUserId.getIdx() == null){
            return "login-fail:userId";
        }

        EncryptionUtil encryptionUtil = new EncryptionUtil();
        String encriptPw = encryptionUtil.getSHA256WithSalt(password, userBaseByUserId.getSalt());

        if(encriptPw.equals(userBaseByUserId.getUserPw())){
            List<Map<String, Object>> claimsList = setAuth(userBaseByUserId.getIdx());
            String token = jwtUtil.createToken(String.valueOf(userBaseByUserId.getIdx()), claimsList);

            httpSessionUtil.setAttribute("jwt", token);
            httpSessionUtil.setAttribute("loginIp", jsonUtil.toJsonStringByMap(ipUtil.getIp()));

            Cookie cookie = new Cookie("JSESSIONID", request.getSession(false).getId());
            response.addCookie(cookie);

            return request.getSession(false).getId();
        }else {
            return "login-fail:password";
        }
    }

    public String sessionCheck(HttpServletRequest request) throws Exception{
        HttpSessionUtil httpSessionUtil = new HttpSessionUtil(request.getSession(false));

        JsonUtil jsonUtil = new JsonUtil();
        IpUtil ipUtil = new IpUtil(request);


        Object jwt = httpSessionUtil.getAttribute("jwt");
        if(jwt == null){
            return "session-fail:token";
        }

        String token = jwt.toString();

        Map loginIp = jsonUtil.toMapByJsonString(httpSessionUtil.getAttribute("loginIp").toString());
        Map<String, String> ip = ipUtil.getIp();

        if(ip.get("ExtractingMethod").equals(loginIp.get("ExtractingMethod"))){
            Claims claims = jwtUtil.getClaims(token);
            if(ip.get("ip").equals(loginIp.get("ip"))){
                if(jwtUtil.validateToken(claims)){
                    int userId = Integer.parseInt((String) claims.get("sub"));

                    List<PageCodeDto> pageCode = CommonCodeCache.getPageCodes().stream()
                            .filter(dto -> dto.getCodeValue().equals(request.getHeader("Path")))
                            .collect(Collectors.toList());

                    if(pageCode.size() == 0){
                        return "auth-fail:page unregistered";
                    }

                    List<PagePermissionInfomationDto> pageAuth = pagePermissionInfomationService.getPagePermissionInfomationByUserId(userId).stream()
                            .filter(dto -> dto.getPageCode().equals(pageCode.get(0).getCodeFullName()))
                            .collect(Collectors.toList());

                    if(pageAuth.size() == 0){
                        return "auth-fail:page permission denied";
                    }

                    PaatCodeDto authX = CommonCodeCache.getPaatCodes().stream().filter(dto -> dto.getCodeValue().equals("X")).collect(Collectors.toList()).get(0);

                    if(pageAuth.get(0).getPaatCode().equals(authX)){
                        return "auth-fail:page permission denied";
                    }

                    return "success";
                }
            }
        }

        return "session-fail:time";
    }

    public void sessionTimeReset(HttpServletRequest request) throws Exception{
        HttpSessionUtil httpSessionUtil = new HttpSessionUtil(request.getSession(false));
        httpSessionUtil.sessionTimeReset(30);

        String token = httpSessionUtil.getAttribute("jwt").toString();
        String userIdStr = (String) jwtUtil.getClaims(token).get("sub");
        int userId = Integer.parseInt(userIdStr);
        List<Map<String, Object>> claimsList = setAuth(userId);

        String newToken = jwtUtil.createToken(userIdStr, claimsList);

        httpSessionUtil.setAttribute("jwt", token);
    }

    private List<Map<String, Object>> setAuth(int userId){
        List<Map<String, Object>> result = new ArrayList<>();

        List<PagePermissionInfomationDto> pagePermissionInfomationByUserId = pagePermissionInfomationService.getPagePermissionInfomationByUserId(userId);

        Map<String, Object> pageAuth = new HashMap<>();
        List<Map<String, Object>> pageAuthList = new ArrayList<>();
        pagePermissionInfomationByUserId.forEach(dto -> {
            Map<String, Object> claim = new HashMap<>();
            claim.put(dto.getPageCode(), dto.getPaatCode());
            pageAuthList.add(claim);
        });
        pageAuth.put("key", "pageAuth");
        pageAuth.put("value", pageAuthList);
        result.add(pageAuth);

        return result;
    }
}
