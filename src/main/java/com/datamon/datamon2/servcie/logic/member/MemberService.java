package com.datamon.datamon2.servcie.logic.member;

import com.datamon.datamon2.common.CommonCodeCache;
import com.datamon.datamon2.dto.input.member.MemberAccountRequestProcessingDto;
import com.datamon.datamon2.dto.input.member.MemberAccountDto;
import com.datamon.datamon2.dto.input.member.CheckIdDuplicateDto;
import com.datamon.datamon2.dto.input.member.MemberUserInfoDto;
import com.datamon.datamon2.dto.output.common.ColumnInfo;
import com.datamon.datamon2.dto.output.common.ErrorOutputDto;
import com.datamon.datamon2.dto.output.common.SuccessOutputDto;
import com.datamon.datamon2.dto.output.member.GetMemberListOutputDto;
import com.datamon.datamon2.dto.output.member.GetMemberOutputDto;
import com.datamon.datamon2.dto.output.member.GetRequestMemberAccountListOutputDto;
import com.datamon.datamon2.dto.output.member.VerificationInfo;
import com.datamon.datamon2.dto.repository.*;
import com.datamon.datamon2.servcie.repository.*;
import com.datamon.datamon2.util.DateTimeUtil;
import com.datamon.datamon2.util.EncryptionUtil;
import com.datamon.datamon2.util.HttpSessionUtil;
import com.datamon.datamon2.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class MemberService {
    private UserBaseService userBaseService;
    private MemberInfomationService memberInfomationService;
    private CompanyInfomationService companyInfomationService;
    private AccountApprovalRequestService accountApprovalRequestService;
    private SmsVerificationService smsVerificationService;
    private EmailVerificationService emailVerificationService;
    private JwtUtil jwtUtil;

    private RestTemplate restTemplate = new RestTemplate();

    public MemberService(UserBaseService userBaseService, MemberInfomationService memberInfomationService, CompanyInfomationService companyInfomationService, AccountApprovalRequestService accountApprovalRequestService, SmsVerificationService smsVerificationService, EmailVerificationService emailVerificationService, JwtUtil jwtUtil) {
        this.userBaseService = userBaseService;
        this.memberInfomationService = memberInfomationService;
        this.companyInfomationService = companyInfomationService;
        this.accountApprovalRequestService = accountApprovalRequestService;
        this.smsVerificationService = smsVerificationService;
        this.emailVerificationService = emailVerificationService;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public Map<String, Object> getMember(HttpServletRequest request) throws Exception{
        HttpSessionUtil httpSessionUtil = new HttpSessionUtil(request.getSession(false));
        GetMemberOutputDto getMemberOutputDto = new GetMemberOutputDto();
        ErrorOutputDto errorOutputDto = new ErrorOutputDto();
        Map<String, Object> result = new HashMap<>();
        result.put("result", "E");

        int userId = jwtUtil.getUserId(httpSessionUtil.getAttribute("jwt").toString());

        UserBaseDto userBaseDto = userBaseService.getUserBaseById(userId);
        MemberInfomationDto memberInfomationDto = memberInfomationService.getMemberInfomationByUserId(userId);

        getMemberOutputDto.setUserId(userBaseDto.getUserId());
        getMemberOutputDto.setName(memberInfomationDto.getName());
        getMemberOutputDto.setRole(memberInfomationDto.getRole());
        getMemberOutputDto.setContactPhone(memberInfomationDto.getContactPhone());
        getMemberOutputDto.setContactMail(memberInfomationDto.getContactMail());

        result.put("result", "S");
        result.put("output", getMemberOutputDto);
        return result;
    }

    @Transactional
    public Map<String, Object> requestMemberAccount(MemberAccountDto memberAccountDto, HttpServletRequest request) throws Exception{
        SuccessOutputDto successOutputDto = new SuccessOutputDto();
        ErrorOutputDto errorOutputDto = new ErrorOutputDto();
        Map<String, Object> result = new HashMap<>();
        result.put("result", "E");

        EncryptionUtil encryptionUtil = new EncryptionUtil();
        String salt = encryptionUtil.getSalt();

        CompanyInfomationDto companyInfomationDto = companyInfomationService.getCompanyInfomationById(memberAccountDto.getCompanyId());
        UserBaseDto companyUser = userBaseService.getUserBaseById(companyInfomationDto.getUserId());

        UserBaseDto userBaseDto = null;
        if(memberAccountDto.getRequestType().equals("C")){
            userBaseDto = new UserBaseDto();
        }else{
            HttpSessionUtil httpSessionUtil = new HttpSessionUtil(request.getSession(false));
            int userId = jwtUtil.getUserId(httpSessionUtil.getAttribute("jwt").toString());
            userBaseDto = userBaseService.getUserBaseById(userId);
        }

        userBaseDto.setUserStatus("ACST_PEND");
        if(memberAccountDto.getRequestType().equals("C")){
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

            userBaseDto.create(CommonCodeCache.getSystemIdIdx());
        }else {
            userBaseDto.modify(userBaseDto.getIdx());
        }

        UserBaseDto saveUser = userBaseService.save(userBaseDto);

        MemberInfomationDto memberInfomationDto = null;
        if(memberAccountDto.getRequestType().equals("C")){
            memberInfomationDto = new MemberInfomationDto();
        }else{
            memberInfomationDto = memberInfomationService.getMemberInfomationByUserId(saveUser.getIdx());
        }
        memberInfomationDto.setCompanyId(memberAccountDto.getCompanyId());
        memberInfomationDto.setUserId(saveUser.getIdx());
        memberInfomationDto.setName(memberAccountDto.getName());
        memberInfomationDto.setRole(memberAccountDto.getRole());
        memberInfomationDto.setContactMail(memberAccountDto.getContactMail());
        memberInfomationDto.setContactPhone(memberAccountDto.getContactPhone());
        memberInfomationService.save(memberInfomationDto);

        AccountApprovalRequestDto accountApprovalRequestDto = new AccountApprovalRequestDto();
        accountApprovalRequestDto.setRequestReason(memberAccountDto.getRequestReason());
        accountApprovalRequestDto.setRequestType(memberAccountDto.getRequestType());
        accountApprovalRequestDto.setUserId(saveUser.getIdx());
        accountApprovalRequestDto.setCompletionYn(false);
        accountApprovalRequestDto.createIdx();
        if(memberAccountDto.getRequestType().equals("C")){
            accountApprovalRequestDto.create(CommonCodeCache.getSystemIdIdx());
        }else{
            accountApprovalRequestDto.create(saveUser.getIdx());
        }

        accountApprovalRequestService.save(accountApprovalRequestDto);


        successOutputDto.setCode(200);
        if(memberAccountDto.getRequestType().equals("C")){
            successOutputDto.setMessage("계정신청이 완료되었습니다.");
        }else{
            successOutputDto.setMessage("계정수정신청이 완료되었습니다.");
        }
        result.put("result", "S");
        result.put("output", successOutputDto);
        return result;
    }

    @Transactional
    public Map<String, Object> checkIdDuplicate(CheckIdDuplicateDto checkIdDuplicateDto) throws Exception{
        SuccessOutputDto successOutputDto = new SuccessOutputDto();
        ErrorOutputDto errorOutputDto = new ErrorOutputDto();
        Map<String, Object> result = new HashMap<>();
        result.put("result", "E");

        CompanyInfomationDto companyInfo = companyInfomationService.getCompanyInfomationById(checkIdDuplicateDto.getCompanyId());

        List<Integer> memberIdxList = memberInfomationService.getMemberInfomationDtoListByCompanyId(checkIdDuplicateDto.getCompanyId()).stream()
                .map(dto-> {return dto.getUserId();})
                .collect(Collectors.toList());

        List<UserBaseDto> userList = userBaseService.getUserBaseByIdxList(memberIdxList).stream()
                .filter(UserBaseDto::getUseYn)
                .filter(user -> !user.getDelYn())
                .filter(user -> user.getUserId().equals(checkIdDuplicateDto.getUserId()))
                .collect(Collectors.toList());

        if(userList.size() != 0){
            errorOutputDto.setCode(404);
            errorOutputDto.setDetailReason("계정이 중복입니다.");
            result.put("output", errorOutputDto);
            return result;
        }else{
            successOutputDto.setCode(200);
            successOutputDto.setMessage("사용가능한 계정입니다.");
            result.put("result", "S");
            result.put("output", successOutputDto);
            return result;
        }
    }

    @Transactional
    public Map<String, Object> getMemberList(HttpServletRequest request) throws Exception{
        Map<String, Object> result = new HashMap<>();
        HttpSessionUtil httpSessionUtil = new HttpSessionUtil(request.getSession(false));
        GetMemberListOutputDto getMemberListOutputDto = new GetMemberListOutputDto();
        ErrorOutputDto errorOutputDto = new ErrorOutputDto();
        getMemberListOutputDto.setColumnInfoList(new ArrayList<>());
        getMemberListOutputDto.setDataList(new ArrayList<>());
        result.put("result", "E");

        int companyId = Integer.parseInt(httpSessionUtil.getAttribute("companyId").toString());

        ColumnInfo columnInfo = new ColumnInfo();

        columnInfo.setColumnType("basic");
        columnInfo.setFilterType("text");
        columnInfo.setName("계정");
        columnInfo.setKey("userId");
        getMemberListOutputDto.getColumnInfoList().add(columnInfo);

        columnInfo = new ColumnInfo();
        columnInfo.setColumnType("basic");
        columnInfo.setFilterType("text");
        columnInfo.setName("담당자 명");
        columnInfo.setKey("name");
        getMemberListOutputDto.getColumnInfoList().add(columnInfo);

        columnInfo = new ColumnInfo();
        columnInfo.setColumnType("basic");
        columnInfo.setFilterType("text");
        columnInfo.setName("역할");
        columnInfo.setKey("role");
        getMemberListOutputDto.getColumnInfoList().add(columnInfo);

        columnInfo = new ColumnInfo();
        columnInfo.setColumnType("basic");
        columnInfo.setFilterType("text");
        columnInfo.setName("연락처");
        columnInfo.setKey("contactPhone");
        getMemberListOutputDto.getColumnInfoList().add(columnInfo);

        columnInfo = new ColumnInfo();
        columnInfo.setColumnType("basic");
        columnInfo.setFilterType("text");
        columnInfo.setName("이메일");
        columnInfo.setKey("contactMail");
        getMemberListOutputDto.getColumnInfoList().add(columnInfo);

        columnInfo = new ColumnInfo();
        columnInfo.setColumnType("basic");
        columnInfo.setFilterType("date");
        columnInfo.setName("생성일");
        columnInfo.setKey("createDate");
        getMemberListOutputDto.getColumnInfoList().add(columnInfo);

        List<MemberInfomationDto> memberInfoList =  memberInfomationService.getMemberInfomationDtoListByCompanyId(companyId);

        List<UserBaseDto> userList = userBaseService.getUserBaseByIdxList(memberInfoList.stream()
                .map(memberInfo -> {return memberInfo.getUserId();})
                .collect(Collectors.toList())).stream()
                .filter(UserBaseDto::getUseYn)
                .filter(user -> !user.getDelYn())
                .filter(user -> user.getUserStatus().equals("ACST_ACTV"))
                .collect(Collectors.toList());

        userList.sort(Comparator.comparing(UserBaseDto::getCreateDate).reversed());

        userList.forEach(user -> {
            Map<String, Object> row = new HashMap<>();

            MemberInfomationDto memberInfo = memberInfoList.stream()
                    .filter(member -> Objects.equals(member.getUserId(), user.getIdx()))
                    .findFirst().orElse(new MemberInfomationDto());

            row.put("idx", user.getIdx());
            row.put("userId", user.getUserId());
            row.put("createDate", user.getCreateDate());
            row.put("role", memberInfo.getRole());
            row.put("name", memberInfo.getName());
            row.put("contactPhone", memberInfo.getContactPhone());
            row.put("contactMail", memberInfo.getContactMail());

            getMemberListOutputDto.getDataList().add(row);
        });

        result.put("result", "S");
        result.put("output", getMemberListOutputDto);

        return result;
    }

    @Transactional
    public Map<String, Object> getRequestMemberAccountList(HttpServletRequest request) throws Exception {
        Map<String, Object> result = new HashMap<>();
        GetRequestMemberAccountListOutputDto getRequestMemberAccountListOutputDto = new GetRequestMemberAccountListOutputDto();
        getRequestMemberAccountListOutputDto.setColumnInfoList(new ArrayList<>());
        getRequestMemberAccountListOutputDto.setDataList(new ArrayList<>());
        HttpSessionUtil httpSessionUtil = new HttpSessionUtil(request.getSession(false));
        result.put("result", "E");

        int companyId = Integer.parseInt(httpSessionUtil.getAttribute("companyId").toString());

        List<MemberInfomationDto> memberInfoList = memberInfomationService.getMemberInfomationDtoListByCompanyId(companyId);

        List<UserBaseDto> userList = userBaseService.getUserBaseByIdxList(memberInfoList.stream()
                .map(member -> {return member.getUserId();})
                .collect(Collectors.toList())).stream()
                .filter(UserBaseDto::getUseYn)
                .filter(user -> !user.getDelYn())
                .collect(Collectors.toList());

        List<AccountApprovalRequestDto> approvalRequestList = accountApprovalRequestService.getAccountApprovalRequestByUserIdList(userList.stream()
                .map(user -> {return user.getIdx();})
                .collect(Collectors.toList()));

        approvalRequestList.sort(Comparator.comparing(AccountApprovalRequestDto::getCreateDate).reversed());
        approvalRequestList.sort((a, b) -> Boolean.compare(a.getCompletionYn(), b.getCompletionYn()));

        ColumnInfo columnInfo = new ColumnInfo();

        columnInfo.setColumnType("basic");
        columnInfo.setFilterType("text");
        columnInfo.setName("계정");
        columnInfo.setKey("userId");
        getRequestMemberAccountListOutputDto.getColumnInfoList().add(columnInfo);

        columnInfo = new ColumnInfo();
        columnInfo.setColumnType("basic");
        columnInfo.setFilterType("text");
        columnInfo.setName("담당자 명");
        columnInfo.setKey("name");
        getRequestMemberAccountListOutputDto.getColumnInfoList().add(columnInfo);

        columnInfo = new ColumnInfo();
        columnInfo.setColumnType("basic");
        columnInfo.setFilterType("select");
        columnInfo.setName("요청구분");
        columnInfo.setKey("requestType");
        getRequestMemberAccountListOutputDto.getColumnInfoList().add(columnInfo);

        columnInfo = new ColumnInfo();
        columnInfo.setColumnType("basic");
        columnInfo.setFilterType("text");
        columnInfo.setName("신청사유");
        columnInfo.setKey("requestReason");
        getRequestMemberAccountListOutputDto.getColumnInfoList().add(columnInfo);

        columnInfo = new ColumnInfo();
        columnInfo.setColumnType("basic");
        columnInfo.setFilterType("text");
        columnInfo.setName("반려사유");
        columnInfo.setKey("rejectionReason");
        getRequestMemberAccountListOutputDto.getColumnInfoList().add(columnInfo);

        columnInfo = new ColumnInfo();
        columnInfo.setColumnType("basic");
        columnInfo.setFilterType("select");
        columnInfo.setName("완료여부");
        columnInfo.setKey("completionYn");
        getRequestMemberAccountListOutputDto.getColumnInfoList().add(columnInfo);

        columnInfo = new ColumnInfo();
        columnInfo.setColumnType("basic");
        columnInfo.setFilterType("text");
        columnInfo.setName("역할");
        columnInfo.setKey("role");
        getRequestMemberAccountListOutputDto.getColumnInfoList().add(columnInfo);

        columnInfo = new ColumnInfo();
        columnInfo.setColumnType("basic");
        columnInfo.setFilterType("text");
        columnInfo.setName("담당자 연락처");
        columnInfo.setKey("contactPhone");
        getRequestMemberAccountListOutputDto.getColumnInfoList().add(columnInfo);

        columnInfo = new ColumnInfo();
        columnInfo.setColumnType("basic");
        columnInfo.setFilterType("text");
        columnInfo.setName("담당자 메일");
        columnInfo.setKey("contactMail");
        getRequestMemberAccountListOutputDto.getColumnInfoList().add(columnInfo);

        columnInfo = new ColumnInfo();
        columnInfo.setColumnType("basic");
        columnInfo.setFilterType("date");
        columnInfo.setName("신청일");
        columnInfo.setKey("createDate");
        getRequestMemberAccountListOutputDto.getColumnInfoList().add(columnInfo);

        columnInfo = new ColumnInfo();
        columnInfo.setColumnType("basic");
        columnInfo.setFilterType("date");
        columnInfo.setName("처리일");
        columnInfo.setKey("modifyDate");
        getRequestMemberAccountListOutputDto.getColumnInfoList().add(columnInfo);

        approvalRequestList.forEach(reqList -> {
            Map<String, Object> row = new HashMap<>();
            MemberInfomationDto memberInfo = memberInfoList.stream()
                    .filter(member -> Objects.equals(member.getUserId(), reqList.getUserId()))
                    .findFirst().orElse(new MemberInfomationDto());

            UserBaseDto userInfo = userList.stream()
                    .filter(user -> Objects.equals(user.getIdx(), reqList.getUserId()))
                    .findFirst().orElse(new UserBaseDto());

            row.put("idx", reqList.getIdx());
            row.put("requestType", reqList.getRequestType().equals("C")?"신규":"수정");
            row.put("completionYn", reqList.getCompletionYn()?"완료":"대기");
            row.put("requestReason", reqList.getRequestReason());
            row.put("rejectionReason", reqList.getRejectionReason());
            row.put("userId", userInfo.getUserId());
            row.put("name", memberInfo.getName());
            row.put("role", memberInfo.getRole());
            row.put("contactPhone", memberInfo.getContactPhone());
            row.put("contactMail", memberInfo.getContactMail());
            row.put("createDate", reqList.getCreateDate());
            row.put("modifyDate", reqList.getCreateDate().equals(reqList.getModifyDate())?"":reqList.getModifyDate());
            getRequestMemberAccountListOutputDto.getDataList().add(row);
        });

        result.put("result", "S");
        result.put("output", getRequestMemberAccountListOutputDto);

        return result;
    }

    @Transactional
    public Map<String, Object> createMemberUser(HttpServletRequest request, MemberUserInfoDto memberUserInfoDto) throws Exception{
        HttpSessionUtil httpSessionUtil = new HttpSessionUtil(request.getSession(false));
        SuccessOutputDto successOutputDto = new SuccessOutputDto();
        ErrorOutputDto errorOutputDto = new ErrorOutputDto();
        Map<String, Object> result = new HashMap<>();
        result.put("result", "E");

        int userId = jwtUtil.getUserId(httpSessionUtil.getAttribute("jwt").toString());

        EncryptionUtil encryptionUtil = new EncryptionUtil();

        CompanyInfomationDto companyInfo = companyInfomationService.getCompanyInfomationByUserId(userId);
        UserBaseDto companyUser =  userBaseService.getUserBaseById(userId);

        if(!userBaseService.getUserBaseByUserId(memberUserInfoDto.getUserId()).stream()
                .filter(UserBaseDto::getUseYn)
                .filter(user -> !user.getDelYn())
                .filter(user -> Objects.equals(memberInfomationService.getMemberInfomationByUserId(user.getIdx()).getCompanyId(), companyInfo.getIdx()))
                .collect(Collectors.toList()).isEmpty()){
            errorOutputDto.setCode(404);
            errorOutputDto.setDetailReason("중복된 계정입니다.");
            result.put("output", errorOutputDto);

            return result;
        }

        UserBaseDto userBaseDto = new UserBaseDto();
        String salt = encryptionUtil.getSalt();

        userBaseDto.setUserId(memberUserInfoDto.getUserId());
        userBaseDto.setSalt(salt);
        userBaseDto.setUserPw(encryptionUtil.getSHA256WithSalt(memberUserInfoDto.getPw(), salt));
        userBaseDto.setUserStatus("ACST_ACTV");
        switch (companyUser.getUserType()){
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
                break;
        }
        userBaseDto.create(userId);

        UserBaseDto save = userBaseService.save(userBaseDto);

        MemberInfomationDto memberInfomationDto = new MemberInfomationDto();
        memberInfomationDto.setName(memberUserInfoDto.getName());
        memberInfomationDto.setUserId(save.getIdx());
        memberInfomationDto.setRole(memberUserInfoDto.getRole());
        memberInfomationDto.setCompanyId(companyInfo.getIdx());
        memberInfomationDto.setContactMail(memberUserInfoDto.getMail());
        memberInfomationDto.setContactPhone(memberUserInfoDto.getContactPhone());

        memberInfomationService.save(memberInfomationDto);

        successOutputDto.setCode(200);
        successOutputDto.setMessage("계정이 생성되었습니다.");
        result.put("result", "S");
        result.put("output", successOutputDto);

        return result;
    }

    @Transactional
    public Map<String, Object> modifyMemberUser(HttpServletRequest request, MemberUserInfoDto memberUserInfoDto) throws Exception{
        HttpSessionUtil httpSessionUtil = new HttpSessionUtil(request.getSession(false));
        SuccessOutputDto successOutputDto = new SuccessOutputDto();
        ErrorOutputDto errorOutputDto = new ErrorOutputDto();
        Map<String, Object> result = new HashMap<>();
        result.put("result", "E");

        int userId = jwtUtil.getUserId(httpSessionUtil.getAttribute("jwt").toString());

        UserBaseDto userBaseDto = userBaseService.getUserBaseById(memberUserInfoDto.getIdx());

        MemberInfomationDto memberInfomationDto = memberInfomationService.getMemberInfomationByUserId(memberUserInfoDto.getIdx());
        memberInfomationDto.setName(memberUserInfoDto.getName());
        memberInfomationDto.setRole(memberUserInfoDto.getRole());
        memberInfomationDto.setContactMail(memberUserInfoDto.getMail());
        memberInfomationDto.setContactPhone(memberUserInfoDto.getContactPhone());

        memberInfomationService.save(memberInfomationDto);

        userBaseDto.modify(userId);
        UserBaseDto save = userBaseService.save(userBaseDto);

        successOutputDto.setCode(200);
        successOutputDto.setMessage("계정이 수정되었습니다.");
        result.put("result", "S");
        result.put("output", successOutputDto);

        return result;
    }

    @Transactional
    public Map<String, Object> deleteMemberUser(HttpServletRequest request, MemberUserInfoDto memberUserInfoDto) throws Exception{
        HttpSessionUtil httpSessionUtil = new HttpSessionUtil(request.getSession(false));
        SuccessOutputDto successOutputDto = new SuccessOutputDto();
        ErrorOutputDto errorOutputDto = new ErrorOutputDto();
        Map<String, Object> result = new HashMap<>();
        result.put("result", "E");

        int userId = jwtUtil.getUserId(httpSessionUtil.getAttribute("jwt").toString());

        UserBaseDto userBaseById = userBaseService.getUserBaseById(memberUserInfoDto.getIdx());
        userBaseById.setDelYn(true);
        userBaseById.delete(userId);

        userBaseService.save(userBaseById);

        successOutputDto.setCode(200);
        successOutputDto.setMessage("계정이 삭제되었습니다.");
        result.put("result", "S");
        result.put("output", successOutputDto);

        return result;
    }

    @Transactional
    public Map<String, Object> approveAccount(HttpServletRequest request, MemberAccountRequestProcessingDto memberAccountRequestProcessingDto) throws Exception{
        HttpSessionUtil httpSessionUtil = new HttpSessionUtil(request.getSession(false));
        SuccessOutputDto successOutputDto = new SuccessOutputDto();
        ErrorOutputDto errorOutputDto = new ErrorOutputDto();
        Map<String, Object> result = new HashMap<>();
        result.put("result", "E");

        int userId = jwtUtil.getUserId(httpSessionUtil.getAttribute("jwt").toString());

        AccountApprovalRequestDto accountApprovalRequestDto = accountApprovalRequestService.getAccountApprovalRequestById(memberAccountRequestProcessingDto.getIdx());

        accountApprovalRequestDto.setCompletionYn(true);
        accountApprovalRequestDto.modify(userId);
        accountApprovalRequestService.save(accountApprovalRequestDto);

        UserBaseDto userBaseDto = userBaseService.getUserBaseById(accountApprovalRequestDto.getUserId());
        MemberInfomationDto memberInfomationDto = memberInfomationService.getMemberInfomationByUserId(accountApprovalRequestDto.getUserId());
        CompanyInfomationDto companyInfomationDto = companyInfomationService.getCompanyInfomationById(memberInfomationDto.getCompanyId());
        UserBaseDto companyUser = userBaseService.getUserBaseById(companyInfomationDto.getUserId());

        userBaseDto.setUserStatus("ACST_ACTV");
        if(accountApprovalRequestDto.getRequestType().equals("C")){
            userBaseDto.create(userId);
        }else{
            userBaseDto.modify(userId);
        }
        userBaseService.save(userBaseDto);

        successOutputDto.setCode(200);
        successOutputDto.setMessage("요청이 승인되었습니다.");

        String body = "회원가입 요청이 완료되었습니다.\n";
        if(accountApprovalRequestDto.getRequestType().equals("C")){
            String url = "https://datamon2.xyz/" + companyUser.getUserId() + "/login";
            body = body + "url : <a href=" + url + ">" + url + "</a>\n";
            body = body + "이 메일은 회신이 불가합니다.";
        }else{
            body = "회원정보 수정요청이 완료되었습니다.";
        }

        // 외부 API 요청을 위한 데이터 설정
        String url = "https://driven-notification.xyz/mail/send";
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("receiver_email", memberInfomationDto.getContactMail());
        if(accountApprovalRequestDto.getRequestType().equals("C")){
            requestBody.put("subject", "[데이터몬] 회원가입이 완료되었습니다.");
        }else{
            requestBody.put("subject", "[데이터몬] 회원정보 수정요청이 완료되었습니다.");
        }
        requestBody.put("body", body);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        try{
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
        } catch (Exception e) {
            successOutputDto.setMessage("요청이 승인되었지만 승인완료 이메일이 발송되지 않았습니다.");
        }

        result.put("result", "S");
        result.put("output", successOutputDto);

        return result;
    }

    @Transactional
    public Map<String, Object> rejectAccount(HttpServletRequest request, MemberAccountRequestProcessingDto memberAccountRequestProcessingDto) throws Exception{
        HttpSessionUtil httpSessionUtil = new HttpSessionUtil(request.getSession(false));
        SuccessOutputDto successOutputDto = new SuccessOutputDto();
        ErrorOutputDto errorOutputDto = new ErrorOutputDto();
        Map<String, Object> result = new HashMap<>();
        result.put("result", "E");

        int userId = jwtUtil.getUserId(httpSessionUtil.getAttribute("jwt").toString());

        AccountApprovalRequestDto accountApprovalRequestDto = accountApprovalRequestService.getAccountApprovalRequestById(memberAccountRequestProcessingDto.getIdx());

        accountApprovalRequestDto.setCompletionYn(true);
        accountApprovalRequestDto.setRejectionReason(memberAccountRequestProcessingDto.getRejectionReason());
        accountApprovalRequestDto.modify(userId);
        AccountApprovalRequestDto saveReq = accountApprovalRequestService.save(accountApprovalRequestDto);

        UserBaseDto userBaseDto = userBaseService.getUserBaseById(accountApprovalRequestDto.getUserId());

        if(accountApprovalRequestDto.getRequestType().equals("C")){
            userBaseDto.create(userId);
        }else{
            userBaseDto.modify(userId);
        }
        UserBaseDto save = userBaseService.save(userBaseDto);

        MemberInfomationDto memberInfomationDto = memberInfomationService.getMemberInfomationByUserId(save.getIdx());

        successOutputDto.setCode(200);
        successOutputDto.setMessage("요청이 반려되었습니다.");

        String body = "회원가입 요청이 반려되었습니다.\n";
        if(accountApprovalRequestDto.getRequestType().equals("C")){
            body = body + "반려사유 : " + saveReq.getRejectionReason() + "\n";
        }else{
            body = "회원정보 수정요청이 반려되었습니다.";
            body = body + "반려사유 : " + saveReq.getRejectionReason() + "\n";
        }

        // 외부 API 요청을 위한 데이터 설정
        String url = "https://driven-notification.xyz/mail/send";
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("receiver_email", memberInfomationDto.getContactMail());
        if(accountApprovalRequestDto.getRequestType().equals("C")){
            requestBody.put("subject", "[데이터몬] 회원가입이 반려되었습니다.");
        }else{
            requestBody.put("subject", "[데이터몬] 회원정보 수정요청이 반려되었습니다.");
        }
        requestBody.put("body", body);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        try{
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
        } catch (Exception e) {
            successOutputDto.setMessage("요청이 반려되었지만 반려 이메일이 발송되지 않았습니다.");
        }

        result.put("result", "S");
        result.put("output", successOutputDto);

        return result;
    }

    @Transactional
    public Map<String, Object> requestSendVerificationSms(String phone) throws Exception{
        SuccessOutputDto successOutputDto = new SuccessOutputDto();
        ErrorOutputDto errorOutputDto = new ErrorOutputDto();
        Map<String, Object> result = new HashMap<>();
        result.put("result", "E");

        SmsVerificationDto smsVerificationDto = new SmsVerificationDto();
        smsVerificationDto.setVerificationCode(generateVerificationCode("int", 6));
        smsVerificationDto.create(CommonCodeCache.getSystemIdIdx());

        SmsVerificationDto save = smsVerificationService.save(smsVerificationDto);

        String body = "데이터몬 인증코드입니다.\n";
        body = body + "인증번호 [" + save.getVerificationCode() + "]\n";
        body = body + "이 문자는 회신이 불가합니다.";

        // 외부 API 요청을 위한 데이터 설정
        String url = "https://driven-notification.xyz/sms/send";
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("DEST_PHONE", phone);
        requestBody.put("SEND_PHONE", "025528818");
        requestBody.put("MSG_BODY", body);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

        successOutputDto.setCode(200);
        successOutputDto.setMessage(save.getIdx());
        result.put("result", "S");
        result.put("output", successOutputDto);

        return result;
    }

    @Transactional
    public Map<String, Object> requestSendVerificationMail(String email) throws Exception{
        SuccessOutputDto successOutputDto = new SuccessOutputDto();
        ErrorOutputDto errorOutputDto = new ErrorOutputDto();
        Map<String, Object> result = new HashMap<>();
        result.put("result", "E");

        EmailVerificationDto emailVerificationDto = new EmailVerificationDto();
        emailVerificationDto.setVerificationCode(generateVerificationCode("string", 16));
        emailVerificationDto.create(CommonCodeCache.getSystemIdIdx());

        EmailVerificationDto save = emailVerificationService.save(emailVerificationDto);

        String body = "데이터몬 인증코드입니다.\n";
        body = body + "인증번호 [" + save.getVerificationCode() + "]\n";
        body = body + "이 메일은 회신이 불가합니다.";

        // 외부 API 요청을 위한 데이터 설정
        String url = "https://driven-notification.xyz/mail/send";
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("receiver_email", email);
        requestBody.put("subject", "[데이터몬] 회원가입 인증메일입니다.");
        requestBody.put("body", body);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

        successOutputDto.setCode(200);
        successOutputDto.setMessage(save.getIdx());
        result.put("result", "S");
        result.put("output", successOutputDto);

        return result;
    }

    @Transactional
    public Map<String, Object> confirmSmsVerification(VerificationInfo verificationInfo) throws Exception{
        SuccessOutputDto successOutputDto = new SuccessOutputDto();
        ErrorOutputDto errorOutputDto = new ErrorOutputDto();
        Map<String, Object> result = new HashMap<>();
        result.put("result", "E");

        SmsVerificationDto smsVerificationDto = smsVerificationService.getSmsVerificationById(verificationInfo.getIdx());

        if(smsVerificationDto.getIdx() == null){
            errorOutputDto.setCode(500);
            errorOutputDto.setDetailReason("인증에 실패하였습니다.");
            result.put("output", errorOutputDto);
            return result;
        }

        if(smsVerificationDto.getVerificationCode().equals(verificationInfo.getVerificationCode())){
            smsVerificationService.delete(smsVerificationDto.getIdx());
        }else{
            errorOutputDto.setCode(500);
            errorOutputDto.setDetailReason("인증에 실패하였습니다.");
            result.put("output", errorOutputDto);
            return result;
        }

        successOutputDto.setCode(200);
        successOutputDto.setMessage("인증이 완료되었습니다.");
        result.put("result", "S");
        result.put("output", successOutputDto);

        return result;
    }

    @Transactional
    public Map<String, Object> confirmMailVerification(VerificationInfo verificationInfo) throws Exception{
        SuccessOutputDto successOutputDto = new SuccessOutputDto();
        ErrorOutputDto errorOutputDto = new ErrorOutputDto();
        Map<String, Object> result = new HashMap<>();
        result.put("result", "E");

        EmailVerificationDto emailVerificationDto = emailVerificationService.getEmailVerificationById(verificationInfo.getIdx());

        if(emailVerificationDto.getIdx() == null){
            errorOutputDto.setCode(500);
            errorOutputDto.setDetailReason("인증에 실패하였습니다.");
            result.put("output", errorOutputDto);
            return result;
        }

        if(emailVerificationDto.getVerificationCode().equals(verificationInfo.getVerificationCode())){
            emailVerificationService.delete(emailVerificationDto.getIdx());
        }else{
            errorOutputDto.setCode(500);
            errorOutputDto.setDetailReason("인증에 실패하였습니다.");
            result.put("output", errorOutputDto);
            return result;
        }

        successOutputDto.setCode(200);
        successOutputDto.setMessage("인증이 완료되었습니다.");
        result.put("result", "S");
        result.put("output", successOutputDto);

        return result;
    }

    private String generateVerificationCode(String mode, int length) {
        String characters;
        if(mode.equals("string")){
            characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        }else{
            characters = "0123456789";
        }
        SecureRandom random = new SecureRandom();

        // 특정 길이 만큼 무작위 문자열 생성
        return IntStream.range(0, length)
                .map(i -> random.nextInt(characters.length()))
                .mapToObj(randomIndex -> String.valueOf(characters.charAt(randomIndex)))
                .collect(Collectors.joining());
    }
}
