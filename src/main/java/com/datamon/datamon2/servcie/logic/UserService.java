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

import java.time.format.DateTimeFormatter;
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

    public UserService(JwtUtil jwtUtil, UserBaseService userBaseService, CompanyInfomationService companyInfomationService, MemberInfomationService memberInfomationService) {
        this.jwtUtil = jwtUtil;
        this.userBaseService = userBaseService;
        this.companyInfomationService = companyInfomationService;
        this.memberInfomationService = memberInfomationService;
    }

    @Transactional
    public Map<String, ?>  getListCompany(HttpServletRequest request) throws Exception{
        Map<String, Object> result = new HashMap<>();
        List<Map<String, String>> rows = new ArrayList<>();
        HttpSessionUtil httpSessionUtil = new HttpSessionUtil(request.getSession());

        int userId = jwtUtil.getUserId(httpSessionUtil.getAttribute("jwt").toString());

        UserBaseDto userBaseById = userBaseService.getUserBaseById(userId);

        List<String> masterCodes = CommonCodeCache.getUstyCodes().stream()
                .filter(UstyCodeDto::getUseYn)
                .filter(dto -> !dto.getDelYn())
                .filter(dto -> dto.getCodeValue().equals("Master") || dto.getCodeValue().equals("InMember"))
                .map(UstyCodeDto::getCodeFullName)
                .collect(Collectors.toList());

        if (masterCodes.contains(userBaseById.getUserType())){
            throw new Exception("login-fail:userId");
        }

        List<String> userCode = CommonCodeCache.getUstyCodes().stream()
                .filter(UstyCodeDto::getUseYn)
                .filter(dto -> !dto.getDelYn())
                .filter(dto -> !masterCodes.contains(dto.getCodeFullName()))
                .filter(dto -> !dto.getCodeValue().contains("Member"))
                .filter(dto -> !dto.getCodeValue().contains("Developer"))
                .map(UstyCodeDto::getCodeFullName)
                .collect(Collectors.toList());

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        userBaseService.getUserBaseByUserTypeList(userCode).forEach(dto->{
            Map<String, String> resultRow = new HashMap<>();
            resultRow.put("Id", dto.getUserId());
            resultRow.put("useIdx", String.valueOf(dto.getIdx()));
            resultRow.put("최종수정일시", dateTimeFormatter.format(dto.getModifyDate()));

            CompanyInfomationDto companyInfomationById = companyInfomationService.getCompanyInfomationByUserId(dto.getIdx());
            resultRow.put("상호", companyInfomationById.getName());
            resultRow.put("대표자명", companyInfomationById.getCeo());
            resultRow.put("사업자등록번호", companyInfomationById.getCorporateNumber());
            resultRow.put("이메일", companyInfomationById.getCorporateMail());
            resultRow.put("소재지", companyInfomationById.getCorporateAddress());
            resultRow.put("품목", companyInfomationById.getBusinessItem());
            resultRow.put("업태", companyInfomationById.getBusinessStatus());

            rows.add(resultRow);
        });

        List<String> keyList = new ArrayList<>();
        keyList.add("Id");
        keyList.add("상호");
        keyList.add("대표자명");
        keyList.add("사업자등록번호");
        keyList.add("이메일");
        keyList.add("소재지");
        keyList.add("품목");
        keyList.add("업태");
        keyList.add("최종수정일시");

        result.put("rows", (Object) rows);
        result.put("keyList", (Object) keyList);

        return result;
    }

    @Transactional
    public Map<String, ?> getListUser(HttpServletRequest request) throws Exception{
        Map<String, Object> result = new HashMap<>();
        List<Map<String, String>> rows = new ArrayList<>();
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

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        memberInfomationService.getMemberInfomationDtoListByCompanyId(companyId).forEach(dto -> {
            Map<String, String> resultRow = new HashMap<>();
            UserBaseDto userDto = userBaseService.getUserBaseById(dto.getUserId());

            resultRow.put("담당자명", dto.getName());
            resultRow.put("소속", dto.getRole());
            resultRow.put("연락처", dto.getContactPhone());
            resultRow.put("이메일", dto.getContactMail());

            resultRow.put("최종수정일시", dateTimeFormatter.format(userDto.getModifyDate()));
            resultRow.put("userIdx", String.valueOf(dto.getIdx()));
            rows.add(resultRow);
        });

        List<String> keyList = new ArrayList<>();
        keyList.add("담당자명");
        keyList.add("소속");
        keyList.add("연락처");
        keyList.add("이메일");

        result.put("rows", (Object) rows);
        result.put("keyList", (Object) keyList);

        return result;
    }
}
