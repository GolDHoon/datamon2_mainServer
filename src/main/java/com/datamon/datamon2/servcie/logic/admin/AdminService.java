package com.datamon.datamon2.servcie.logic.admin;

import com.datamon.datamon2.common.CommonCodeCache;
import com.datamon.datamon2.dto.input.admin.AdminAccountDto;
import com.datamon.datamon2.dto.input.admin.AdminAccountRequestProcessingDto;
import com.datamon.datamon2.dto.input.admin.AdminUserInfoDto;
import com.datamon.datamon2.dto.input.admin.CheckIdDuplicateDto;
import com.datamon.datamon2.dto.output.admin.GetAdminListOutputDto;
import com.datamon.datamon2.dto.output.admin.GetAdminOutputDto;
import com.datamon.datamon2.dto.output.admin.GetRequestAdminAccountListOutputDto;
import com.datamon.datamon2.dto.output.admin.SearchCompanyInfoByBRMOutputDto;
import com.datamon.datamon2.dto.output.common.ColumnInfo;
import com.datamon.datamon2.dto.output.common.ErrorOutputDto;
import com.datamon.datamon2.dto.output.common.SuccessOutputDto;
import com.datamon.datamon2.dto.output.member.VerificationInfo;
import com.datamon.datamon2.dto.repository.*;
import com.datamon.datamon2.servcie.repository.AccountApprovalRequestService;
import com.datamon.datamon2.servcie.repository.CompanyInfomationService;
import com.datamon.datamon2.servcie.repository.EmailVerificationService;
import com.datamon.datamon2.servcie.repository.UserBaseService;
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
public class AdminService {
    private UserBaseService userBaseService;
    private CompanyInfomationService companyInfomationService;
    private AccountApprovalRequestService accountApprovalRequestService;
    private EmailVerificationService emailVerificationService;
    private JwtUtil jwtUtil;

    private RestTemplate restTemplate = new RestTemplate();

