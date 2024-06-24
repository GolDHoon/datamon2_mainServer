package com.datamon.datamon2.servcie.logic;

import com.datamon.datamon2.common.CommonCodeCache;
import com.datamon.datamon2.dto.repository.CompanyInfomationDto;
import com.datamon.datamon2.dto.repository.MemberInfomationDto;
import com.datamon.datamon2.dto.repository.UserBaseDto;
import com.datamon.datamon2.dto.repository.UstyCodeDto;
import com.datamon.datamon2.servcie.repository.CompanyInfomationService;
import com.datamon.datamon2.servcie.repository.MemberInfomationService;
import com.datamon.datamon2.servcie.repository.UserBaseService;
import com.datamon.datamon2.util.HttpSessionUtil;
import com.datamon.datamon2.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserService {
    private JwtUtil jwtUtil;
    private UserBaseService userBaseService;
    private CompanyInfomationService companyInfomationService;
    private MemberInfomationService memberInfomationService;


    @Transactional
    public List<Map<String, String>>  getListCompany(HttpServletRequest request) throws Exception{
        List<Map<String, String>> result = new ArrayList<>();
        HttpSessionUtil httpSessionUtil = new HttpSessionUtil(request.getSession());

        int userId = jwtUtil.getUserId(httpSessionUtil.getAttribute("jwt").toString());

        UserBaseDto userBaseById = userBaseService.getUserBaseById(userId);

        List<String> masterCodes = CommonCodeCache.getUstyCodes().stream()
                .filter(UstyCodeDto::getUseYn)
                .filter(dto -> !dto.getDelYn())
                .filter(dto -> dto.getCodeValue().equals("Masetr") || dto.getCodeValue().equals("InMember"))
                .map(UstyCodeDto::getCodeFullName)
                .collect(Collectors.toList());

        if (masterCodes.contains(userBaseById.getUserType())){
            throw new Exception("login-fail:userId");
        }

        List<String> userCode = CommonCodeCache.getUstyCodes().stream()
                .filter(UstyCodeDto::getUseYn)
                .filter(dto -> !dto.getDelYn())
                .filter(dto -> masterCodes.contains(dto.getCodeFullName()))
                .filter(dto -> !dto.getCodeValue().contains("Member"))
                .map(UstyCodeDto::getCodeFullName)
                .collect(Collectors.toList());

        userBaseService.getUserBaseByUserTypeList(userCode).forEach(dto->{
            Map<String, String> resultRow = new HashMap<>();
            resultRow.put("Id", dto.getUserId());

            CompanyInfomationDto companyInfomationById = companyInfomationService.getCompanyInfomationById(dto.getIdx());
            resultRow.put("companyName", companyInfomationById.getName());
            resultRow.put("ceo", companyInfomationById.getCeo());
            resultRow.put("corporateNumber", companyInfomationById.getCorporateNumber());
            resultRow.put("corporateMail", companyInfomationById.getCorporateMail());
            resultRow.put("corporateAddress", companyInfomationById.getCorporateAddress());
            resultRow.put("businessItem", companyInfomationById.getBusinessItem());
            resultRow.put("businessStatus", companyInfomationById.getBusinessStatus());

            result.add(resultRow);
        });

        return result;
    }

    @Transactional
    public List<Map<String, String>> getListUser(HttpServletRequest request) throws Exception{
        List<Map<String, String>> result = new ArrayList<>();
        HttpSessionUtil httpSessionUtil = new HttpSessionUtil(request.getSession());

        int userId = jwtUtil.getUserId(httpSessionUtil.getAttribute("jwt").toString());

        UserBaseDto userBaseById = userBaseService.getUserBaseById(userId);

        UstyCodeDto ustyCodeDto = CommonCodeCache.getUstyCodes().stream()
                .filter(UstyCodeDto::getUseYn)
                .filter(dto -> !dto.getDelYn())
                .filter(dto -> dto.getCodeFullName().equals(userBaseById.getUserType()))
                .findFirst().orElse(new UstyCodeDto());

        int companyId = 0;

        if(ustyCodeDto.getCodeValue().contains("Member")){
            MemberInfomationDto memberInfomationByUserId = memberInfomationService.getMemberInfomationByUserId(userId);
            companyId = memberInfomationByUserId.getCompanyId();
        }else{
            CompanyInfomationDto companyInfomationByUserId = companyInfomationService.getCompanyInfomationByUserId(userId);
            companyId = companyInfomationByUserId.getIdx();
        }

        memberInfomationService.getMemberInfomationDtoListByCompanyId(companyId).forEach(dto -> {
            Map<String, String> resultRow = new HashMap<>();
            resultRow.put("name", dto.getName());
            resultRow.put("department", dto.getRole());
            resultRow.put("phoneNumber", dto.getContactPhone());
            resultRow.put("mail", dto.getContactMail());
            result.add(resultRow);
        });

        return result;
    }
}
