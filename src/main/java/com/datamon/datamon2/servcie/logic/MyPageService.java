package com.datamon.datamon2.servcie.logic;

import com.datamon.datamon2.common.CommonCodeCache;
import com.datamon.datamon2.dto.input.myPage.ModifyCompanyDto;
import com.datamon.datamon2.dto.input.myPage.ModifyMemberDto;
import com.datamon.datamon2.dto.repository.CompanyInfomationDto;
import com.datamon.datamon2.dto.repository.MemberInfomationDto;
import com.datamon.datamon2.dto.repository.UserBaseDto;
import com.datamon.datamon2.dto.repository.UstyCodeDto;
import com.datamon.datamon2.servcie.repository.CompanyInfomationService;
import com.datamon.datamon2.servcie.repository.MemberInfomationService;
import com.datamon.datamon2.servcie.repository.UserBaseService;
import com.datamon.datamon2.util.DateTimeUtil;
import com.datamon.datamon2.util.EncryptionUtil;
import com.datamon.datamon2.util.HttpSessionUtil;
import com.datamon.datamon2.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MyPageService {
    private DateTimeUtil dateTimeUtil = new DateTimeUtil();
    private JwtUtil jwtUtil;
    private UserBaseService userBaseService;
    private CompanyInfomationService companyInfomationService;
    private MemberInfomationService memberInfomationService;

    public MyPageService(JwtUtil jwtUtil, UserBaseService userBaseService, CompanyInfomationService companyInfomationService, MemberInfomationService memberInfomationService) {
        this.jwtUtil = jwtUtil;
        this.userBaseService = userBaseService;
        this.companyInfomationService = companyInfomationService;
        this.memberInfomationService = memberInfomationService;
    }

    @Transactional
    public Map<String, String> getInfo(HttpServletRequest request) throws Exception {
        Map<String, String> result = new HashMap<>();
        HttpSessionUtil httpSessionUtil = new HttpSessionUtil(request.getSession(false));

        int userId = jwtUtil.getUserId(httpSessionUtil.getAttribute("jwt").toString());

        UserBaseDto userBaseById = userBaseService.getUserBaseById(userId);

        result.put("id", userBaseById.getUserId());
        result.put("userType", userBaseById.getUserType());
        result.put("modifyDate", dateTimeUtil.LocalDateTimeToDateTimeStr(userBaseById.getModifyDate()));

        List<String> memberCodes = CommonCodeCache.getMemberCodes().stream()
                .map(dto -> {
                    return dto.getCodeFullName();
                })
                .collect(Collectors.toList());
        memberCodes.add("USTY_DEVL");
        memberCodes.add("USTY_INME");

        if(memberCodes.contains(userBaseById.getUserType())){
            MemberInfomationDto memberInfo = memberInfomationService.getMemberInfomationByUserId(userId);
            CompanyInfomationDto companyInfo = new CompanyInfomationDto();
            if(memberInfo.getCompanyId() != null){
                companyInfo = companyInfomationService.getCompanyInfomationById(memberInfo.getCompanyId());
            }
            result.put("companyName", companyInfo.getName());
            result.put("name", memberInfo.getName());
            result.put("role", memberInfo.getRole());
            result.put("phoneNumber", memberInfo.getContactPhone());
            result.put("mail", memberInfo.getContactMail());
            result.put("userKind", "member");
        }else{
            CompanyInfomationDto companyInfo = companyInfomationService.getCompanyInfomationByUserId(userId);
            result.put("companyName", companyInfo.getName());
            result.put("ceo", companyInfo.getCeo());
            result.put("corporateNumber", companyInfo.getCorporateNumber());
            result.put("corporateMail", companyInfo.getCorporateMail());
            result.put("corporateAddress", companyInfo.getCorporateAddress());
            result.put("businessItem", companyInfo.getBusinessItem());
            result.put("businessStatus", companyInfo.getBusinessStatus());
            result.put("userKind", "company");
        }

        return result;
    }

    @Transactional
    public String modifyMember(HttpServletRequest request, ModifyMemberDto modifyMemberDto) throws Exception{
        Map<String, String> result = new HashMap<>();
        HttpSessionUtil httpSessionUtil = new HttpSessionUtil(request.getSession(false));

        int userId = jwtUtil.getUserId(httpSessionUtil.getAttribute("jwt").toString());

        MemberInfomationDto memberInfo = memberInfomationService.getMemberInfomationByUserId(userId);
        memberInfo.setName(modifyMemberDto.getName());
        memberInfo.setRole(modifyMemberDto.getRole());
        memberInfo.setContactPhone(modifyMemberDto.getPhoneNumber());
        memberInfo.setContactMail(modifyMemberDto.getMail());

        UserBaseDto userBaseById = userBaseService.getUserBaseById(userId);
        userBaseById.modify(userId);

        userBaseService.save(userBaseById);
        memberInfomationService.save(memberInfo);

        return "success";
    }

    @Transactional
    public String modifyCompany(HttpServletRequest request, ModifyCompanyDto modifyCompanyDto) throws Exception{
        Map<String, String> result = new HashMap<>();
        HttpSessionUtil httpSessionUtil = new HttpSessionUtil(request.getSession(false));

        int userId = jwtUtil.getUserId(httpSessionUtil.getAttribute("jwt").toString());

        CompanyInfomationDto companyInfo = companyInfomationService.getCompanyInfomationByUserId(userId);
        companyInfo.setName(modifyCompanyDto.getCompanyName());
        companyInfo.setCeo(modifyCompanyDto.getCeo());

        UserBaseDto userBaseById = userBaseService.getUserBaseById(userId);
        userBaseById.modify(userId);

        userBaseService.save(userBaseById);
        companyInfomationService.save(companyInfo);

        return "success";
    }

    @Transactional
    public String setPassword(HttpServletRequest request, String passWord) throws Exception{
        EncryptionUtil encryptionUtil = new EncryptionUtil();
        Map<String, String> result = new HashMap<>();
        HttpSessionUtil httpSessionUtil = new HttpSessionUtil(request.getSession(false));

        int userId = jwtUtil.getUserId(httpSessionUtil.getAttribute("jwt").toString());

        UserBaseDto userBaseById = userBaseService.getUserBaseById(userId);

        String salt = encryptionUtil.getSalt();
        String encryptPw = encryptionUtil.getSHA256WithSalt(passWord, salt);

        userBaseById.setUserPw(encryptPw);
        userBaseById.setSalt(salt);
        userBaseById.modify(userId);
        userBaseService.save(userBaseById);

        return "success";
    }
}
