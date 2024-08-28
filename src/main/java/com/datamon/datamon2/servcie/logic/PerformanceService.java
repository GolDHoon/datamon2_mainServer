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
    public List<Map<String, Object>> getCrmCompanyPerformance(HttpServletRequest request, String companyId) throws Exception{
        List<Map<String, Object>> result = new ArrayList<>();
        HttpSessionUtil httpSessionUtil = new HttpSessionUtil(request.getSession(false));

        int userId = jwtUtil.getUserId(httpSessionUtil.getAttribute("jwt").toString());
        List<String> companyCode = CommonCodeCache.getCompanyCode().stream().map(code -> code.getCodeFullName()).collect(Collectors.toList());
        CompanyInfomationDto companyInfomationDto;

        if(companyCode.contains(userBaseService.getUserBaseById(userId).getUserType())){
            companyInfomationDto = companyInfomationService.getCompanyInfomationByUserId(userId);
        }else{
            UserBaseDto userBaseDto = userBaseService.getUserBaseByUserId(companyId).stream()
                    .filter(user -> "USTY_MAST".equals(user.getUserType()))
                    .findFirst().orElse(new UserBaseDto());
            companyInfomationDto = companyInfomationService.getCompanyInfomationByUserId(userBaseDto.getIdx());
        }

        memberInfomationService.getMemberInfomationDtoListByCompanyId(companyInfomationDto.getIdx()).forEach(member -> {
            Map<String, Object> resultMap = new LinkedHashMap<>();
            resultMap.put("name", member.getName());
            resultMap.put("index", member.getUserId());
            resultMap.put("role", member.getRole());
            resultMap.put("total", 0);
            resultMap.put("quality", new HashMap<String, Object>());
            resultMap.put("status", new HashMap<String, Object>());

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
                                    .filter(map -> outboundHistoryDto.getUserId().equals(Integer.parseInt(map.get("index").toString())))
                                    .findFirst().orElse(new LinkedHashMap<>());

                            String cdbsMapKey = CommonCodeCache.getCdbsCodes().stream()
                                    .filter(code -> code.getCodeFullName().equals(custInfo.getCdbsCode()))
                                    .findFirst().orElse(new CdbsCodeDto()).getCodeValue();

                            String cdbqMapKey = CommonCodeCache.getCdbqCodes().stream()
                                    .filter(code -> code.getCodeFullName().equals(custInfo.getCdbqCode()))
                                    .findFirst().orElse(new CdbqCodeDto()).getCodeValue();

                            HashMap<String, Object> status = (HashMap<String, Object>) resultMap.get("status");
                            HashMap<String, Object> quality = (HashMap<String, Object>) resultMap.get("quality");

                            if(resultMap.get("index") != null){
                                if(status.get(cdbsMapKey) != null){
                                    status.put(cdbsMapKey, ((int) status.get(cdbsMapKey)) + 1);
                                }else {
                                    status.put(cdbsMapKey, 1);
                                }

                                if(quality.get(cdbqMapKey) != null){
                                    quality.put(cdbqMapKey, ((int) quality.get(cdbqMapKey)) + 1);
                                }else {
                                    quality.put(cdbqMapKey, 1);
                                }

                                resultMap.put("total", ((int) resultMap.get("total")) + 1);
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
        result.put("total", 0);
        result.put("quality", new HashMap<String, Object>());
        result.put("status", new HashMap<String, Object>());


        List<String> allowUstyCods = new ArrayList<>();
        allowUstyCods.add("USTY_CRAC");
        allowUstyCods.add("USTY_MAST");

        UserBaseDto userBaseDto = userBaseService.getUserBaseByUserId(companyId).stream()
                .filter(user -> allowUstyCods.contains(user.getUserType()))
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
                                String cdbsMapKey = CommonCodeCache.getCdbsCodes().stream()
                                        .filter(code -> code.getCodeFullName().equals(custInfo.getCdbsCode()))
                                        .findFirst().orElse(new CdbsCodeDto()).getCodeValue();

                                String cdbqMapKey = CommonCodeCache.getCdbqCodes().stream()
                                        .filter(code -> code.getCodeFullName().equals(custInfo.getCdbqCode()))
                                        .findFirst().orElse(new CdbqCodeDto()).getCodeValue();

                                HashMap<String, Object> status = (HashMap<String, Object>) result.get("status");
                                HashMap<String, Object> quality = (HashMap<String, Object>) result.get("quality");

                                if(status.get(cdbsMapKey) != null){
                                    status.put(cdbsMapKey, ((int) status.get(cdbsMapKey)) + 1);
                                }else {
                                    status.put(cdbsMapKey, 1);
                                }

                                if(quality.get(cdbqMapKey) != null){
                                    quality.put(cdbqMapKey, ((int) quality.get(cdbqMapKey)) + 1);
                                }else {
                                    quality.put(cdbqMapKey, 1);
                                }
                                result.put("total", ((int) result.get("total")) + 1);
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