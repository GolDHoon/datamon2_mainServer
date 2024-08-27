package com.datamon.datamon2.servcie.logic;

import com.datamon.datamon2.common.CommonCodeCache;
import com.datamon.datamon2.dto.repository.*;
import com.datamon.datamon2.servcie.repository.*;
import com.datamon.datamon2.util.HttpSessionUtil;
import com.datamon.datamon2.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PerformanceService {
    private JwtUtil jwtUtil;
    private CustomerInformationService customerInformationService;
    private CustomerBasicConsultationService customerBasicConsultationService;
    private UserCdbtMappingService userCdbtMappingService;
    private CompanyInfomationService companyInfomationService;
    private MemberInfomationService memberInfomationService;
    private UserBaseService userBaseService;
    private OutboundHistoryService outboundHistoryService;

    public PerformanceService(JwtUtil jwtUtil, CustomerInformationService customerInformationService, CustomerBasicConsultationService customerBasicConsultationService, UserCdbtMappingService userCdbtMappingService, CompanyInfomationService companyInfomationService, MemberInfomationService memberInfomationService, UserBaseService userBaseService, OutboundHistoryService outboundHistoryService) {
        this.jwtUtil = jwtUtil;
        this.customerInformationService = customerInformationService;
        this.customerBasicConsultationService = customerBasicConsultationService;
        this.userCdbtMappingService = userCdbtMappingService;
        this.companyInfomationService = companyInfomationService;
        this.memberInfomationService = memberInfomationService;
        this.userBaseService = userBaseService;
        this.outboundHistoryService = outboundHistoryService;
    }

    @Transactional
    public List<Map<String, Object>> getAdPerformance(HttpServletRequest request) throws Exception{
        List<Map<String, Object>> result = new ArrayList<>();
        HttpSessionUtil httpSessionUtil = new HttpSessionUtil(request.getSession(false));

        int userId = jwtUtil.getUserId(httpSessionUtil.getAttribute("jwt").toString());

        userCdbtMappingService.getUserCdbtListByUserId(userId).forEach(mapping -> {
            Map<String, Object> resultMap = new HashMap<>();

            switch (mapping.getCdbtLowCode().substring(0, 9)) {
                case "CDBT_LPGE":
                    List<CustomerInformationDto> customerInformationDtoList = customerInformationService.getCustomerInformationByCdbtLowCode(mapping.getCdbtLowCode()).stream()
                            .filter(custInfo -> !custInfo.getDelYn())
                            .collect(Collectors.toList());
                    Map<String, Object> resultMapItemTotal = new HashMap<>();

                    resultMapItemTotal.put("totalData", customerInformationDtoList.size());
                    CommonCodeCache.getCdbqCodes().forEach(code -> {
                        resultMapItemTotal.put(code.getCodeValue(), customerInformationDtoList.stream().filter(info -> info.getCdbqCode().equals(code.getCodeFullName())).collect(Collectors.toList()).size());
                    });
                    resultMap.put("total", resultMapItemTotal);

                    List<CustomerInformationDto> minusOneWeekList = customerInformationDtoList.stream()
                            .filter(info -> info.getCreateDate().isAfter(LocalDateTime.now().minusWeeks(1l)))
                            .collect(Collectors.toList());
                    Map<String, Object> resultMapItemMinusOneWeek = new HashMap<>();
                    resultMapItemMinusOneWeek.put("totalData", minusOneWeekList.size());
                    CommonCodeCache.getCdbqCodes().forEach(code -> {
                        resultMapItemMinusOneWeek.put(code.getCodeValue(), minusOneWeekList.stream().filter(info -> info.getCdbqCode().equals(code.getCodeFullName())).collect(Collectors.toList()).size());
                    });
                    resultMap.put("minusOneWeek", resultMapItemMinusOneWeek);

                    List<CustomerInformationDto> minusOneMonthList = customerInformationDtoList.stream()
                            .filter(info -> info.getCreateDate().isAfter(LocalDateTime.now().minusMonths(1l)))
                            .collect(Collectors.toList());
                    Map<String, Object> resultMapItemMinusOneMonth = new HashMap<>();
                    resultMapItemMinusOneMonth.put("totalData", minusOneMonthList.size());
                    CommonCodeCache.getCdbqCodes().forEach(code -> {
                        resultMapItemMinusOneMonth.put(code.getCodeValue(), minusOneMonthList.stream().filter(info -> info.getCdbqCode().equals(code.getCodeFullName())).collect(Collectors.toList()).size());
                    });
                    resultMap.put("minusOneMonth", resultMapItemMinusOneMonth);

                    List<CustomerInformationDto> minusOneYearList = customerInformationDtoList.stream()
                            .filter(info -> info.getCreateDate().isAfter(LocalDateTime.now().minusYears(1l)))
                            .collect(Collectors.toList());
                    Map<String, Object> resultMapItemMinusOneYear = new HashMap<>();
                    resultMapItemMinusOneYear.put("totalData", minusOneYearList.size());
                    CommonCodeCache.getCdbqCodes().forEach(code -> {
                        resultMapItemMinusOneYear.put(code.getCodeValue(), minusOneYearList.stream().filter(info -> info.getCdbqCode().equals(code.getCodeFullName())).collect(Collectors.toList()).size());
                    });
                    resultMap.put("minusOneYear", resultMapItemMinusOneYear);

                    resultMap.put("list", customerInformationDtoList.stream()
                                                                    .map(custInfo -> {
                                                                        custInfo.setCdbqCode(CommonCodeCache.getCdbqCodes().stream()
                                                                                .filter(code -> custInfo.getCdbqCode().equals(code.getCodeFullName()))
                                                                                .findFirst().orElse(new CdbqCodeDto()).getCodeValue());

                                                                        custInfo.setCdbsCode(CommonCodeCache.getCdbsCodes().stream()
                                                                                .filter(code -> custInfo.getCdbsCode().equals(code.getCodeFullName()))
                                                                                .findFirst().orElse(new CdbsCodeDto()).getCodeValue());

                                                                        return custInfo;
                                                                    })
                                                                    .collect(Collectors.toList()));

                    LpgeCodeDto lpgeCodeDto = CommonCodeCache.getLpgeCodes().stream()
                            .filter(code -> code.getCodeFullName().equals(mapping.getCdbtLowCode()))
                            .findFirst().orElse(new LpgeCodeDto());

                    resultMap.put("cdbtCode", lpgeCodeDto.getCodeFullName());
                    resultMap.put("url", lpgeCodeDto.getCodeValue());
                    result.add(resultMap);
                    break;
                default:
                    break;
            }

        });

        return result;
    }

    @Transactional
    public List<Map<String, Object>> getCrmCompanyPerformance(HttpServletRequest request) throws Exception{
        List<Map<String, Object>> result = new ArrayList<>();
        HttpSessionUtil httpSessionUtil = new HttpSessionUtil(request.getSession(false));

        int userId = jwtUtil.getUserId(httpSessionUtil.getAttribute("jwt").toString());

        CompanyInfomationDto companyInfomationDto = companyInfomationService.getCompanyInfomationByUserId(userId);
        memberInfomationService.getMemberInfomationDtoListByCompanyId(companyInfomationDto.getIdx()).forEach(member -> {
            Map<String, Object> resultMap = new LinkedHashMap<>();
            resultMap.put("name", member.getName());
            resultMap.put("index", member.getIdx());
            resultMap.put("role", member.getRole());

            result.add(resultMap);
        });

        userCdbtMappingService.getUserCdbtListByUserId(userId).forEach(mapping -> {

            switch (mapping.getCdbtLowCode().substring(0, 9)) {
                case "CDBT_LPGE":
                    customerInformationService.getCustomerInformationByCdbtLowCode(mapping.getCdbtLowCode()).forEach(custInfo -> {
                        OutboundHistoryDto outboundHistoryDto = outboundHistoryService.getOutboundHistoryByCustId(custInfo.getIdx()).stream()
                                .max(Comparator.comparing(OutboundHistoryDto::getSort))
                                .orElse(new OutboundHistoryDto());

                        if (outboundHistoryDto.getIdx() != null){
                            Map<String, Object> resultMap = result.stream()
                                    .filter(map -> map.get("index").equals(outboundHistoryDto.getUserId()))
                                    .findFirst().orElse(new LinkedHashMap<>());

                            if(resultMap.get("index") != null){
                                if(resultMap.get(custInfo.getCdbsCode()) != null){
                                    resultMap.put(custInfo.getCdbsCode(), ((int) resultMap.get(custInfo.getCdbsCode())) + 1);
                                }else {
                                    resultMap.put(custInfo.getCdbsCode(), 1);
                                }

                                if(resultMap.get(custInfo.getCdbqCode()) != null){
                                    resultMap.put(custInfo.getCdbqCode(), ((int) resultMap.get(custInfo.getCdbqCode())) + 1);
                                }else {
                                    resultMap.put(custInfo.getCdbqCode(), 1);
                                }
                            }
                        }
                    });

                    break;
                default:
                    break;
            }
        });

        return result;
    }

    @Transactional
    public Map<String, Object> getCrmMemberPerformance(HttpServletRequest request, String companyId) throws Exception{
        Map<String, Object> result = new HashMap<>();
        HttpSessionUtil httpSessionUtil = new HttpSessionUtil(request.getSession(false));

        int userId = jwtUtil.getUserId(httpSessionUtil.getAttribute("jwt").toString());

        MemberInfomationDto memberInfomationDto = memberInfomationService.getMemberInfomationByUserId(userId);

        result.put("name", memberInfomationDto.getName());
        result.put("index", memberInfomationDto.getIdx());
        result.put("role", memberInfomationDto.getRole());

        UserBaseDto userBaseDto = userBaseService.getUserBaseByUserId(companyId).stream()
                .filter(user -> user.getUserType().equals("USTY_CRAC"))
                .findFirst().orElse(new UserBaseDto());

        if(userBaseDto.getIdx() != null){
            userCdbtMappingService.getUserCdbtListByUserId(userBaseDto.getIdx()).forEach(mapping -> {
                switch (mapping.getCdbtLowCode().substring(0, 9)) {
                    case "CDBT_LPGE":
                        customerInformationService.getCustomerInformationByCdbtLowCode(mapping.getCdbtLowCode()).forEach(custInfo -> {
                            OutboundHistoryDto outboundHistoryDto = outboundHistoryService.getOutboundHistoryByCustId(custInfo.getIdx()).stream()
                                    .max(Comparator.comparing(OutboundHistoryDto::getSort))
                                    .orElse(new OutboundHistoryDto());

                            if(outboundHistoryDto.getUserId() == memberInfomationDto.getUserId()){
                                if(result.get(custInfo.getCdbsCode()) != null){
                                    result.put(custInfo.getCdbsCode(), ((int) result.get(custInfo.getCdbsCode())) + 1);
                                }else {
                                    result.put(custInfo.getCdbsCode(), 1);
                                }

                                if(result.get(custInfo.getCdbqCode()) != null){
                                    result.put(custInfo.getCdbqCode(), ((int) result.get(custInfo.getCdbqCode())) + 1);
                                }else {
                                    result.put(custInfo.getCdbqCode(), 1);
                                }
                            }
                        });
                        break;
                    default:
                        break;
                }
            });
        }

        return result;
    }
}