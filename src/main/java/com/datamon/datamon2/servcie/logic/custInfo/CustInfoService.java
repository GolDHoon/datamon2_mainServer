package com.datamon.datamon2.servcie.logic.custInfo;

import com.datamon.datamon2.common.CommonCodeCache;
import com.datamon.datamon2.dto.input.custInfo.CustInfoDto;
import com.datamon.datamon2.dto.input.custInfo.ModifyCustInfoDto;
import com.datamon.datamon2.dto.output.custInfo.ColumnInfo;
import com.datamon.datamon2.dto.output.custInfo.GetCustDbCodeListOutputDto;
import com.datamon.datamon2.dto.output.custInfo.GetCustInfoListOutputDto;
import com.datamon.datamon2.dto.output.custInfo.RowInfo;
import com.datamon.datamon2.dto.repository.*;
import com.datamon.datamon2.servcie.logic.UserService;
import com.datamon.datamon2.servcie.repository.*;
import com.datamon.datamon2.util.DateTimeUtil;
import com.datamon.datamon2.util.EncryptionUtil;
import com.datamon.datamon2.util.HttpSessionUtil;
import com.datamon.datamon2.util.JwtUtil;
import com.mysql.cj.result.Row;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CustInfoService {
    private UserBaseService userBaseService;
    private DateTimeUtil dateTimeUtil = new DateTimeUtil();
    private CustomerInformationService customerInformationService;
    private CustomerBasicConsultationService customerBasicConsultationService;
    private OutboundService outboundService;
    private UserCdbtMappingService userCdbtMappingService;
    private JwtUtil jwtUtil;

    public CustInfoService(CustomerInformationService customerInformationService, CustomerBasicConsultationService customerBasicConsultationService, JwtUtil jwtUtil, UserBaseService userBaseService, UserBaseService userBaseService1, OutboundService outboundService, UserCdbtMappingService userCdbtMappingService) {
        this.customerInformationService = customerInformationService;
        this.customerBasicConsultationService = customerBasicConsultationService;
        this.jwtUtil = jwtUtil;
        this.userBaseService = userBaseService1;
        this.outboundService = outboundService;
        this.userCdbtMappingService = userCdbtMappingService;
    }

    @Transactional
    public Map<String, Object> getCustInfoList(HttpServletRequest request, String custDBType, String custDBCode) throws Exception{
        HttpSessionUtil httpSessionUtil = new HttpSessionUtil(request.getSession(false));
        Map<String, Object> result = new HashMap<>();
        int userId = jwtUtil.getUserId(httpSessionUtil.getAttribute("jwt").toString());
        GetCustInfoListOutputDto getCustInfoListOutputDto = new GetCustInfoListOutputDto();

        getCustInfoListOutputDto.setColumnInfoList(new ArrayList<>());
        getCustInfoListOutputDto.setDataList(new ArrayList<>());

        List<CustomerInformationDto> custInfoDtoList = customerInformationService.getCustomerInformationByCdbtLowCode(custDBCode).stream()
                .filter(CustomerInformationDto::getUseYn)
                .filter(dto -> !dto.getDelYn())
                .sorted(Comparator.comparing(CustomerInformationDto::getCreateDate).reversed())
                .collect(Collectors.toList());

        List<String> custInfoIdxList = custInfoDtoList.stream()
                .map(dto -> {
                    return dto.getIdx();
                })
                .collect(Collectors.toList());

        List<String> customCustInfoKeyList = customerBasicConsultationService.getCustomerBasicConsultationByCustIdList(custInfoIdxList).stream()
                .map(dto -> {
                    return dto.getKey();
                })
                .distinct()
                .collect(Collectors.toList());

        customCustInfoKeyList.forEach(key -> {
            ColumnInfo columnInfo = new ColumnInfo();
            columnInfo.setColumnType("custom");
            columnInfo.setFilterType("text");
            columnInfo.setName(key);
            columnInfo.setKey(key);
            getCustInfoListOutputDto.getColumnInfoList().add(columnInfo);
        });

        ColumnInfo columnInfo = new ColumnInfo();
        columnInfo.setColumnType("basic");
        columnInfo.setFilterType("select");
        columnInfo.setName("상태");
        columnInfo.setKey("cdbsCode");
        getCustInfoListOutputDto.getColumnInfoList().add(columnInfo);

        columnInfo = new ColumnInfo();
        columnInfo.setColumnType("crm");
        columnInfo.setFilterType("text");
        columnInfo.setName("메모");
        columnInfo.setKey("memo");
        getCustInfoListOutputDto.getColumnInfoList().add(columnInfo);

        columnInfo = new ColumnInfo();
        columnInfo.setColumnType("basic");
        columnInfo.setFilterType("text");
        columnInfo.setName("소스");
        columnInfo.setKey("utmSource");
        getCustInfoListOutputDto.getColumnInfoList().add(columnInfo);

        columnInfo = new ColumnInfo();
        columnInfo.setColumnType("basic");
        columnInfo.setFilterType("text");
        columnInfo.setName("매체");
        columnInfo.setKey("utmMedium");
        getCustInfoListOutputDto.getColumnInfoList().add(columnInfo);

        columnInfo = new ColumnInfo();
        columnInfo.setColumnType("basic");
        columnInfo.setFilterType("text");
        columnInfo.setName("캠페인");
        columnInfo.setKey("utmCampaign");
        getCustInfoListOutputDto.getColumnInfoList().add(columnInfo);

        columnInfo = new ColumnInfo();
        columnInfo.setColumnType("basic");
        columnInfo.setFilterType("text");
        columnInfo.setName("키워드");
        columnInfo.setKey("utmTerm");
        getCustInfoListOutputDto.getColumnInfoList().add(columnInfo);

        columnInfo = new ColumnInfo();
        columnInfo.setColumnType("basic");
        columnInfo.setFilterType("text");
        columnInfo.setName("콘텐츠");
        columnInfo.setKey("utmContent");
        getCustInfoListOutputDto.getColumnInfoList().add(columnInfo);

        columnInfo = new ColumnInfo();
        columnInfo.setColumnType("basic");
        columnInfo.setFilterType("text");
        columnInfo.setName("ip");
        columnInfo.setKey("ip");
        getCustInfoListOutputDto.getColumnInfoList().add(columnInfo);

        columnInfo = new ColumnInfo();
        columnInfo.setColumnType("basic");
        columnInfo.setFilterType("date");
        columnInfo.setName("생성일");
        columnInfo.setKey("createDate");
        getCustInfoListOutputDto.getColumnInfoList().add(columnInfo);

        columnInfo = new ColumnInfo();
        columnInfo.setColumnType("basic");
        columnInfo.setFilterType("date");
        columnInfo.setName("최종수정일");
        columnInfo.setKey("modifyDate");
        getCustInfoListOutputDto.getColumnInfoList().add(columnInfo);

        custInfoDtoList.forEach(dto -> {
            OutboundDto outbound = outboundService.getOutboundByCustId(dto.getIdx());

            if(outbound.getIdx() == null){
                outbound = new OutboundDto();
                outbound.setUserId(userId);
                outbound.setCustId(dto.getIdx());
                outbound.setTelColumn("");
                outbound.createId();
                outboundService.save(outbound);
            }

            OutboundDto finalOutbound = outbound;
            Map<String, Object> row = new HashMap<>();
            getCustInfoListOutputDto.getColumnInfoList().forEach(column -> {
                switch (column.getColumnType()){
                    case "basic" : {
                        switch (column.getKey()){
                            case "cdbsCode" :{
                                row.put(column.getKey(), dto.getCdbsCode());
                                break;
                            }
                            case "utmSource" :{
                                row.put(column.getKey(), dto.getUtmSource());
                                break;
                            }
                            case "utmMedium" :{
                                row.put(column.getKey(), dto.getUtmMedium());
                                break;
                            }
                            case "utmCampaign" :{
                                row.put(column.getKey(), dto.getUtmCampaign());
                                break;
                            }
                            case "utmTerm" :{
                                row.put(column.getKey(), dto.getUtmTerm());
                                break;
                            }
                            case "utmContent" :{
                                row.put(column.getKey(), dto.getUtmContent());
                                break;
                            }
                            case "ip" :{
                                row.put(column.getKey(), dto.getIp());
                                break;
                            }
                            case "createDate" :{
                                row.put(column.getKey(), dto.getCreateDate());
                                break;
                            }
                            case "modifyDate" :{
                                row.put(column.getKey(), dto.getModifyDate());
                                break;
                            }
                            default:break;
                        }
                        break;
                    }
                    case "crm": {
                        switch (column.getKey()){
                            case "memo":{
                                row.put(column.getKey(), finalOutbound.getMemo());
                                break;
                            }
                            default:break;
                        }
                        break;
                    }
                    default:break;
                }
            });

            EncryptionUtil encryptionUtil = new EncryptionUtil();
            customerBasicConsultationService.getCustomerBasicConsultationByCustId(dto.getIdx())
                    .forEach(customInfo -> {
                        row.put(customInfo.getKey(), encryptionUtil.AES256decrypt(customInfo.getValue()));
                    });

            getCustInfoListOutputDto.getDataList().add(row);
        });

        result.put("result", "S");
        result.put("output", getCustInfoListOutputDto);

        return result;
    }

    @Transactional
    public Map<String, Object> getCustDBCodeList(HttpServletRequest request) throws Exception{
        HttpSessionUtil httpSessionUtil = new HttpSessionUtil(request.getSession(false));
        Map<String, Object> result = new HashMap<>();
        List<GetCustDbCodeListOutputDto> outputDtoList = new ArrayList<>();

        int userId = jwtUtil.getUserId(httpSessionUtil.getAttribute("jwt").toString());

        List<String> userAndDbCodeMapping = userCdbtMappingService.getUserCdbtListByUserId(userId).stream()
                .map(dto -> {
                    return dto.getCdbtLowCode();
                })
                .collect(Collectors.toList());

        List<LpgeCodeDto> lpgeDbCodeList = CommonCodeCache.getLpgeCodes().stream()
                .filter(code -> userAndDbCodeMapping.contains(code.getCodeFullName()))
                .collect(Collectors.toList());

        outputDtoList.add(new GetCustDbCodeListOutputDto());
        outputDtoList.get(0).setCustDbType("CDBT_LPGE");
        outputDtoList.get(0).setCustDbCodeList(new ArrayList<>());
        lpgeDbCodeList.forEach(code -> {
            outputDtoList.get(0).getCustDbCodeList().add(code.getCodeFullName());
        });

        result.put("result", "S");
        result.put("output", outputDtoList);

        return result;
    }

    @Transactional
    public Map<String, Object> getListByLpgeCode(CustInfoDto custInfoDto) throws Exception{
        List<CustomerInformationDto> customerInformationByLpgeCode = customerInformationService.getCustomerInformationByCdbtLowCode(custInfoDto.getLpgeCode());
        
        List<String> custIds = customerInformationByLpgeCode.stream()
                .filter(dto -> !dto.getDelYn())
                .map(CustomerInformationDto::getIdx)
                .collect(Collectors.toList());

        List<CustomerBasicConsultationDto> customerBasicConsultationBycustIdList = customerBasicConsultationService.getCustomerBasicConsultationByCustIdList(custIds);

        List<String> keyList = customerBasicConsultationBycustIdList.stream()
                .map(CustomerBasicConsultationDto::getKey)
                .distinct()
                .collect(Collectors.toList());

        List<Map<String, Object>> rows = customerInformationByLpgeCode.stream()
                .filter(dto -> !dto.getDelYn())
                .map(dto -> {
                    Map<String, Object> map = new HashMap<>();
                    Boolean useYn = !dto.getUseYn();
                    map.put("idx", dto.getIdx());
                    map.put("cdbtLowCode", Optional.ofNullable(dto.getCdbtLowCode()).orElse(" "));
                    map.put("품질", Optional.ofNullable(CommonCodeCache.getCdbqCodes().stream()
                            .filter(code -> code.getCodeFullName().equals(dto.getCdbqCode()))
                            .map(code -> code.getCodeValue())
                            .findFirst().orElse(" ")).orElse(" "));
                    map.put("품질변경사유", Optional.ofNullable(dto.getQualityChangeReason()).orElse(" "));
                    map.put("상태", Optional.ofNullable(CommonCodeCache.getCdbsCodes().stream()
                            .filter(code -> code.getCodeFullName().equals(dto.getCdbsCode()))
                            .map(code -> code.getCodeValue())
                            .findFirst().orElse(" ")).orElse(" "));
                    map.put("상태변경사유", Optional.ofNullable(dto.getStatusChangeReason()).orElse(" "));
                    map.put("source", Optional.ofNullable(dto.getUtmSource()).orElse(" "));
                    map.put("medium", Optional.ofNullable(dto.getUtmMedium()).orElse(" "));
                    map.put("campaign", Optional.ofNullable(dto.getUtmCampaign()).orElse(" "));
                    map.put("term", Optional.ofNullable(dto.getUtmTerm()).orElse(" "));
                    map.put("content", Optional.ofNullable(dto.getUtmContent()).orElse(" "));
                    map.put("IP", dto.getIp());
                    map.put("사용여부", useYn.toString());
                    map.put("삭제여부", dto.getDelYn().toString());
                    map.put("생성일", Optional.ofNullable(dateTimeUtil.LocalDateTimeToDateTimeStr(dto.getCreateDate())).orElse(""));
                    map.put("수정일", Optional.ofNullable(dateTimeUtil.LocalDateTimeToDateTimeStr(dto.getModifyDate())).orElse(""));

                    List<String> tempKeyList = new ArrayList<>(keyList);

                    Iterator<String> keyIterator = tempKeyList.iterator();
                    EncryptionUtil encryptionUtil = new EncryptionUtil();

                    while (keyIterator.hasNext()) {
                        String key = keyIterator.next();

                        boolean removed = customerBasicConsultationBycustIdList.stream()
                                .filter(custCusultation -> Objects.equals(custCusultation.getCustId(), dto.getIdx()) && key.equals(custCusultation.getKey()))
                                .peek(custCusultation -> map.put(custCusultation.getKey(), Optional.ofNullable(encryptionUtil.AES256decrypt(custCusultation.getValue())).orElse("")))
                                .count() > 0; // just for triggering the terminal operation

                        if (removed) {
                            keyIterator.remove();
                        }
                    }

                    if (!tempKeyList.isEmpty()) tempKeyList.forEach(key -> map.put(key, null))
                            ;
                    return map;
                })
                .collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("keyList", keyList);
        result.put("rows", rows);

        return result;
    }

    @Transactional
    public String modifyCustInfo(HttpServletRequest request, ModifyCustInfoDto modifyCustInfoDto, String mode) throws Exception{
        HttpSessionUtil httpSessionUtil = new HttpSessionUtil(request.getSession(false));

        int userId = jwtUtil.getUserId(httpSessionUtil.getAttribute("jwt").toString());

        CustomerInformationDto customerInformationById = customerInformationService.getCustomerInformationById(modifyCustInfoDto.getIdx());

        if(mode.equals("useYn")){
            customerInformationById.setUseYn(!modifyCustInfoDto.isValue());
            customerInformationById.modify(userId);
        }else if (mode.equals("delYn")){
            customerInformationById.setDelYn(modifyCustInfoDto.isValue());
            customerInformationById.delete(userId);
        }

        customerInformationService.save(customerInformationById);
        return "success";
    }
}
