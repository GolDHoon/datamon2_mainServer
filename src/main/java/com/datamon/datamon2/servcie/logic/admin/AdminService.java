package com.datamon.datamon2.servcie.logic.admin;

import com.datamon.datamon2.common.CommonCodeCache;
import com.datamon.datamon2.dto.input.admin.AdminAccountDto;
import com.datamon.datamon2.dto.output.admin.GetAdminListOutputDto;
import com.datamon.datamon2.dto.output.admin.GetRequestAdminAccountListOutputDto;
import com.datamon.datamon2.dto.output.common.ColumnInfo;
import com.datamon.datamon2.dto.output.common.ErrorOutputDto;
import com.datamon.datamon2.dto.output.common.SuccessOutputDto;
import com.datamon.datamon2.dto.output.member.GetMemberListOutputDto;
import com.datamon.datamon2.dto.output.member.GetRequestMemberAccountListOutputDto;
import com.datamon.datamon2.dto.repository.*;
import com.datamon.datamon2.servcie.repository.AccountApprovalRequestService;
import com.datamon.datamon2.servcie.repository.CompanyInfomationService;
import com.datamon.datamon2.servcie.repository.UserBaseService;
import com.datamon.datamon2.util.EncryptionUtil;
import com.datamon.datamon2.util.HttpSessionUtil;
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

    public AdminService(UserBaseService userBaseService, CompanyInfomationService companyInfomationService, AccountApprovalRequestService accountApprovalRequestService) {
        this.userBaseService = userBaseService;
        this.companyInfomationService = companyInfomationService;
        this.accountApprovalRequestService = accountApprovalRequestService;
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
                        .map(companyInfo -> {return companyInfo.getUserId();})
                        .collect(Collectors.toList())).stream()
                .filter(UserBaseDto::getUseYn)
                .filter(user -> !user.getDelYn())
                .filter(user -> user.getUserStatus().equals("ACST_ACTV"))
                .collect(Collectors.toList());

        userList.sort(Comparator.comparing(UserBaseDto::getCreateDate).reversed());

        userList.forEach(user -> {
            Map<String, Object> row = new HashMap<>();

            CompanyInfomationDto companyInfo = companyInfoList.stream()
                    .filter(company -> Objects.equals(company.getUserId(), user.getIdx()))
                    .findFirst().orElse(new CompanyInfomationDto());

            row.put("idx", user.getIdx());
            row.put("userId", user.getUserId());
            row.put("userType", CommonCodeCache.getUstyCodes().stream()
                    .filter(code -> code.getCodeFullName().equals(user.getUserType()))
                    .findFirst().orElse(new UstyCodeDto()).getCodeValue());
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
                        .map(company -> {return company.getUserId();})
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
            row.put("userType", CommonCodeCache.getUstyCodes().stream()
                    .filter(code -> code.getCodeFullName().equals(userInfo.getUserType()))
                    .findFirst().orElse(new UstyCodeDto()).getCodeValue());
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
}
