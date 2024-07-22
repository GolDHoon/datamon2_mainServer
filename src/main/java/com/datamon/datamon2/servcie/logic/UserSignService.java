package com.datamon.datamon2.servcie.logic;

import com.datamon.datamon2.common.CommonCodeCache;
import com.datamon.datamon2.dto.input.user.LoginInuptDto;
import com.datamon.datamon2.dto.repository.CompanyInfomationDto;
import com.datamon.datamon2.dto.repository.UserBaseDto;
import com.datamon.datamon2.servcie.repository.CompanyInfomationService;
import com.datamon.datamon2.servcie.repository.MemberInfomationService;
import com.datamon.datamon2.servcie.repository.UserBaseService;
import com.datamon.datamon2.util.*;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserSignService {
    private final CompanyInfomationService companyInfomationService;
    private final MemberInfomationService memberInfomationService;
    private UserBaseService userBaseService;
    private JwtUtil jwtUtil;

    public UserSignService(UserBaseService userBaseService, JwtUtil jwtUtil, CompanyInfomationService companyInfomationService, MemberInfomationService memberInfomationService) {
        this.userBaseService = userBaseService;
        this.jwtUtil = jwtUtil;
        this.companyInfomationService = companyInfomationService;
        this.memberInfomationService = memberInfomationService;
    }

    @Transactional
    public String userLogout(HttpServletRequest request) throws Exception{
        HttpSessionUtil httpSessionUtil = new HttpSessionUtil(request.getSession(false));
        httpSessionUtil.invalidate();
        return "success";
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String userLogin(LoginInuptDto loginInuptDto, HttpServletRequest request, HttpServletResponse response) throws Exception{
        JsonUtil jsonUtil = new JsonUtil();
        HttpSessionUtil httpSessionUtil = new HttpSessionUtil(request.getSession(true));
        IpUtil ipUtil = new IpUtil(request);

        List<String> companyCodes = CommonCodeCache.getCompanyCode().stream()
                .map(dto -> {
                    return dto.getCodeFullName();
                })
                .collect(Collectors.toList());

        UserBaseDto companyUser = userBaseService.getUserBaseByUserId(loginInuptDto.getCompanyId()).stream()
                .filter(dto-> companyCodes.contains(dto.getUserType()) || "USTY_MAST".equals(dto.getUserType()))
                .filter(UserBaseDto::getUseYn)
                .filter(dto -> !dto.getDelYn())
                .findFirst().orElse(new UserBaseDto());

        if(companyUser.getIdx() == null){
            return "login-fail:companyId";
        }

        UserBaseDto userBaseByUserId = userBaseService.getUserBaseByUserId(loginInuptDto.getUserId()).stream()
                .filter(UserBaseDto::getUseYn)
                .filter(dto -> !dto.getDelYn())
                .findFirst().orElse(new UserBaseDto());

        if(userBaseByUserId.getIdx() == null){
            return "login-fail:userId";
        }

        int companyId;

        if(companyCodes.contains(userBaseByUserId.getUserType()) || "USTY_MAST".equals(userBaseByUserId.getUserType())){
            companyId = userBaseByUserId.getIdx();
        }else{
            companyId = companyInfomationService.getCompanyInfomationById(memberInfomationService.getMemberInfomationByUserId(userBaseByUserId.getIdx()).getCompanyId()).getUserId();
        }

        if(companyUser.getIdx() != companyId){
            return "login-fail:companyId";
        }

        EncryptionUtil encryptionUtil = new EncryptionUtil();
        String encriptPw = encryptionUtil.getSHA256WithSalt(loginInuptDto.getPassword(), userBaseByUserId.getSalt());

        if(encriptPw.equals(userBaseByUserId.getUserPw())){
            List<Map<String, Object>> claimsList = new ArrayList<>();
            String token = jwtUtil.createToken(String.valueOf(userBaseByUserId.getIdx()), claimsList);

            httpSessionUtil.setAttribute("jwt", token);
            httpSessionUtil.setAttribute("loginIp", jsonUtil.toJsonStringByMap(ipUtil.getIp()));
            httpSessionUtil.setAttribute("companyId", String.valueOf(companyId));

            Cookie cookie = new Cookie("JSESSIONID", request.getSession(false).getId());
            response.addCookie(cookie);

            return request.getSession(false).getId();
        }else {
            return "login-fail:password";
        }
    }

    @Transactional
    public String getCompanyName(String companyId) throws Exception {
        List<String> companyCodes = CommonCodeCache.getCompanyCode().stream()
                .map(dto -> {
                    return dto.getCodeFullName();
                })
                .collect(Collectors.toList());

        UserBaseDto userBaseDto = userBaseService.getUserBaseByUserId(companyId).stream()
                .filter(UserBaseDto::getUseYn)
                .filter(dto -> !dto.getDelYn())
                .filter(dto -> companyCodes.contains(dto.getUserType()) || dto.getUserType().equals("USTY_MAST"))
                .findFirst().orElse(new UserBaseDto());

        if(userBaseDto.getIdx() == null){
            return "companyName-fail:companyId";
        }

        CompanyInfomationDto companyInfomationByUserId = companyInfomationService.getCompanyInfomationByUserId(userBaseDto.getIdx());
        return companyInfomationByUserId.getName();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
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
//                if(jwtUtil.validateToken(claims)){
//                    int userId = Integer.parseInt((String) claims.get("sub"));
//
//                    List<PageCodeDto> pageCode = CommonCodeCache.getPageCodes().stream()
//                            .filter(dto -> dto.getCodeValue().equals(request.getHeader("Path")))
//                            .collect(Collectors.toList());
//
//                    if(pageCode.size() == 0){
//                        return "auth-fail:page unregistered";
//                    }
//
//                    List<PagePermissionInfomationDto> pageAuth = pagePermissionInfomationService.getPagePermissionInfomationByUserId(userId).stream()
//                            .filter(dto -> dto.getPageCode().equals(pageCode.get(0).getCodeFullName()))
//                            .collect(Collectors.toList());
//
//                    if(pageAuth.size() == 0){
//                        return "auth-fail:page permission denied";
//                    }
//
//                    PaatCodeDto authX = CommonCodeCache.getPaatCodes().stream().filter(dto -> dto.getCodeValue().equals("X")).collect(Collectors.toList()).get(0);
//
//                    if(pageAuth.get(0).getPaatCode().equals(authX)){
//                        return "auth-fail:page permission denied";
//                    }
                    return "success";
//                }
            }
        }

        return "session-fail:time";
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sessionTimeReset(HttpServletRequest request) throws Exception{
        HttpSessionUtil httpSessionUtil = new HttpSessionUtil(request.getSession(false));
        httpSessionUtil.sessionTimeReset(30);

        String token = httpSessionUtil.getAttribute("jwt").toString();
        String userIdStr = (String) jwtUtil.getClaims(token).get("sub");
        int userId = Integer.parseInt(userIdStr);
        List<Map<String, Object>> claimsList = new ArrayList<>();

        String newToken = jwtUtil.createToken(userIdStr, claimsList);

        httpSessionUtil.setAttribute("jwt", newToken);
    }

//    @Transactional(propagation = Propagation.REQUIRES_NEW)
//    protected List<Map<String, Object>> setAuth(int userId){
//        List<Map<String, Object>> result = new ArrayList<>();
//
//        List<PagePermissionInfomationDto> pagePermissionInfomationByUserId = pagePermissionInfomationService.getPagePermissionInfomationByUserId(userId);
//
//        Map<String, Object> pageAuth = new HashMap<>();
//        List<Map<String, Object>> pageAuthList = new ArrayList<>();
//        pagePermissionInfomationByUserId.forEach(dto -> {
//            Map<String, Object> claim = new HashMap<>();
//            claim.put(dto.getPageCode(), dto.getPaatCode());
//            pageAuthList.add(claim);
//        });
//        pageAuth.put("key", "pageAuth");
//        pageAuth.put("value", pageAuthList);
//        result.add(pageAuth);
//
//        return result;
//    }
}
