package com.datamon.datamon2.servcie.logic.admin;

import com.datamon.datamon2.common.CommonCodeCache;
import com.datamon.datamon2.dto.input.admin.AdminAccountDto;
import com.datamon.datamon2.dto.input.admin.AdminUserInfoDto;
import com.datamon.datamon2.dto.input.admin.CheckIdDuplicateDto;
import com.datamon.datamon2.dto.input.member.MemberAccountRequestProcessingDto;
import com.datamon.datamon2.dto.output.admin.GetAdminListOutputDto;
import com.datamon.datamon2.dto.output.admin.GetRequestAdminAccountListOutputDto;
import com.datamon.datamon2.dto.output.admin.SearchCompanyInfoByBRMOutputDto;
import com.datamon.datamon2.dto.output.common.ColumnInfo;
import com.datamon.datamon2.dto.output.common.ErrorOutputDto;
import com.datamon.datamon2.dto.output.common.SuccessOutputDto;
import com.datamon.datamon2.dto.repository.*;
import com.datamon.datamon2.servcie.repository.AccountApprovalRequestService;
import com.datamon.datamon2.servcie.repository.CompanyInfomationService;
import com.datamon.datamon2.servcie.repository.UserBaseService;
import com.datamon.datamon2.util.EncryptionUtil;
import com.datamon.datamon2.util.HttpSessionUtil;
import com.datamon.datamon2.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdminService {
    private UserBaseService userBaseService;
    private CompanyInfomationService companyInfomationService;
    private AccountApprovalRequestService accountApprovalRequestService;
    private JwtUtil jwtUtil;

    public AdminService(UserBaseService userBaseService, CompanyInfomationService companyInfomationService, AccountApprovalRequestService accountApprovalRequestService, JwtUtil jwtUtil) {
        this.userBaseService = userBaseService;
        this.companyInfomationService = companyInfomationService;
        this.accountApprovalRequestService = accountApprovalRequestService;
        this.jwtUtil = jwtUtil;
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
    public Map<String, Object> requestAdminAccount(AdminAccountDto adminAccountDto) throws Exception{
        SuccessOutputDto successOutputDto = new SuccessOutputDto();
        ErrorOutputDto errorOutputDto = new ErrorOutputDto();
        Map<String, Object> result = new HashMap<>();
        result.put("result", "E");

        UserBaseDto userBaseDto = new UserBaseDto();
        EncryptionUtil encryptionUtil = new EncryptionUtil();
        String salt = encryptionUtil.getSalt();


        userBaseDto.setUserId(adminAccountDto.getUserId());
        userBaseDto.setUserPw(encryptionUtil.getSHA256WithSalt(adminAccountDto.getUserPw(), salt));
        userBaseDto.setSalt(salt);
        userBaseDto.setUserType(adminAccountDto.getUserType());
        userBaseDto.setUserStatus("ACST_PEND");
        userBaseDto.create(CommonCodeCache.getSystemIdIdx());
        UserBaseDto saveUser = userBaseService.save(userBaseDto);

        CompanyInfomationDto companyInfomationDto = new CompanyInfomationDto();
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
                .filter(user -> user.getUserStatus().equals("ACST_PEND"))
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
    public Map<String, Object> approveAccount(HttpServletRequest request, MemberAccountRequestProcessingDto memberAccountRequestProcessingDto) throws Exception{
        HttpSessionUtil httpSessionUtil = new HttpSessionUtil(request.getSession(false));
        SuccessOutputDto successOutputDto = new SuccessOutputDto();
        ErrorOutputDto errorOutputDto = new ErrorOutputDto();
        Map<String, Object> result = new HashMap<>();
        result.put("result", "E");

        int userId = jwtUtil.getUserId(httpSessionUtil.getAttribute("jwt").toString());

        AccountApprovalRequestDto accountApprovalRequestDto = accountApprovalRequestService.getAccountApprovalRequestById(memberAccountRequestProcessingDto.getIdx());

        accountApprovalRequestDto.setCompletionYn(true);
        accountApprovalRequestDto.create(userId);
        accountApprovalRequestService.save(accountApprovalRequestDto);

        UserBaseDto userBaseDto = userBaseService.getUserBaseById(userId);

        userBaseDto.setUserStatus("ACST_ACTV");
        userBaseDto.create(userId);
        userBaseService.save(userBaseDto);

        successOutputDto.setCode(200);
        successOutputDto.setMessage("요청이 승인되었습니다.");
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
        accountApprovalRequestDto.setRequestReason(memberAccountRequestProcessingDto.getRejectionReason());
        accountApprovalRequestDto.create(userId);
        accountApprovalRequestService.save(accountApprovalRequestDto);

        UserBaseDto userBaseDto = userBaseService.getUserBaseById(userId);

        userBaseDto.create(userId);
        userBaseService.save(userBaseDto);

        successOutputDto.setCode(200);
        successOutputDto.setMessage("요청이 반려되었습니다.");
        result.put("result", "S");
        result.put("output", successOutputDto);

        return result;
    }
}
