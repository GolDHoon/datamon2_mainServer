package com.datamon.datamon2.servcie.logic.custInfo;

import com.datamon.datamon2.common.CommonCodeCache;
import com.datamon.datamon2.dto.input.custInfo.DeleteCustInfoDto;
import com.datamon.datamon2.dto.input.custInfo.ModifyCustInfoDto;
import com.datamon.datamon2.dto.output.common.ErrorOutputDto;
import com.datamon.datamon2.dto.output.common.SuccessOutputDto;
import com.datamon.datamon2.dto.output.custInfo.ColumnInfo;
import com.datamon.datamon2.dto.output.custInfo.GetCustDbCodeListOutputDto;
import com.datamon.datamon2.dto.output.custInfo.GetCustInfoListOutputDto;
import com.datamon.datamon2.dto.repository.*;
import com.datamon.datamon2.servcie.repository.*;
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
public class CustInfoService {
    private final OutboundHistoryService outboundHistoryService;
    private UserBaseService userBaseService;
    private DateTimeUtil dateTimeUtil = new DateTimeUtil();
    private CustomerInformationService customerInformationService;
    private CustomerBasicConsultationService customerBasicConsultationService;
    private OutboundService outboundService;
    private UserCdbtMappingService userCdbtMappingService;
    private JwtUtil jwtUtil;

    public CustInfoService(CustomerInformationService customerInformationService, CustomerBasicConsultationService customerBasicConsultationService, JwtUtil jwtUtil, UserBaseService userBaseService, UserBaseService userBaseService1, OutboundService outboundService, UserCdbtMappingService userCdbtMappingService, OutboundHistoryService outboundHistoryService) {
        this.customerInformationService = customerInformationService;
        this.customerBasicConsultationService = customerBasicConsultationService;
        this.jwtUtil = jwtUtil;
        this.userBaseService = userBaseService1;
        this.outboundService = outboundService;
        this.userCdbtMappingService = userCdbtMappingService;
        this.outboundHistoryService = outboundHistoryService;
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
                                row.put(column.getKey(), CommonCodeCache.getCdbsCodes().stream()
                                                                        .filter(code -> code.getCodeFullName().equals(dto.getCdbsCode()))
                                                                        .findFirst().orElse(new CdbsCodeDto()).getCodeValue());
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

            row.put("idx", dto.getIdx());
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
            Map<String, String> dbCode = new HashMap<>();
            dbCode.put(code.getCodeFullName(), code.getCodeValue());
            dbCode.put("key", code.getCodeFullName());
            outputDtoList.get(0).getCustDbCodeList().add(dbCode);
        });

        result.put("result", "S");
        result.put("output", outputDtoList);

        return result;
    }

    @Transactional
    public Map<String, Object> deleteCustInfo(HttpServletRequest request, DeleteCustInfoDto deleteCustInfoDto) throws Exception{
        HttpSessionUtil httpSessionUtil = new HttpSessionUtil(request.getSession(false));
        SuccessOutputDto successOutputDto = new SuccessOutputDto();
        ErrorOutputDto errorOutputDto = new ErrorOutputDto();
        Map<String, Object> result = new HashMap<>();
        result.put("result", "E");

        int userId = jwtUtil.getUserId(httpSessionUtil.getAttribute("jwt").toString());

        CustomerInformationDto custInfo = customerInformationService.getCustomerInformationById(deleteCustInfoDto.getCustInfoIdx());

        if(custInfo.getIdx() == null){
            errorOutputDto.setCode(400);
            errorOutputDto.setDetailReason("고객정보를 찾을 수 없습니다.");
            result.put("output", errorOutputDto);
            return result;
        }

        custInfo.setDelYn(true);
        custInfo.delete(userId);

        customerInformationService.save(custInfo);

        successOutputDto.setCode(200);
        successOutputDto.setMessage("고객정보가 삭제되었습니다.");
        result.put("result", "S");
        result.put("output", successOutputDto);
        return result;
    }

    @Transactional
    public Map<String, Object> modifyCustInfo(HttpServletRequest request, ModifyCustInfoDto modifyCustInfoDto) throws Exception{
        HttpSessionUtil httpSessionUtil = new HttpSessionUtil(request.getSession(false));
        SuccessOutputDto successOutputDto = new SuccessOutputDto();
        ErrorOutputDto errorOutputDto = new ErrorOutputDto();
        Map<String, Object> result = new HashMap<>();
        result.put("result", "E");

        int userId = jwtUtil.getUserId(httpSessionUtil.getAttribute("jwt").toString());
        CustomerInformationDto custInfo = customerInformationService.getCustomerInformationById(modifyCustInfoDto.getCustInfoIdx());

        if(custInfo.getIdx() == null){
            errorOutputDto.setCode(400);
            errorOutputDto.setDetailReason("고객정보를 찾을 수 없습니다.");
            result.put("output", errorOutputDto);
        }

        EncryptionUtil encryptionUtil = new EncryptionUtil();
        OutboundDto outbound = outboundService.getOutboundByCustId(modifyCustInfoDto.getCustInfoIdx());
        List<CustomerBasicConsultationDto> customColumnDataList = customerBasicConsultationService.getCustomerBasicConsultationByCustId(modifyCustInfoDto.getCustInfoIdx());

        modifyCustInfoDto.getDataList().forEach(map -> {
            switch ((String) map.get("columnType")){
                case "crm":{
                    switch ((String) map.get("key")){
                        case "memo" : {
                            outbound.setMemo((String) map.get("value"));
                            break;
                        }
                        default: break;
                    }
                    break;
                }
                case "basic":{
                    switch ((String) map.get("key")){
                        case "cdbsCode" :{
                            custInfo.setCdbsCode((String) map.get("value"));
                            break;
                        }
                        case "utmSource" :{
                            custInfo.setUtmSource((String) map.get("value"));
                            break;
                        }
                        case "utmMedium" :{
                            custInfo.setUtmMedium((String) map.get("value"));
                            break;
                        }
                        case "utmCampaign" :{
                            custInfo.setUtmCampaign((String) map.get("value"));
                            break;
                        }
                        case "utmTerm" :{
                            custInfo.setUtmTerm((String) map.get("value"));
                            break;
                        }
                        case "utmContent" :{
                            custInfo.setUtmContent((String) map.get("value"));
                            break;
                        }
                        case "ip" :{
                            custInfo.setIp((String) map.get("value"));
                            break;
                        }
                        default:break;
                    }
                    break;
                }
                case "custom":{
                    customColumnDataList.stream()
                            .filter(dto -> dto.getKey().equals((String) map.get("key")))
                            .map(dto -> {
                                dto.setValue(encryptionUtil.AES256encrypt((String) map.get("value")));
                                return null;
                            })
                            .collect(Collectors.toList());
                    break;
                }
                default: break;
            }

            customColumnDataList.forEach(dto -> {
                customerBasicConsultationService.save(dto);
            });

            custInfo.modify(userId);
            customerInformationService.save(custInfo);

            outboundService.save(outbound);

            OutboundHistoryDto outboundHistoryDto = new OutboundHistoryDto();
            outboundHistoryDto.setHistory(outbound);
            outboundHistoryDto.setCustDbStatusInfo(custInfo);
            outboundHistoryDto.setSaveReason("고객정보목록-고객정보 수정");
            outboundHistoryDto.createId();
            outboundHistoryDto.create(userId);

            outboundHistoryService.save(outboundHistoryDto);
        });

        successOutputDto.setCode(200);
        successOutputDto.setMessage("고객정보가 삭제되었습니다.");
        result.put("result", "S");
        result.put("output", successOutputDto);
        return result;
    }
}
