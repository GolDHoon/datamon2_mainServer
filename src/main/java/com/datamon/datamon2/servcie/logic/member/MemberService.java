package com.datamon.datamon2.servcie.logic.member;

import com.datamon.datamon2.common.CommonCodeCache;
import com.datamon.datamon2.dto.input.member.MemberAccountDto;
import com.datamon.datamon2.dto.input.member.CheckIdDuplicateDto;
import com.datamon.datamon2.dto.input.member.CreateMemberUserDto;
import com.datamon.datamon2.dto.input.member.DeleteMemberUserDto;
import com.datamon.datamon2.dto.repository.AccountApprovalRequestDto;
import com.datamon.datamon2.dto.repository.CompanyInfomationDto;
import com.datamon.datamon2.dto.repository.MemberInfomationDto;
import com.datamon.datamon2.dto.repository.UserBaseDto;
import com.datamon.datamon2.servcie.repository.AccountApprovalRequestService;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MemberService {
    private UserBaseService userBaseService;
    private MemberInfomationService memberInfomationService;
    private CompanyInfomationService companyInfomationService;
    private AccountApprovalRequestService accountApprovalRequestService;
    private JwtUtil jwtUtil;
    private DateTimeUtil dateTimeUtil = new DateTimeUtil();

    public MemberService(UserBaseService userBaseService, MemberInfomationService memberInfomationService, CompanyInfomationService companyInfomationService, AccountApprovalRequestService accountApprovalRequestService, JwtUtil jwtUtil) {
        this.userBaseService = userBaseService;
        this.memberInfomationService = memberInfomationService;
        this.companyInfomationService = companyInfomationService;
        this.accountApprovalRequestService = accountApprovalRequestService;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public String requestMemberAccount(MemberAccountDto memberAccountDto, HttpServletRequest request) throws Exception{
        UserBaseDto userBaseDto = new UserBaseDto();
        EncryptionUtil encryptionUtil = new EncryptionUtil();
        String salt = encryptionUtil.getSalt();

        CompanyInfomationDto companyInfomationDto =companyInfomationService.getCompanyInfomationById(memberAccountDto.getCompanyId());
        UserBaseDto companyUser = userBaseService.getUserBaseById(companyInfomationDto.getUserId());

        userBaseDto.setUserId(memberAccountDto.getUserId());
        userBaseDto.setUserPw(encryptionUtil.getSHA256WithSalt(memberAccountDto.getUserPw(), salt));
        userBaseDto.setSalt(salt);

        switch (companyUser.getUserType()){
            case "USTY_MAST": userBaseDto.setUserType("USTY_INME"); break;
            case "USTY_CLNT": userBaseDto.setUserType("USTY_CLME"); break;
            case "USTY_ADAC": userBaseDto.setUserType("USTY_AAME"); break;
            case "USTY_CRAC": userBaseDto.setUserType("USTY_CAME"); break;
            default: break;
        }

        userBaseDto.setUserStatus("ACST_PEND");
        userBaseDto.create(CommonCodeCache.getSystemIdIdx());

        UserBaseDto saveUser = userBaseService.save(userBaseDto);

        MemberInfomationDto memberInfomationDto = new MemberInfomationDto();
        memberInfomationDto.setCompanyId(memberAccountDto.getCompanyId());
        memberInfomationDto.setUserId(saveUser.getIdx());
        memberInfomationDto.setRole(memberAccountDto.getRole());
        memberInfomationDto.setContactMail(memberAccountDto.getContactMail());
        memberInfomationDto.setContactPhone(memberAccountDto.getContactPhone());
        memberInfomationService.save(memberInfomationDto);

        AccountApprovalRequestDto accountApprovalRequestDto = new AccountApprovalRequestDto();
        accountApprovalRequestDto.setRequestReason(memberAccountDto.getRequestReason());
        accountApprovalRequestDto.setRequestType("C");
        accountApprovalRequestDto.setUserId(saveUser.getIdx());
        accountApprovalRequestDto.setCompletionYn(false);

        accountApprovalRequestService.save(accountApprovalRequestDto);

        return "success";
    }

    @Transactional
    public String requestSendVerificationSms() throws Exception{
        return "success";
    }

    @Transactional
    public String requestSendVerificationMail() throws Exception{
        return "success";
    }

    @Transactional
    public String confirmSmsVerification() throws Exception{
        return "success";
    }

    @Transactional
    public String confirmMailVerification() throws Exception{
        return "success";
    }

    @Transactional
    public String createUser(HttpServletRequest request, CreateMemberUserDto createMemberUserDto) throws Exception{
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
        userBaseDto.setUserStatus("ACST_ACTV");
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
    public String checkIdDuplicate(CheckIdDuplicateDto checkIdDuplicateDto) throws Exception{
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
}
