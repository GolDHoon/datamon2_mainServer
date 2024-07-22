package com.datamon.datamon2.servcie.logic;

import com.datamon.datamon2.common.CommonCodeCache;
import com.datamon.datamon2.dto.input.user.*;
import com.datamon.datamon2.dto.repository.*;
import com.datamon.datamon2.servcie.repository.CompanyInfomationService;
import com.datamon.datamon2.servcie.repository.MemberInfomationService;
import com.datamon.datamon2.servcie.repository.UserBaseService;
import com.datamon.datamon2.servcie.repository.UserCdbtMappingService;
import com.datamon.datamon2.util.DateTimeUtil;
import com.datamon.datamon2.util.EncryptionUtil;
import com.datamon.datamon2.util.HttpSessionUtil;
import com.datamon.datamon2.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserService {
    private DateTimeUtil dateTimeUtil = new DateTimeUtil();
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

        if (!masterCodes.contains(userBaseById.getUserType())){
            throw new Exception("login-fail:userId");
        }

        List<String> companyCodes = CommonCodeCache.getCompanyCode().stream()
                .map(dto -> {
                    return dto.getCodeFullName();
                })
                .collect(Collectors.toList());

        userBaseService.getUserBaseByUserTypeList(companyCodes).stream()
                .filter(UserBaseDto::getUseYn)
                .filter(dto -> !dto.getDelYn())
                .sorted(Comparator.comparing(UserBaseDto::getModifyDate).reversed())
                .collect(Collectors.toList())
                .forEach(dto->{
            Map<String, String> resultRow = new HashMap<>();
            resultRow.put("ID", dto.getUserId());
            resultRow.put("idx", String.valueOf(dto.getIdx()));
            resultRow.put("최종수정일시", dateTimeUtil.LocalDateTimeToDateTimeStr(dto.getModifyDate()));

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

        if(memberCodes.contains(userBaseById.getUserType()) || "USTY_DEVL".equals(userBaseById.getUserType()) || "USTY_INME".equals(userBaseById.getUserType())){
            MemberInfomationDto memberInfomationByUserId = memberInfomationService.getMemberInfomationByUserId(userId);
            companyId = memberInfomationByUserId.getCompanyId();
        }else {
            CompanyInfomationDto companyInfomationByUserId = companyInfomationService.getCompanyInfomationByUserId(userId);
            companyId = companyInfomationByUserId.getIdx();
        }


        memberInfomationService.getMemberInfomationDtoListByCompanyId(companyId).forEach(dto -> {
            Map<String, String> resultRow = new HashMap<>();
            UserBaseDto userDto = userBaseService.getUserBaseById(dto.getUserId());

            if(userDto.getUseYn() && !userDto.getDelYn()){
                resultRow.put("ID", userDto.getUserId());
                resultRow.put("담당자명", dto.getName());
                resultRow.put("소속", dto.getRole());
                resultRow.put("연락처", dto.getContactPhone());
                resultRow.put("이메일", dto.getContactMail());

                resultRow.put("최종수정일시", dateTimeUtil.LocalDateTimeToDateTimeStr(userDto.getModifyDate()));
                resultRow.put("idx", String.valueOf(userDto.getIdx()));
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

        List<String> companyCodes = CommonCodeCache.getCompanyCode().stream()
                .map(dto -> {
                    return dto.getCodeFullName();
                })
                .collect(Collectors.toList());

        List<UserBaseDto> companyUserList = userBaseService.getUserBaseByUserId(createCompanyUserDto.getUserId()).stream()
                .filter(UserBaseDto::getUseYn)
                .filter(dto -> !dto.getDelYn())
                .filter(dto -> companyCodes.contains(dto.getUserType()))
                .collect(Collectors.toList());

        if(companyUserList.size() != 0){
            return "createCompany-fail:idDuplication";
        }

        EncryptionUtil encryptionUtil = new EncryptionUtil();

        UserBaseDto userBaseDto = new UserBaseDto();
        userBaseDto.setUserId(createCompanyUserDto.getUserId());
        String salt = encryptionUtil.getSalt();
        String encryptPw = encryptionUtil.getSHA256WithSalt(createCompanyUserDto.getPw(), salt);
        userBaseDto.setSalt(salt);
        userBaseDto.setUserPw(encryptPw);
        userBaseDto.setUserType(createCompanyUserDto.getUserType());
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

        UserBaseDto companyUser = userBaseService.getUserBaseByUserId(createMemberUserDto.getCompanyId()).stream()
                .filter(UserBaseDto::getUseYn)
                .filter(dto -> !dto.getDelYn())
                .findFirst().orElse(new UserBaseDto());
        CompanyInfomationDto companyInfo = companyInfomationService.getCompanyInfomationByUserId(companyUser.getIdx());

        List<MemberInfomationDto> memberInfomationDtoListByCompanyId = memberInfomationService.getMemberInfomationDtoListByCompanyId(companyInfo.getIdx());
        List<Integer> memberList = memberInfomationDtoListByCompanyId.stream()
                .map(dto -> {
                    return dto.getUserId();
                })
                .collect(Collectors.toList());

        List<UserBaseDto> userBaseByIdxList = userBaseService.getUserBaseByIdxList(memberList).stream()
                .filter(UserBaseDto::getUseYn)
                .filter(dto -> !dto.getDelYn())
                .filter(dto -> dto.getUserId().equals(createMemberUserDto.getUserId()))
                .collect(Collectors.toList());

        if(userBaseByIdxList.size() != 0){
            return "createMember-fail:idDuplication";
        }

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
            default:
                userBaseDto.setUserType(userBaseById.getUserType());
                break;
        }
        userBaseDto.create(userId);

        UserBaseDto save = userBaseService.save(userBaseDto);

        MemberInfomationDto memberInfomationDto = new MemberInfomationDto();
        memberInfomationDto.setCompanyId(companyInfo.getIdx());
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

    @Transactional
    public String checkMemberIdDuplicate(CheckIdDuplicateDto checkIdDuplicateDto) throws Exception{
        UserBaseDto companyUser = userBaseService.getUserBaseByUserId(checkIdDuplicateDto.getCompanyId()).stream()
                .filter(UserBaseDto::getUseYn)
                .filter(dto -> !dto.getDelYn())
                .findFirst().orElse(new UserBaseDto());

        if (companyUser.getIdx() == null){
            return "checkId-fail:sessionError";
        }

        CompanyInfomationDto companyInfomationByUserId = companyInfomationService.getCompanyInfomationByUserId(companyUser.getIdx());

        List<Integer> memberIdxList = memberInfomationService.getMemberInfomationDtoListByCompanyId(companyInfomationByUserId.getIdx()).stream()
                .map(dto -> {
                    return dto.getUserId();
                })
                .collect(Collectors.toList());

        List<UserBaseDto> userList = userBaseService.getUserBaseByIdxList(memberIdxList).stream()
                .filter(dto -> dto.getUserId().equals(checkIdDuplicateDto.getUserId()))
                .filter(UserBaseDto::getUseYn)
                .filter(dto -> !dto.getDelYn())
                .collect(Collectors.toList());

        if(userList.size() != 0){
            return "checkId-fail:idDuplication";
        }else{
            return "success";
        }
    }

    @Transactional
    public String checkCompanyIdDuplicate(CheckIdDuplicateDto checkIdDuplicateDto) throws Exception{
        List<String> companyCodes = CommonCodeCache.getCompanyCode().stream()
                .map(dto -> {
                    return dto.getCodeFullName();
                })
                .collect(Collectors.toList());

        List<UserBaseDto> companyUserList = userBaseService.getUserBaseByUserId(checkIdDuplicateDto.getUserId()).stream()
                .filter(UserBaseDto::getUseYn)
                .filter(dto -> !dto.getDelYn())
                .filter(dto -> companyCodes.contains(dto.getUserType()))
                .collect(Collectors.toList());

        if(companyUserList.size() != 0){
            return "checkId-fail:idDuplication";
        }else{
            return "success";
        }
    }
}