    public AdminService(UserBaseService userBaseService, CompanyInfomationService companyInfomationService, AccountApprovalRequestService accountApprovalRequestService, EmailVerificationService emailVerificationService, JwtUtil jwtUtil) {
        this.userBaseService = userBaseService;
        this.companyInfomationService = companyInfomationService;
        this.accountApprovalRequestService = accountApprovalRequestService;
        this.emailVerificationService = emailVerificationService;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public Map<String, Object> getAdmin(HttpServletRequest request) throws Exception{
        HttpSessionUtil httpSessionUtil = new HttpSessionUtil(request.getSession(false));
        Map<String, Object> result = new HashMap<>();
        GetAdminOutputDto getAdminOutputDto = new GetAdminOutputDto();
        ErrorOutputDto errorOutputDto = new ErrorOutputDto();
        result.put("result", "E");

        int userId = jwtUtil.getUserId(httpSessionUtil.getAttribute("jwt").toString());

        UserBaseDto userBaseDto = userBaseService.getUserBaseById(userId);
        CompanyInfomationDto companyInfomationDto = companyInfomationService.getCompanyInfomationByUserId(userId);

        getAdminOutputDto.setUserId(userBaseDto.getUserId());
        getAdminOutputDto.setName(companyInfomationDto.getName());
        getAdminOutputDto.setCeo(companyInfomationDto.getCeo());
        getAdminOutputDto.setCorporateNumber(companyInfomationDto.getCorporateNumber());
        getAdminOutputDto.setCorporateAddress(companyInfomationDto.getCorporateAddress());
        getAdminOutputDto.setCorporateMail(companyInfomationDto.getCorporateMail());
        getAdminOutputDto.setBusinessStatus(companyInfomationDto.getBusinessStatus());
        getAdminOutputDto.setBusinessItem(companyInfomationDto.getBusinessItem());

        result.put("output", getAdminOutputDto);
        result.put("result", "S");
        return result;
    }

    @Transactional
    public Map<String, Object> checkIdDuplicate(CheckIdDuplicateDto checkIdDuplicateDto) throws Exception{
        Map<String, Object> result = new HashMap<>();
        SuccessOutputDto successOutputDto = new SuccessOutputDto();
        ErrorOutputDto errorOutputDto = new ErrorOutputDto();
        result.put("result", "E");

        List<String> companyCode = new ArrayList<>();
        companyCode.add("USTY_MAST");
        companyCode.add("USTY_CLNT");
        companyCode.add("USTY_ADAC");
        companyCode.add("USTY_CRAC");

        List<UserBaseDto> userList = userBaseService.getUserBaseByUserId(checkIdDuplicateDto.getUserId()).stream()
                .filter(UserBaseDto::getUseYn)
                .filter(user -> !user.getDelYn())
                .filter(user -> companyCode.contains(user.getUserType()))
                .toList();

        if(!userList.isEmpty()){
            errorOutputDto.setCode(404);
            errorOutputDto.setDetailReason("계정이 중복입니다.");
            result.put("output", errorOutputDto);
        }else{
            successOutputDto.setCode(200);
            successOutputDto.setMessage("사용가능한 계정입니다.");
            result.put("result", "S");
            result.put("output", successOutputDto);
        }
        return result;
    }

    @Transactional
    public Map<String, Object> searchCompanyInfoByBRM(String corporateNumber) throws Exception{
        Map<String, Object> result = new HashMap<>();
        SearchCompanyInfoByBRMOutputDto searchCompanyInfoByBRMOutputDto = new SearchCompanyInfoByBRMOutputDto();
        ErrorOutputDto errorOutputDto = new ErrorOutputDto();
        result.put("result", "E");

        CompanyInfomationDto companyInfoDto = companyInfomationService.getCompanyByCorporateNumber(corporateNumber);

        UserBaseDto userBaseDto = userBaseService.getUserBaseById(companyInfoDto.getUserId());

        if(userBaseDto.getDelYn() || !userBaseDto.getUseYn()){
            errorOutputDto.setDetailReason("업체ID를 찾을 수 없습니다.");
            errorOutputDto.setCode(404);
            result.put("output", errorOutputDto);
            return result;
        }

        searchCompanyInfoByBRMOutputDto.setCompanyId(userBaseDto.getUserId());
        result.put("output", searchCompanyInfoByBRMOutputDto);
        result.put("result", "S");
        return result;
    }

    @Transactional
    public Map<String, Object> requestAdminAccount(HttpServletRequest request, AdminAccountDto adminAccountDto) throws Exception{
        SuccessOutputDto successOutputDto = new SuccessOutputDto();
        ErrorOutputDto errorOutputDto = new ErrorOutputDto();
        Map<String, Object> result = new HashMap<>();
        result.put("result", "E");

        UserBaseDto userBaseDto = null;
        if(adminAccountDto.getRequestType().equals("C")){
            userBaseDto = new UserBaseDto();
        }else{
            HttpSessionUtil httpSessionUtil = new HttpSessionUtil(request.getSession(false));
            int userId = jwtUtil.getUserId(httpSessionUtil.getAttribute("jwt").toString());
            userBaseDto = userBaseService.getUserBaseById(userId);
        }
        EncryptionUtil encryptionUtil = new EncryptionUtil();
        String salt = encryptionUtil.getSalt();

        UserBaseDto saveUser = null;
        userBaseDto.setUserStatus("ACST_PEND");
        if(adminAccountDto.getRequestType().equals("C")){
            userBaseDto.setUserId(adminAccountDto.getUserId());
            userBaseDto.setUserPw(encryptionUtil.getSHA256WithSalt(adminAccountDto.getUserPw(), salt));
            userBaseDto.setSalt(salt);
            userBaseDto.setUserType(adminAccountDto.getUserType());
            userBaseDto.create(CommonCodeCache.getSystemIdIdx());
            saveUser = userBaseService.save(userBaseDto);
        }else{
            userBaseDto.modify(userBaseDto.getIdx());
            saveUser = userBaseService.save(userBaseDto);
        }

        CompanyInfomationDto companyInfomationDto = null;
        if(adminAccountDto.getRequestType().equals("C")){
            companyInfomationDto = new CompanyInfomationDto();
        }else{
            companyInfomationDto = companyInfomationService.getCompanyInfomationByUserId(saveUser.getIdx());
        }
        companyInfomationDto.setUserId(saveUser.getIdx());
        companyInfomationDto.setName(adminAccountDto.getName());
        companyInfomationDto.setCeo(adminAccountDto.getCeo());
        companyInfomationDto.setCorporateNumber(adminAccountDto.getCorporateNumber());
        companyInfomationDto.setCorporateAddress(adminAccountDto.getCorporateAddress());
        companyInfomationDto.setCorporateMail(adminAccountDto.getCorporateMail());
        companyInfomationDto.setBusinessStatus(adminAccountDto.getBusinessStatus());
        companyInfomationDto.setBusinessItem(adminAccountDto.getBusinessItem());
        companyInfomationService.save(companyInfomationDto);

        AccountApprovalRequestDto accountApprovalRequestDto = new AccountApprovalRequestDto();
        accountApprovalRequestDto.setRequestReason(adminAccountDto.getRequestReason());
        accountApprovalRequestDto.setRequestType(adminAccountDto.getRequestType());
        accountApprovalRequestDto.setUserId(saveUser.getIdx());
        accountApprovalRequestDto.setCompletionYn(false);
        accountApprovalRequestDto.createIdx();
        if(adminAccountDto.getRequestType().equals("C")){
            accountApprovalRequestDto.create(CommonCodeCache.getSystemIdIdx());
        }else{
            accountApprovalRequestDto.create(saveUser.getIdx());
        }

        accountApprovalRequestService.save(accountApprovalRequestDto);
        
        successOutputDto.setCode(200);
        if(adminAccountDto.getRequestType().equals("C")){
            successOutputDto.setMessage("계정신청이 완료되었습니다.");
        }else{
            successOutputDto.setMessage("계정수정신청이 완료되었습니다.");
        }
        result.put("result", "S");
        result.put("output", successOutputDto);
        return result;
    }

    @Transactional
    public Map<String, Object> getAdminList(HttpServletRequest request) throws Exception{
        Map<String, Object> result = new HashMap<>();
        GetAdminListOutputDto getAdminListOutputDto = new GetAdminListOutputDto();
        ErrorOutputDto errorOutputDto = new ErrorOutputDto();
        getAdminListOutputDto.setColumnInfoList(new ArrayList<>());
        getAdminListOutputDto.setDataList(new ArrayList<>());
        result.put("result", "E");

        ColumnInfo columnInfo = new ColumnInfo();

        columnInfo.setColumnType("basic");
        columnInfo.setFilterType("text");
        columnInfo.setName("계정");
        columnInfo.setKey("userId");
        getAdminListOutputDto.getColumnInfoList().add(columnInfo);

        columnInfo = new ColumnInfo();
        columnInfo.setColumnType("basic");
        columnInfo.setFilterType("text");
        columnInfo.setName("업체명");
        columnInfo.setKey("name");
        getAdminListOutputDto.getColumnInfoList().add(columnInfo);

        columnInfo = new ColumnInfo();
        columnInfo.setColumnType("basic");
        columnInfo.setFilterType("select");
        columnInfo.setName("업체유형");
        columnInfo.setKey("userType");
        getAdminListOutputDto.getColumnInfoList().add(columnInfo);

        columnInfo = new ColumnInfo();
        columnInfo.setColumnType("basic");
        columnInfo.setFilterType("text");
        columnInfo.setName("대표자명");
        columnInfo.setKey("ceo");
        getAdminListOutputDto.getColumnInfoList().add(columnInfo);

        columnInfo = new ColumnInfo();
        columnInfo.setColumnType("basic");
        columnInfo.setFilterType("text");
        columnInfo.setName("사업자등록번호");
        columnInfo.setKey("corporateNumber");
        getAdminListOutputDto.getColumnInfoList().add(columnInfo);

        columnInfo = new ColumnInfo();
        columnInfo.setColumnType("basic");
        columnInfo.setFilterType("text");
        columnInfo.setName("소재지");
        columnInfo.setKey("corporateAddress");
        getAdminListOutputDto.getColumnInfoList().add(columnInfo);

        columnInfo = new ColumnInfo();
        columnInfo.setColumnType("basic");
        columnInfo.setFilterType("text");
        columnInfo.setName("이메일");
        columnInfo.setKey("corporateMail");
        getAdminListOutputDto.getColumnInfoList().add(columnInfo);

        columnInfo = new ColumnInfo();
        columnInfo.setColumnType("basic");
        columnInfo.setFilterType("text");
        columnInfo.setName("소재지");
        columnInfo.setKey("corporateAddress");
        getAdminListOutputDto.getColumnInfoList().add(columnInfo);

        columnInfo = new ColumnInfo();
        columnInfo.setColumnType("basic");
        columnInfo.setFilterType("text");
        columnInfo.setName("업태");
        columnInfo.setKey("businessStatus");
        getAdminListOutputDto.getColumnInfoList().add(columnInfo);

        columnInfo = new ColumnInfo();
        columnInfo.setColumnType("basic");
        columnInfo.setFilterType("text");
        columnInfo.setName("업종");
        columnInfo.setKey("businessItem");
        getAdminListOutputDto.getColumnInfoList().add(columnInfo);

        columnInfo = new ColumnInfo();
        columnInfo.setColumnType("basic");
        columnInfo.setFilterType("date");
        columnInfo.setName("생성일");
        columnInfo.setKey("createDate");
        getAdminListOutputDto.getColumnInfoList().add(columnInfo);

        List<CompanyInfomationDto> companyInfoList = companyInfomationService.getCompanyInfomationAll();

        List<UserBaseDto> userList = userBaseService.getUserBaseByIdxList(companyInfoList.stream()
                        .map(CompanyInfomationDto::getUserId)
                        .collect(Collectors.toList())).stream()
                .filter(UserBaseDto::getUseYn)
                .filter(user -> !user.getDelYn())
                .filter(user -> user.getUserStatus().equals("ACST_ACTV"))
                .sorted(Comparator.comparing(UserBaseDto::getCreateDate).reversed())
                .toList();

        userList.forEach(user -> {
            Map<String, Object> row = new HashMap<>();

            CompanyInfomationDto companyInfo = companyInfoList.stream()
                    .filter(company -> Objects.equals(company.getUserId(), user.getIdx()))
                    .findFirst().orElse(new CompanyInfomationDto());

            row.put("idx", user.getIdx());
            row.put("userId", user.getUserId());
            switch (user.getUserType()){
                case "USTY_MAST": {
                    row.put("userType", "마스터");
                    break;
                }
                case "USTY_CLNT": {
                    row.put("userType", "광고주");
                    break;
                }
                case "USTY_ADAC": {
                    row.put("userType", "광고대행사");
                    break;
                }
                case "USTY_CRAC": {
                    row.put("userType", "CRM");
                    break;
                }
                default: break;
            }
            row.put("createDate", user.getCreateDate());
            row.put("name", companyInfo.getName());
            row.put("ceo", companyInfo.getCeo());
            row.put("corporateNumber", companyInfo.getCorporateNumber());
            row.put("corporateAddress", companyInfo.getCorporateAddress());
            row.put("corporateMail", companyInfo.getCorporateMail());
            row.put("businessStatus", companyInfo.getBusinessStatus());
            row.put("businessItem", companyInfo.getBusinessItem());

            getAdminListOutputDto.getDataList().add(row);
        });

        result.put("result", "S");
        result.put("output", getAdminListOutputDto);

        return result;
    }

    @Transactional
    public Map<String, Object> getRequestAdminAccountList(HttpServletRequest request) throws Exception {
        Map<String, Object> result = new HashMap<>();
        GetRequestAdminAccountListOutputDto getRequestAdminAccountListOutputDto = new GetRequestAdminAccountListOutputDto();
        getRequestAdminAccountListOutputDto.setColumnInfoList(new ArrayList<>());
        getRequestAdminAccountListOutputDto.setDataList(new ArrayList<>());
        HttpSessionUtil httpSessionUtil = new HttpSessionUtil(request.getSession(false));
        result.put("result", "E");

        int companyId = Integer.parseInt(httpSessionUtil.getAttribute("companyId").toString());

        List<CompanyInfomationDto> companyInfoList = companyInfomationService.getCompanyInfomationAll();

        List<UserBaseDto> userList = userBaseService.getUserBaseByIdxList(companyInfoList.stream()
                        .map(CompanyInfomationDto::getUserId)
                        .collect(Collectors.toList())).stream()
                .filter(UserBaseDto::getUseYn)
                .filter(user -> !user.getDelYn())
                .toList();

        List<AccountApprovalRequestDto> approvalRequestList = accountApprovalRequestService.getAccountApprovalRequestByUserIdList(userList.stream()
                .map(UserBaseDto::getIdx)
                .collect(Collectors.toList()));

        approvalRequestList.sort(Comparator.comparing(AccountApprovalRequestDto::getCreateDate).reversed());

        ColumnInfo columnInfo = new ColumnInfo();

        columnInfo.setColumnType("basic");
        columnInfo.setFilterType("text");
        columnInfo.setName("계정");
        columnInfo.setKey("userId");
        getRequestAdminAccountListOutputDto.getColumnInfoList().add(columnInfo);

        columnInfo = new ColumnInfo();
        columnInfo.setColumnType("basic");
        columnInfo.setFilterType("text");
        columnInfo.setName("업체명");
        columnInfo.setKey("name");
        getRequestAdminAccountListOutputDto.getColumnInfoList().add(columnInfo);

        columnInfo = new ColumnInfo();
        columnInfo.setColumnType("basic");
        columnInfo.setFilterType("select");
        columnInfo.setName("유저유형");
        columnInfo.setKey("userType");
        getRequestAdminAccountListOutputDto.getColumnInfoList().add(columnInfo);

        columnInfo = new ColumnInfo();
        columnInfo.setColumnType("basic");
        columnInfo.setFilterType("select");
        columnInfo.setName("요청구분");
        columnInfo.setKey("requestType");
        getRequestAdminAccountListOutputDto.getColumnInfoList().add(columnInfo);

        columnInfo = new ColumnInfo();
        columnInfo.setColumnType("basic");
        columnInfo.setFilterType("text");
        columnInfo.setName("신청사유");
        columnInfo.setKey("requestReason");
        getRequestAdminAccountListOutputDto.getColumnInfoList().add(columnInfo);

        columnInfo = new ColumnInfo();
        columnInfo.setColumnType("basic");
        columnInfo.setFilterType("text");
        columnInfo.setName("반려사유");
        columnInfo.setKey("rejectionReason");
        getRequestAdminAccountListOutputDto.getColumnInfoList().add(columnInfo);

        columnInfo = new ColumnInfo();
        columnInfo.setColumnType("basic");
        columnInfo.setFilterType("select");
        columnInfo.setName("완료여부");
        columnInfo.setKey("completionYn");
        getRequestAdminAccountListOutputDto.getColumnInfoList().add(columnInfo);

        columnInfo = new ColumnInfo();
        columnInfo.setColumnType("basic");
        columnInfo.setFilterType("text");
        columnInfo.setName("대표자명");
        columnInfo.setKey("ceo");
        getRequestAdminAccountListOutputDto.getColumnInfoList().add(columnInfo);

        columnInfo = new ColumnInfo();
        columnInfo.setColumnType("basic");
        columnInfo.setFilterType("text");
        columnInfo.setName("사업자등록번호");
        columnInfo.setKey("corporateNumber");
        getRequestAdminAccountListOutputDto.getColumnInfoList().add(columnInfo);

        columnInfo = new ColumnInfo();
        columnInfo.setColumnType("basic");
        columnInfo.setFilterType("text");
        columnInfo.setName("소재지");
        columnInfo.setKey("corporateAddress");
        getRequestAdminAccountListOutputDto.getColumnInfoList().add(columnInfo);

        columnInfo = new ColumnInfo();
        columnInfo.setColumnType("basic");
        columnInfo.setFilterType("text");
        columnInfo.setName("이메일");
        columnInfo.setKey("corporateMail");
        getRequestAdminAccountListOutputDto.getColumnInfoList().add(columnInfo);

        columnInfo = new ColumnInfo();
        columnInfo.setColumnType("basic");
        columnInfo.setFilterType("text");
        columnInfo.setName("소재지");
        columnInfo.setKey("corporateAddress");
        getRequestAdminAccountListOutputDto.getColumnInfoList().add(columnInfo);

        columnInfo = new ColumnInfo();
        columnInfo.setColumnType("basic");
        columnInfo.setFilterType("text");
        columnInfo.setName("업태");
        columnInfo.setKey("businessStatus");
        getRequestAdminAccountListOutputDto.getColumnInfoList().add(columnInfo);

        columnInfo = new ColumnInfo();
        columnInfo.setColumnType("basic");
        columnInfo.setFilterType("text");
        columnInfo.setName("업종");
        columnInfo.setKey("businessItem");
        getRequestAdminAccountListOutputDto.getColumnInfoList().add(columnInfo);

        columnInfo = new ColumnInfo();
        columnInfo.setColumnType("basic");
        columnInfo.setFilterType("date");
        columnInfo.setName("신청일");
        columnInfo.setKey("createDate");
        getRequestAdminAccountListOutputDto.getColumnInfoList().add(columnInfo);

        columnInfo = new ColumnInfo();
        columnInfo.setColumnType("basic");
        columnInfo.setFilterType("date");
        columnInfo.setName("처리일");
        columnInfo.setKey("modifyDate");
        getRequestAdminAccountListOutputDto.getColumnInfoList().add(columnInfo);

        approvalRequestList.forEach(reqList -> {
            Map<String, Object> row = new HashMap<>();
            CompanyInfomationDto companyInfo = companyInfoList.stream()
                    .filter(member -> Objects.equals(member.getUserId(), reqList.getUserId()))
                    .findFirst().orElse(new CompanyInfomationDto());

            UserBaseDto userInfo = userList.stream()
                    .filter(user -> Objects.equals(user.getIdx(), reqList.getUserId()))
                    .findFirst().orElse(new UserBaseDto());

            row.put("idx", reqList.getIdx());
            row.put("requestType", reqList.getRequestType().equals("C")?"신규":"수정");
            row.put("completionYn", reqList.getCompletionYn()?"완료":"대기");
            row.put("requestReason", reqList.getRequestReason());
            row.put("rejectionReason", reqList.getRejectionReason());
            row.put("userId", userInfo.getUserId());
            switch (userInfo.getUserType()){
                case "USTY_MAST": {
                    row.put("userType", "마스터");
                    break;
                }
                case "USTY_CLNT": {
                    row.put("userType", "광고주");
                    break;
                }
                case "USTY_ADAC": {
                    row.put("userType", "광고대행사");
                    break;
                }
                case "USTY_CRAC": {
                    row.put("userType", "CRM");
                    break;
                }
                default: break;
            }
            row.put("name", companyInfo.getName());
            row.put("ceo", companyInfo.getCeo());
            row.put("corporateNumber", companyInfo.getCorporateNumber());
            row.put("corporateAddress", companyInfo.getCorporateAddress());
            row.put("corporateMail", companyInfo.getCorporateMail());
            row.put("businessStatus", companyInfo.getBusinessStatus());
            row.put("businessItem", companyInfo.getBusinessItem());
            row.put("createDate", reqList.getCreateDate());
            row.put("modifyDate", reqList.getCreateDate().equals(reqList.getModifyDate())?"":reqList.getModifyDate());
            getRequestAdminAccountListOutputDto.getDataList().add(row);
        });

        result.put("result", "S");
        result.put("output", getRequestAdminAccountListOutputDto);

        return result;
    }

    @Transactional
    public Map<String, Object> createAdminUser (HttpServletRequest request, AdminUserInfoDto adminUserInfoDto) throws Exception{
        HttpSessionUtil httpSessionUtil = new HttpSessionUtil(request.getSession(false));
        SuccessOutputDto successOutputDto = new SuccessOutputDto();
        ErrorOutputDto errorOutputDto = new ErrorOutputDto();
        Map<String, Object> result = new HashMap<>();
        result.put("result", "E");

        int userId = jwtUtil.getUserId(httpSessionUtil.getAttribute("jwt").toString());

        EncryptionUtil encryptionUtil = new EncryptionUtil();

        List<String> companyUserTypeList = new ArrayList<>();
        companyUserTypeList.add("USTY_MAST");
        companyUserTypeList.add("USTY_CLNT");
        companyUserTypeList.add("USTY_ADAC");
        companyUserTypeList.add("USTY_CRAC");

        if(userBaseService.getUserBaseByUserTypeList(companyUserTypeList).stream()
            .anyMatch(user -> user.getUserId().equals(adminUserInfoDto.getUserId()))) {
                errorOutputDto.setCode(404);
                errorOutputDto.setDetailReason("중복된 계정입니다.");
                result.put("output", errorOutputDto);

                return result;
            }

        String salt = encryptionUtil.getSalt();

        UserBaseDto userBaseDto = new UserBaseDto();
        userBaseDto.setUserId(adminUserInfoDto.getUserId());
        userBaseDto.setSalt(salt);
        userBaseDto.setUserPw(encryptionUtil.getSHA256WithSalt(adminUserInfoDto.getUserPw(), salt));
        userBaseDto.setUserType(adminUserInfoDto.getUserType());
        userBaseDto.setUserStatus("ACST_ACTV");
        userBaseDto.create(userId);

        UserBaseDto userInfo = userBaseService.save(userBaseDto);

        CompanyInfomationDto companyInfomationDto = new CompanyInfomationDto();
        companyInfomationDto.setUserId(userInfo.getIdx());
        companyInfomationDto.setName(adminUserInfoDto.getName());
        companyInfomationDto.setCeo(adminUserInfoDto.getCeo());
        companyInfomationDto.setCorporateNumber(adminUserInfoDto.getCorporateNumber());
        companyInfomationDto.setCorporateAddress(adminUserInfoDto.getCorporateAddress());
        companyInfomationDto.setCorporateMail(adminUserInfoDto.getCorporateMail());
        companyInfomationDto.setBusinessStatus(adminUserInfoDto.getBusinessStatus());
        companyInfomationDto.setBusinessItem(adminUserInfoDto.getBusinessItem());

        companyInfomationService.save(companyInfomationDto);

        successOutputDto.setCode(200);
        successOutputDto.setMessage("계정이 생성되었습니다.");
        result.put("result", "S");
        result.put("output", successOutputDto);

        return result;
    }

    @Transactional
    public Map<String, Object> modifyAdminUser (HttpServletRequest request, AdminUserInfoDto adminUserInfoDto) throws Exception{
        HttpSessionUtil httpSessionUtil = new HttpSessionUtil(request.getSession(false));
        SuccessOutputDto successOutputDto = new SuccessOutputDto();
        ErrorOutputDto errorOutputDto = new ErrorOutputDto();
        Map<String, Object> result = new HashMap<>();
        result.put("result", "E");

        int userId = jwtUtil.getUserId(httpSessionUtil.getAttribute("jwt").toString());

        UserBaseDto userInfo = userBaseService.getUserBaseById(adminUserInfoDto.getIdx());
        CompanyInfomationDto companyInfoDto = companyInfomationService.getCompanyInfomationByUserId(userInfo.getIdx());

        companyInfoDto.setName(adminUserInfoDto.getName());
        companyInfoDto.setCeo(adminUserInfoDto.getCeo());
        companyInfoDto.setCorporateNumber(adminUserInfoDto.getCorporateNumber());
        companyInfoDto.setCorporateAddress(adminUserInfoDto.getCorporateAddress());
        companyInfoDto.setCorporateMail(adminUserInfoDto.getCorporateMail());
        companyInfoDto.setBusinessStatus(adminUserInfoDto.getBusinessStatus());
        companyInfoDto.setBusinessItem(adminUserInfoDto.getBusinessItem());

        companyInfomationService.save(companyInfoDto);
        userInfo.modify(userId);
        userBaseService.save(userInfo);

        successOutputDto.setCode(200);
        successOutputDto.setMessage("계정이 수정되었습니다.");
        result.put("result", "S");
        result.put("output", successOutputDto);

        return result;
    }

    @Transactional
    public Map<String, Object> deleteAdminUser (HttpServletRequest request, AdminUserInfoDto adminUserInfoDto) throws Exception{
        HttpSessionUtil httpSessionUtil = new HttpSessionUtil(request.getSession(false));
        SuccessOutputDto successOutputDto = new SuccessOutputDto();
        ErrorOutputDto errorOutputDto = new ErrorOutputDto();
        Map<String, Object> result = new HashMap<>();
        result.put("result", "E");

        int userId = jwtUtil.getUserId(httpSessionUtil.getAttribute("jwt").toString());

        UserBaseDto userBaseDto = userBaseService.getUserBaseById(adminUserInfoDto.getIdx());
        userBaseDto.delete(userId);
        userBaseService.save(userBaseDto);

        successOutputDto.setCode(200);
        successOutputDto.setMessage("계정이 삭제되었습니다.");
        result.put("result", "S");
        result.put("output", successOutputDto);

        return result;
    }

    @Transactional
    public Map<String, Object> approveAccount(HttpServletRequest request, AdminAccountRequestProcessingDto adminAccountRequestProcessingDto) throws Exception{
        HttpSessionUtil httpSessionUtil = new HttpSessionUtil(request.getSession(false));
        SuccessOutputDto successOutputDto = new SuccessOutputDto();
        ErrorOutputDto errorOutputDto = new ErrorOutputDto();
        Map<String, Object> result = new HashMap<>();
        result.put("result", "E");

        int userId = jwtUtil.getUserId(httpSessionUtil.getAttribute("jwt").toString());

        AccountApprovalRequestDto accountApprovalRequestDto = accountApprovalRequestService.getAccountApprovalRequestById(adminAccountRequestProcessingDto.getIdx());

        accountApprovalRequestDto.setCompletionYn(true);
        accountApprovalRequestDto.modify(userId);
        accountApprovalRequestService.save(accountApprovalRequestDto);

        UserBaseDto userBaseDto = userBaseService.getUserBaseById(accountApprovalRequestDto.getUserId());

        userBaseDto.setUserStatus("ACST_ACTV");
        if(accountApprovalRequestDto.getRequestType().equals("C")){
            userBaseDto.create(userId);
        }else{
            userBaseDto.modify(userId);
        }
        UserBaseDto save = userBaseService.save(userBaseDto);
        CompanyInfomationDto companyInfomationDto = companyInfomationService.getCompanyInfomationByUserId(save.getIdx());

        successOutputDto.setCode(200);
        successOutputDto.setMessage("요청이 승인되었습니다.");

        String body = "회원가입 요청이 완료되었습니다.\n";
        if(accountApprovalRequestDto.getRequestType().equals("C")){
            String url = "https://datamon2.xyz/" + userBaseDto.getUserId() + "/login";
            body = body + "url : <a href=" + url + ">" + url + "</a>\n";
            body = body + "이 메일은 회신이 불가합니다.";
        }else{
            body = "회원정보 수정요청이 완료되었습니다.";
        }

        // 외부 API 요청을 위한 데이터 설정
        String url = "https://driven-notification.xyz/mail/send";
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("receiver_email", companyInfomationDto.getCorporateMail());
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
    public Map<String, Object> rejectAccount(HttpServletRequest request, AdminAccountRequestProcessingDto adminAccountRequestProcessingDto) throws Exception{
        HttpSessionUtil httpSessionUtil = new HttpSessionUtil(request.getSession(false));
        SuccessOutputDto successOutputDto = new SuccessOutputDto();
        ErrorOutputDto errorOutputDto = new ErrorOutputDto();
        Map<String, Object> result = new HashMap<>();
        result.put("result", "E");

        int userId = jwtUtil.getUserId(httpSessionUtil.getAttribute("jwt").toString());

        AccountApprovalRequestDto accountApprovalRequestDto = accountApprovalRequestService.getAccountApprovalRequestById(adminAccountRequestProcessingDto.getIdx());

        accountApprovalRequestDto.setCompletionYn(true);
        accountApprovalRequestDto.setRejectionReason(adminAccountRequestProcessingDto.getRejectionReason());
        accountApprovalRequestDto.modify(userId);
        AccountApprovalRequestDto saveReq = accountApprovalRequestService.save(accountApprovalRequestDto);

        UserBaseDto userBaseDto = userBaseService.getUserBaseById(accountApprovalRequestDto.getUserId());

        if(accountApprovalRequestDto.getRequestType().equals("C")){
            userBaseDto.create(userId);
        }else{
            userBaseDto.modify(userId);
        }
        UserBaseDto save = userBaseService.save(userBaseDto);
        CompanyInfomationDto companyInfomationDto = companyInfomationService.getCompanyInfomationByUserId(save.getIdx());

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
        requestBody.put("receiver_email", companyInfomationDto.getCorporateMail());
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
