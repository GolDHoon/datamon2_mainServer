package com.datamon.datamon2.servcie.logic;

import com.datamon.datamon2.common.CommonCodeCache;
import com.datamon.datamon2.dto.input.call.CustListDto;
import com.datamon.datamon2.dto.repository.*;
import com.datamon.datamon2.servcie.repository.*;
import com.datamon.datamon2.util.EncryptionUtil;
import com.datamon.datamon2.util.HttpSessionUtil;
import com.datamon.datamon2.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CallService {
    private JwtUtil jwtUtil;
    private UserBaseService userBaseService;
    private CompanyInfomationService companyInfomationService;
    private MemberInfomationService memberInfomationService;
    private UserCdbtMappingService userCdbtMappingService;
    private CustomerInformationService customerInformationService;
    private CustomerBasicConsultationService customerBasicConsultationService;

    public CallService(JwtUtil jwtUtil, UserBaseService userBaseService, CompanyInfomationService companyInfomationService, MemberInfomationService memberInfomationService, UserCdbtMappingService userCdbtMappingService, CustomerInformationService customerInformationService, CustomerBasicConsultationService customerBasicConsultationService) {
        this.jwtUtil = jwtUtil;
        this.userBaseService = userBaseService;
        this.companyInfomationService = companyInfomationService;
        this.memberInfomationService = memberInfomationService;
        this.userCdbtMappingService = userCdbtMappingService;
        this.customerInformationService = customerInformationService;
        this.customerBasicConsultationService = customerBasicConsultationService;
    }

    @Transactional
    public List<Map<String, String>> getCustDbList (HttpServletRequest request) throws Exception{
        List<Map<String, String>> result = new ArrayList<>();
        HttpSessionUtil httpSessionUtil = new HttpSessionUtil(request.getSession(false));

        int userId = jwtUtil.getUserId(httpSessionUtil.getAttribute("jwt").toString());

        userCdbtMappingService.getUserCdbtListByUserId(userId).forEach(dto -> {
            switch (dto.getCdbtCode()) {
                case "LPGE" :
                    List<LpgeCodeDto> lpgeCodeDtoList = CommonCodeCache.getLpgeCodes().stream()
                            .filter(LpgeCodeDto::getUseYn)
                            .filter(code -> !code.getDelYn())
                            .filter(code -> code.getCodeFullName().equals(dto.getCdbtLowCode()))
                            .collect(Collectors.toList());
                    lpgeCodeDtoList.forEach(lpge -> {
                        Map<String, String> resultMap = new HashMap<>();
                        resultMap.put("dbName", lpge.getCodeValue());
                        resultMap.put("code", lpge.getCodeFullName());
                        result.add(resultMap);
                    });
                    break;
                default:
                    break;
            }
        });

        return result;
    }

    @Transactional
    public List<Map<String, String>> getEmployeeList(HttpServletRequest request) throws Exception {
        List<Map<String, String>> result = new ArrayList<>();
        HttpSessionUtil httpSessionUtil = new HttpSessionUtil(request.getSession(false));

        int userId = jwtUtil.getUserId(httpSessionUtil.getAttribute("jwt").toString());

        UserBaseDto userDto = userBaseService.getUserBaseById(userId);

        List<String> companyCodes = CommonCodeCache.getCompanyCode().stream()
                .map(dto -> {
                    return dto.getCodeFullName();
                })
                .collect(Collectors.toList());

        int companyId;

        if(companyCodes.contains(userDto.getUserType()) || userDto.getUserType().equals("USTY_MAST")){
            companyId = companyInfomationService.getCompanyInfomationByUserId(userId).getIdx();
        }else{
            companyId = memberInfomationService.getMemberInfomationByUserId(userId).getCompanyId();
        }

        memberInfomationService.getMemberInfomationDtoListByCompanyId(companyId).forEach(dto -> {
            UserBaseDto userBaseById = userBaseService.getUserBaseById(dto.getUserId());
            if(!userBaseById.getDelYn()){
                Map<String, String> resultMap = new HashMap<>();
                resultMap.put("userId", String.valueOf(userBaseById.getIdx()));
                resultMap.put("name", dto.getName());
                resultMap.put("role", dto.getRole());
                resultMap.put("useYn", userBaseById.getUseYn().toString());

                result.add(resultMap);
            }
        });

        return result;
    }

    @Transactional
    public List<Map<String, String>> getCustList(CustListDto custListDto) throws Exception {
        List<Map<String, String>> result = new ArrayList<>();
        EncryptionUtil encryptionUtil = new EncryptionUtil();

        customerInformationService.getCustomerInformationByCdbtLowCode(custListDto.getCdbtCode()).stream()
                .filter(CustomerInformationDto::getUseYn)
                .filter(dto -> !dto.getDelYn())
                .collect(Collectors.toList())
                .forEach(dto -> {
                    Map<String, String> resultMap = new HashMap<>();
                    customerBasicConsultationService.getCustomerBasicConsultationByCustId(dto.getIdx()).stream()
                            .filter(custInfo -> custInfo.getDeleteId() == null)
                            .collect(Collectors.toList())
                            .forEach(custInfo -> {
                                resultMap.put(custInfo.getKey(), Optional.ofNullable(encryptionUtil.AES256decrypt(custInfo.getValue())).orElse(""));
                            });
                    result.add(resultMap);
                });

        return result;
    }
}
