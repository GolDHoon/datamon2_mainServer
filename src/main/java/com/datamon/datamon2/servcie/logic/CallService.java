package com.datamon.datamon2.servcie.logic;

import com.datamon.datamon2.common.CommonCodeCache;
import com.datamon.datamon2.dto.input.call.CustListDto;
import com.datamon.datamon2.dto.input.call.SaveMultiOutBoundDto;
import com.datamon.datamon2.dto.input.call.SaveSingleOutBoundDto;
import com.datamon.datamon2.dto.input.call.UpdateCdbsCodeDto;
import com.datamon.datamon2.dto.repository.*;
import com.datamon.datamon2.entity.OutboundEntity;
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
    private LandingPageSettingService landingPageSettingService;
    private OutboundService outboundService;
    private OutboundHistoryService outboundHistoryService;

    public CallService(JwtUtil jwtUtil, UserBaseService userBaseService, CompanyInfomationService companyInfomationService, MemberInfomationService memberInfomationService, UserCdbtMappingService userCdbtMappingService, CustomerInformationService customerInformationService, CustomerBasicConsultationService customerBasicConsultationService, LandingPageSettingService landingPageSettingService, OutboundService outboundService, OutboundHistoryService outboundHistoryService) {
        this.jwtUtil = jwtUtil;
        this.userBaseService = userBaseService;
        this.companyInfomationService = companyInfomationService;
        this.memberInfomationService = memberInfomationService;
        this.userCdbtMappingService = userCdbtMappingService;
        this.customerInformationService = customerInformationService;
        this.customerBasicConsultationService = customerBasicConsultationService;
        this.landingPageSettingService = landingPageSettingService;
        this.outboundService = outboundService;
        this.outboundHistoryService = outboundHistoryService;
    }

    @Transactional
    public List<Map<String, String>> getCustDbList (HttpServletRequest request) throws Exception{
        List<Map<String, String>> result = new ArrayList<>();
        HttpSessionUtil httpSessionUtil = new HttpSessionUtil(request.getSession(false));

        int userId = jwtUtil.getUserId(httpSessionUtil.getAttribute("jwt").toString());

        List<String> distributionAllowedCodes = CommonCodeCache.getDistributionAllowedCodes().stream()
                .map(dto -> dto.getCodeFullName())
                .collect(Collectors.toList());

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

                        int checker = customerInformationService.getCustomerInformationByCdbtLowCode(lpge.getCodeFullName()).stream()
                                .filter(custInfo -> distributionAllowedCodes.contains(custInfo.getCdbsCode()))
                                .collect(Collectors.toList()).size();
                        if(checker > 0){
                            result.add(resultMap);
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
    public List<Map<String, Object>> getEmployeeList(HttpServletRequest request) throws Exception {
        List<Map<String, Object>> result = new ArrayList<>();
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

        EncryptionUtil encryptionUtil = new EncryptionUtil();

        memberInfomationService.getMemberInfomationDtoListByCompanyId(companyId).forEach(dto -> {
            UserBaseDto userBaseById = userBaseService.getUserBaseById(dto.getUserId());
            if(!userBaseById.getDelYn()){
                Map<String, Object> resultMap = new HashMap<>();
                resultMap.put("userId", String.valueOf(userBaseById.getIdx()));
                resultMap.put("name", dto.getName());
                resultMap.put("role", dto.getRole());
                resultMap.put("useYn", userBaseById.getUseYn().toString());
                List<Map<String, Object>> outboundList = new ArrayList<>();
                outboundService.getOutboundByUserId(userBaseById.getIdx()).forEach(outbound -> {
                    Map<String, Object> outboundListMap = new HashMap<>();
                    List<CustomerBasicConsultationDto> customerBasicConsultationDtoList = customerBasicConsultationService.getCustomerBasicConsultationByCustId(outbound.getCustId()).stream()
                            .filter(consultation -> consultation.getDeleteId() == null)
                            .collect(Collectors.toList());

                    Map<String, Object> custInfo = new HashMap<>();

                    customerBasicConsultationDtoList.forEach(consultationInfo -> {
                        custInfo.put(consultationInfo.getKey(), Optional.ofNullable(encryptionUtil.AES256decrypt(consultationInfo.getValue())).orElse(""));
                    });

                    outboundListMap.put("custInfo", custInfo);

                    List<LandingPageSettingDto> landingPageSettingDto = landingPageSettingService.getLandingPageSettingListByLpgeCode(customerInformationService.getCustomerInformationById(outbound.getCustId()).getCdbtLowCode());

                    outboundListMap.put("setting", landingPageSettingDto);

                    outboundList.add(outboundListMap);
                });
                resultMap.put("outboundList", outboundList);


                result.add(resultMap);
            }
        });

        return result;
    }

    @Transactional
    public List<Map<String, Object>> getCustList(CustListDto custListDto) throws Exception {
        List<Map<String, Object>> result = new ArrayList<>();
        EncryptionUtil encryptionUtil = new EncryptionUtil();
        String cdbtCode = custListDto.getCdbtCode().replace("CDBT_", "").substring(0, 4);

        List<String> distributionAllowedCodes = CommonCodeCache.getDistributionAllowedCodes().stream()
                .map(dto -> dto.getCodeFullName())
                .collect(Collectors.toList());

        customerInformationService.getCustomerInformationByCdbtLowCode(custListDto.getCdbtCode()).stream()
                .filter(CustomerInformationDto::getUseYn)
                .filter(dto -> !dto.getDelYn())
                .filter(dto -> distributionAllowedCodes.contains(dto.getCdbsCode()))
                .collect(Collectors.toList())
                .forEach(dto -> {
                    Map<String, Object> resultMap = new HashMap<>();
                    List<Map<String, String>> resultList = new ArrayList<>();

                    switch (cdbtCode){
                        case "LPGE":
                            List<LandingPageSettingDto> landingPageSettingDtoList = landingPageSettingService.getLandingPageSettingListByLpgeCode(custListDto.getCdbtCode());
                            List<String> columnNameList = landingPageSettingDtoList.stream()
                                    .map(landingPageSettingDto -> landingPageSettingDto.getColumnName())
                                    .collect(Collectors.toList());
                            customerBasicConsultationService.getCustomerBasicConsultationByCustId(dto.getIdx()).stream()
                                    .filter(custInfo -> custInfo.getDeleteId() == null)
                                    .filter(custInfo -> columnNameList.contains(custInfo.getKey()))
                                    .collect(Collectors.toList())
                                    .forEach(custInfo -> {
                                        Map<String, String> resultMapDetail = new HashMap<>();

                                        LandingPageSettingDto landingPageSettingDto = landingPageSettingDtoList.stream()
                                                .filter(landingPageSetting -> landingPageSetting.getColumnName().equals(custInfo.getKey()))
                                                .findFirst().orElse(new LandingPageSettingDto());

                                        resultMapDetail.put("key", custInfo.getKey());
                                        resultMapDetail.put("value", Optional.ofNullable(encryptionUtil.AES256decrypt(custInfo.getValue())).orElse(""));
                                        resultMapDetail.put("displayOrdering", Optional.ofNullable(landingPageSettingDto.getDisplayOrderingNumber()).orElse(-1L).toString());
                                        resultMapDetail.put("displayOrderingYn", Optional.ofNullable(landingPageSettingDto.getDisplayOrderingYn()).orElse(false).toString());
                                        resultList.add(resultMapDetail);
                                    });
                            break;
                        default:
                            break;
                    }
                    resultMap.put("detail", resultList);
                    resultMap.put("status", dto.getCdbsCode());
                    resultMap.put("idx", dto.getIdx());
                    result.add(resultMap);
                });

        return result;
    }

    @Transactional
    public String saveSingleOutBound(HttpServletRequest request, SaveSingleOutBoundDto saveSingleOutBoundDto) throws Exception{
        HttpSessionUtil httpSessionUtil = new HttpSessionUtil(request.getSession(false));

        int userId = jwtUtil.getUserId(httpSessionUtil.getAttribute("jwt").toString());

        OutboundDto outboundDto = outboundService.getOutboundByCustId(saveSingleOutBoundDto.getCustId());
        OutboundHistoryDto outboundHistoryDto = new OutboundHistoryDto();

        outboundDto.setCustId(saveSingleOutBoundDto.getCustId());
        outboundHistoryDto.setCustId(saveSingleOutBoundDto.getCustId());
        outboundDto.setUserId(saveSingleOutBoundDto.getUserId());
        outboundHistoryDto.setUserId(saveSingleOutBoundDto.getUserId());
        outboundDto.setOrderMemo(saveSingleOutBoundDto.getOrderMemo());
        outboundHistoryDto.setOrderMemo(saveSingleOutBoundDto.getOrderMemo());

        if(outboundDto.getIdx() == null){
            outboundDto.setTelColumn("");
            outboundHistoryDto.setTelColumn("");
            outboundDto.createId();
        }
        outboundHistoryDto.setOriginalIdx(outboundDto.getIdx());
        outboundHistoryDto.setSort(outboundHistoryService.getOutboundHistoryByOriginalIdx(outboundHistoryDto.getOriginalIdx()).size());
        outboundHistoryDto.setSaveReason("outbound 분배");
        outboundHistoryDto.createId();
        outboundHistoryDto.create(userId);

        outboundService.save(outboundDto);
        outboundHistoryService.save(outboundHistoryDto);

        CustomerInformationDto customerInformationDto = customerInformationService.getCustomerInformationById(saveSingleOutBoundDto.getCustId());
        customerInformationDto.distribution();
        customerInformationDto.setStatusChangeReason("outbound 분배");
        customerInformationDto.modify(userId);
        customerInformationService.save(customerInformationDto);

        return "success";
    }

    @Transactional
    public String saveMultiOutBound(HttpServletRequest request, SaveMultiOutBoundDto saveMultiOutBoundDto) throws Exception{
        HttpSessionUtil httpSessionUtil = new HttpSessionUtil(request.getSession(false));

        int userId = jwtUtil.getUserId(httpSessionUtil.getAttribute("jwt").toString());

        saveMultiOutBoundDto.getCustId().forEach(custId -> {
            OutboundDto outboundDto = outboundService.getOutboundByCustId(custId);
            OutboundHistoryDto outboundHistoryDto = new OutboundHistoryDto();

            outboundDto.setCustId(custId);
            outboundHistoryDto.setCustId(custId);
            outboundDto.setUserId(saveMultiOutBoundDto.getUserId());
            outboundHistoryDto.setUserId(saveMultiOutBoundDto.getUserId());
            outboundDto.setOrderMemo(saveMultiOutBoundDto.getOrderMemo());
            outboundHistoryDto.setOrderMemo(saveMultiOutBoundDto.getOrderMemo());

            if(outboundDto.getIdx() == null){
                outboundDto.setTelColumn("");
                outboundHistoryDto.setTelColumn("");
                outboundDto.createId();
            }
            outboundHistoryDto.setOriginalIdx(outboundDto.getIdx());
            outboundHistoryDto.setSort(outboundHistoryService.getOutboundHistoryByOriginalIdx(outboundHistoryDto.getOriginalIdx()).size());
            outboundHistoryDto.setSaveReason("outbound 분배");
            outboundHistoryDto.createId();
            outboundHistoryDto.create(userId);

            outboundService.save(outboundDto);
            outboundHistoryService.save(outboundHistoryDto);

            CustomerInformationDto customerInformationDto = customerInformationService.getCustomerInformationById(custId);
            customerInformationDto.distribution();
            customerInformationDto.setStatusChangeReason("outbound 분배");
            customerInformationDto.modify(userId);
            customerInformationService.save(customerInformationDto);
        });

        return "success";
    }

    @Transactional
    public List<Map<String, Object>> getOutBoundList (HttpServletRequest request) throws Exception{
        List<Map<String, Object>> result = new ArrayList<>();
        HttpSessionUtil httpSessionUtil = new HttpSessionUtil(request.getSession(false));

        int userId = jwtUtil.getUserId(httpSessionUtil.getAttribute("jwt").toString());

        EncryptionUtil encryptionUtil = new EncryptionUtil();

        outboundService.getOutboundByUserId(userId).forEach(outbound -> {
            Map<String, Object> resultMap = new HashMap<>();

            resultMap.put("idx", outbound.getIdx());
            resultMap.put("custIdx", outbound.getCustId());
            resultMap.put("order", outbound.getOrderMemo());
            resultMap.put("memo", outbound.getMemo());
            resultMap.put("callbackDate", outbound.getScheduledCallbackDate());
            resultMap.put("conversionDate", outbound.getScheduledConversionDate());

            List<CustomerBasicConsultationDto> customerBasicConsultationDtoList = customerBasicConsultationService.getCustomerBasicConsultationByCustId(outbound.getCustId()).stream()
                    .filter(consultation -> consultation.getDeleteId() == null)
                    .collect(Collectors.toList());

            Map<String, Object> cardDetail = new HashMap<>();

            customerBasicConsultationDtoList.forEach(consultationInfo -> {
                cardDetail.put(consultationInfo.getKey(), Optional.ofNullable(encryptionUtil.AES256decrypt(consultationInfo.getValue())).orElse(""));
            });

            resultMap.put("cardDetail", cardDetail);

            CustomerInformationDto customerInformationDto = customerInformationService.getCustomerInformationById(outbound.getCustId());
            CustomerInformationDto finalCustInfo;
            if(customerInformationDto.getCdbsCode().equals("CDBS_ICST")){
                customerInformationDto.setCdbsCode("CDBS_CWLG");
                customerInformationDto.modify(userId);
                finalCustInfo = customerInformationService.save(customerInformationDto);
            }else{
                finalCustInfo = customerInformationDto;
            }

            resultMap.put("상태", CommonCodeCache.getCdbsCodes().stream()
                    .filter(code -> code.getCodeFullName().equals(finalCustInfo.getCdbsCode()))
                    .map(code -> code.getCodeValue())
                    .findFirst().orElse(""));


            resultMap.put("cdbs", finalCustInfo.getCdbsCode());

            resultMap.put("품질", CommonCodeCache.getCdbqCodes().stream()
                    .filter(code -> code.getCodeFullName().equals(finalCustInfo.getCdbqCode()))
                    .map(code -> code.getCodeValue())
                    .findFirst().orElse(""));

            resultMap.put("cdbq", finalCustInfo.getCdbqCode());

            List<LandingPageSettingDto> landingPageSettingDtoList = landingPageSettingService.getLandingPageSettingListByLpgeCode(finalCustInfo.getCdbtLowCode()).stream()
                    .sorted(Comparator.comparing(LandingPageSettingDto::getDisplayOrderingNumber))
                    .collect(Collectors.toList());

            resultMap.put("settings", landingPageSettingDtoList);

            result.add(resultMap);
        });

        return result;
    }

    @Transactional
    public String updateCdbsCode(HttpServletRequest request, UpdateCdbsCodeDto updateCdbsCodeDto) throws Exception{
        HttpSessionUtil httpSessionUtil = new HttpSessionUtil(request.getSession(false));

        int userId = jwtUtil.getUserId(httpSessionUtil.getAttribute("jwt").toString());

        CustomerInformationDto customerInformationDto = customerInformationService.getCustomerInformationById(updateCdbsCodeDto.getCustId());
        customerInformationDto.setCdbsCode(updateCdbsCodeDto.getCdbsCode());
        customerInformationDto.modify(userId);
        customerInformationService.save(customerInformationDto);

        return "success";
    }
}
