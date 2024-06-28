package com.datamon.datamon2.servcie.logic;

import com.datamon.datamon2.common.CommonCodeCache;
import com.datamon.datamon2.dto.input.user.*;
import com.datamon.datamon2.dto.repository.*;
import com.datamon.datamon2.servcie.repository.CompanyInfomationService;
import com.datamon.datamon2.servcie.repository.MemberInfomationService;
import com.datamon.datamon2.servcie.repository.UserBaseService;
import com.datamon.datamon2.servcie.repository.UserCdbtMappingService;
import com.datamon.datamon2.util.EncryptionUtil;
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
        HttpSessionUtil httpSessionUtil = new HttpSessionUtil(request.getSession(false));

        int userId = jwtUtil.getUserId(httpSessionUtil.getAttribute("jwt").toString());

        UserBaseDto userBaseById = userBaseService.getUserBaseById(userId);

        List<String> masterCodes = CommonCodeCache.getMasterCodes().stream()
                .map(dto -> {
                    return dto.getCodeFullName();
                })
                .collect(Collectors.toList());

        if (masterCodes.contains(userBaseById.getUserType())){
            throw new Exception("login-fail:userId");
        }

        List<String> companyCodes = CommonCodeCache.getCompanyCode().stream()
                .map(dto -> {
                    return dto.getCodeFullName();
                })
                .collect(Collectors.toList());

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        userBaseService.getUserBaseByUserTypeList(companyCodes).stream()
                .filter(UserBaseDto::getUseYn)
                .filter(dto -> !dto.getDelYn())
                .collect(Collectors.toList())
                .forEach(dto->{
            Map<String, String> resultRow = new HashMap<>();
            resultRow.put("ID", dto.getUserId());
            resultRow.put("userIdx", String.valueOf(dto.getIdx()));
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
        keyList.add("ID");
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
        HttpSessionUtil httpSessionUtil = new HttpSessionUtil(request.getSession(false));

        List<String> memberCodes = CommonCodeCache.getMemberCodes().stream()
                .map(dto -> {
                    return dto.getCodeFullName();
                })
                .collect(Collectors.toList());

        int userId = jwtUtil.getUserId(httpSessionUtil.getAttribute("jwt").toString());

        UserBaseDto userBaseById = userBaseService.getUserBaseById(userId);

        int companyId = 0;

        if(memberCodes.contains(userBaseById.getUserType())){
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

            if(userDto.getUseYn() && !userDto.getDelYn()){
                resultRow.put("ID", userDto.getUserId());
                resultRow.put("담당자명", dto.getName());
                resultRow.put("소속", dto.getRole());
                resultRow.put("연락처", dto.getContactPhone());
                resultRow.put("이메일", dto.getContactMail());

                resultRow.put("최종수정일시", dateTimeFormatter.format(userDto.getModifyDate()));
                resultRow.put("userIdx", String.valueOf(userDto.getIdx()));
                rows.add(resultRow);
            }
        });

        List<String> keyList = new ArrayList<>();
        keyList.add("ID");
        keyList.add("담당자명");
        keyList.add("소속");
        keyList.add("연락처");
        keyList.add("이메일");

        result.put("rows", (Object) rows);
        result.put("keyList", (Object) keyList);

        return result;
    }

    @Transactional
    public String createCompanyUser (HttpServletRequest request, CreateCompanyUserDto createCompanyUserDto) throws Exception{
        HttpSessionUtil httpSessionUtil = new HttpSessionUtil(request.getSession(false));

        int userId = jwtUtil.getUserId(httpSessionUtil.getAttribute("jwt").toString());

        EncryptionUtil encryptionUtil = new EncryptionUtil();

        UserBaseDto userBaseDto = new UserBaseDto();
        userBaseDto.setUserId(createCompanyUserDto.getUserId());
        String salt = encryptionUtil.getSalt();
        String encryptPw = encryptionUtil.getSHA256WithSalt(createCompanyUserDto.getPw(), salt);
        userBaseDto.setSalt(salt);
        userBaseDto.setUserPw(encryptPw);
        userBaseDto.setUserType("USTY_CLNT");
        userBaseDto.create(userId);

        UserBaseDto save = userBaseService.save(userBaseDto);

        CompanyInfomationDto companyInfomationDto = new CompanyInfomationDto();
        companyInfomationDto.setUserId(save.getIdx());
        companyInfomationDto.setCeo(createCompanyUserDto.getCeo());
        companyInfomationDto.setName(createCompanyUserDto.getName());
        companyInfomationDto.setBusinessItem(createCompanyUserDto.getBusinessItem());
        companyInfomationDto.setBusinessStatus(createCompanyUserDto.getBusinessStatus());
        companyInfomationDto.setCorporateNumber(createCompanyUserDto.getCorporateNumber());
        companyInfomationDto.setCorporateAddress(createCompanyUserDto.getCorporateAddress());
        companyInfomationDto.setCorporateMail(createCompanyUserDto.getCorporateMail());
        companyInfomationService.save(companyInfomationDto);

        return "success";
    }

    @Transactional
    public String deleteCompanyUser(HttpServletRequest request, DeleteCompanyUserDto deleteCompanyUserDto) throws Exception{
        HttpSessionUtil httpSessionUtil = new HttpSessionUtil(request.getSession(false));

        int userId = jwtUtil.getUserId(httpSessionUtil.getAttribute("jwt").toString());

        UserBaseDto userBaseById = userBaseService.getUserBaseById(deleteCompanyUserDto.getIdx());
        userBaseById.setDelYn(true);
        userBaseById.delete(userId);

        userBaseService.save(userBaseById);

        return "success";
    }

    @Transactional
    public String createMemberUser(HttpServletRequest request, CreateMemberUserDto createMemberUserDto) throws Exception{
        HttpSessionUtil httpSessionUtil = new HttpSessionUtil(request.getSession(false));

        int userId = jwtUtil.getUserId(httpSessionUtil.getAttribute("jwt").toString());

        EncryptionUtil encryptionUtil = new EncryptionUtil();

        UserBaseDto userBaseById = userBaseService.getUserBaseById(userId);


        UserBaseDto userBaseDto = new UserBaseDto();
        userBaseDto.setUserId(createMemberUserDto.getUserId());
        String salt = encryptionUtil.getSalt();
        String encryptPw = encryptionUtil.getSHA256WithSalt(createMemberUserDto.getPw(), salt);
        userBaseDto.setSalt(salt);
        userBaseDto.setUserPw(encryptPw);
        switch (userBaseById.getUserType()){
            case "USTY_MAST":
            userBaseDto.setUserType("USTY_INME");
            break;
            case "USTY_CLNT":
            userBaseDto.setUserType("USTY_CLME");
            break;
            case "USTY_ADAC":
            userBaseDto.setUserType("USTY_AAME");
            break;
            case "USTY_CRAC":
            userBaseDto.setUserType("USTY_CAME");
            break;
        }
        userBaseDto.create(userId);

        UserBaseDto save = userBaseService.save(userBaseDto);
        CompanyInfomationDto companyInfomationByUserId = companyInfomationService.getCompanyInfomationByUserId(userId);

        MemberInfomationDto memberInfomationDto = new MemberInfomationDto();
        memberInfomationDto.setCompanyId(companyInfomationByUserId.getIdx());
        memberInfomationDto.setUserId(save.getIdx());
        memberInfomationDto.setName(createMemberUserDto.getName());
        memberInfomationDto.setRole(createMemberUserDto.getRole());
        memberInfomationDto.setContactMail(createMemberUserDto.getMail());
        memberInfomationDto.setContactPhone(createMemberUserDto.getContactPhone());

        memberInfomationService.save(memberInfomationDto);

        return "success";
    }

    @Transactional
    public String deleteMemberUser(HttpServletRequest request, DeleteMemberUserDto deleteMemberUserDto) throws Exception{
        HttpSessionUtil httpSessionUtil = new HttpSessionUtil(request.getSession(false));

        int userId = jwtUtil.getUserId(httpSessionUtil.getAttribute("jwt").toString());

        UserBaseDto userBaseById = userBaseService.getUserBaseById(deleteMemberUserDto.getIdx());
        userBaseById.setDelYn(true);
        userBaseById.delete(userId);

        userBaseService.save(userBaseById);

        return "success";
    }
}
