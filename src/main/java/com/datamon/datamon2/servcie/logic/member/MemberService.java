package com.datamon.datamon2.servcie.logic.member;

import com.datamon.datamon2.common.CommonCodeCache;
import com.datamon.datamon2.dto.input.member.MemberAccountDto;
import com.datamon.datamon2.dto.input.member.CheckIdDuplicateDto;
import com.datamon.datamon2.dto.input.member.CreateMemberUserDto;
import com.datamon.datamon2.dto.input.member.DeleteMemberUserDto;
import com.datamon.datamon2.dto.output.common.ColumnInfo;
import com.datamon.datamon2.dto.output.common.ErrorOutputDto;
import com.datamon.datamon2.dto.output.common.SuccessOutputDto;
import com.datamon.datamon2.dto.output.member.GetMemberListOutputDto;
import com.datamon.datamon2.dto.output.member.GetRequestMemberAccountListOutputDto;
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

import java.util.*;
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
    public Map<String, Object> requestMemberAccount(MemberAccountDto memberAccountDto, HttpServletRequest request) throws Exception{
        SuccessOutputDto successOutputDto = new SuccessOutputDto();
        ErrorOutputDto errorOutputDto = new ErrorOutputDto();
        Map<String, Object> result = new HashMap<>();
        result.put("result", "E");

        UserBaseDto userBaseDto = new UserBaseDto();
        EncryptionUtil encryptionUtil = new EncryptionUtil();
        String salt = encryptionUtil.getSalt();

        CompanyInfomationDto companyInfomationDto = companyInfomationService.getCompanyInfomationById(memberAccountDto.getCompanyId());
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
        accountApprovalRequestDto.createIdx();
        accountApprovalRequestDto.create(CommonCodeCache.getSystemIdIdx());

        accountApprovalRequestService.save(accountApprovalRequestDto);


        successOutputDto.setCode(200);
        successOutputDto.setMessage("계정신청이 완료되었습니다.");
        result.put("result", "S");
        result.put("output", successOutputDto);
        return result;
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
                .filter(user -> user.getUserStatus().equals("ACST_PEND"))
                .collect(Collectors.toList());

        List<AccountApprovalRequestDto> approvalRequestList = accountApprovalRequestService.getAccountApprovalRequestByUserIdList(userList.stream()
                .map(user -> {return user.getIdx();})
                .collect(Collectors.toList()));

        approvalRequestList.sort(Comparator.comparing(AccountApprovalRequestDto::getCreateDate).reversed());

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
}
