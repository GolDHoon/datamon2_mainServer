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
            columnInfo.setKeyName(key);
            columnInfo.setOriginalKeyName(key);
            getCustInfoListOutputDto.getColumnInfoList().add(columnInfo);
        });

        ColumnInfo columnInfo = new ColumnInfo();
        columnInfo.setColumnType("basic");
        columnInfo.setFilterType("select");
        columnInfo.setKeyName("상태");
        columnInfo.setOriginalKeyName("cdbsCode");
        getCustInfoListOutputDto.getColumnInfoList().add(columnInfo);

        columnInfo = new ColumnInfo();
        columnInfo.setColumnType("basic");
        columnInfo.setFilterType("text");
        columnInfo.setKeyName("소스");
        columnInfo.setOriginalKeyName("utmSource");
        getCustInfoListOutputDto.getColumnInfoList().add(columnInfo);

        columnInfo = new ColumnInfo();
        columnInfo.setColumnType("basic");
        columnInfo.setFilterType("text");
        columnInfo.setKeyName("매체");
        columnInfo.setOriginalKeyName("utmMedium");
        getCustInfoListOutputDto.getColumnInfoList().add(columnInfo);

        columnInfo = new ColumnInfo();
        columnInfo.setColumnType("basic");
        columnInfo.setFilterType("text");
        columnInfo.setKeyName("캠페인");
        columnInfo.setOriginalKeyName("utmCampaign");
        getCustInfoListOutputDto.getColumnInfoList().add(columnInfo);

        columnInfo = new ColumnInfo();
        columnInfo.setColumnType("basic");
        columnInfo.setFilterType("text");
        columnInfo.setKeyName("키워드");
        columnInfo.setOriginalKeyName("utmTerm");
        getCustInfoListOutputDto.getColumnInfoList().add(columnInfo);

        columnInfo = new ColumnInfo();
        columnInfo.setColumnType("basic");
        columnInfo.setFilterType("text");
        columnInfo.setKeyName("콘텐츠");
        columnInfo.setOriginalKeyName("utmContent");
        getCustInfoListOutputDto.getColumnInfoList().add(columnInfo);

        columnInfo = new ColumnInfo();
        columnInfo.setColumnType("basic");
        columnInfo.setFilterType("text");
        columnInfo.setKeyName("ip");
        columnInfo.setOriginalKeyName("ip");
        getCustInfoListOutputDto.getColumnInfoList().add(columnInfo);

        columnInfo = new ColumnInfo();
        columnInfo.setColumnType("basic");
        columnInfo.setFilterType("date");
        columnInfo.setKeyName("생성일");
        columnInfo.setOriginalKeyName("createDate");
        getCustInfoListOutputDto.getColumnInfoList().add(columnInfo);

        columnInfo = new ColumnInfo();
        columnInfo.setColumnType("basic");
        columnInfo.setFilterType("date");
        columnInfo.setKeyName("최종수정일");
        columnInfo.setOriginalKeyName("modifyDate");
        getCustInfoListOutputDto.getColumnInfoList().add(columnInfo);

        columnInfo = new ColumnInfo();
        columnInfo.setColumnType("crm");
        columnInfo.setFilterType("text");
        columnInfo.setKeyName("메모");
        columnInfo.setOriginalKeyName("memo");
        getCustInfoListOutputDto.getColumnInfoList().add(columnInfo);

        custInfoDtoList.forEach(dto -> {
            List<RowInfo> rowInfoList = new ArrayList<>();

            RowInfo rowInfo = new RowInfo();
            rowInfo.setData(dto.getCdbsCode());
            rowInfo.setDataType("string");
            rowInfo.setKeyName("상태");
            rowInfo.setOriginalKeyName("cdbsCode");
            rowInfoList.add(rowInfo);

            rowInfo = new RowInfo();
            rowInfo.setData(dto.getUtmSource());
            rowInfo.setDataType("string");
            rowInfo.setKeyName("소스");
            rowInfo.setOriginalKeyName("utmSource");
            rowInfoList.add(rowInfo);

            rowInfo = new RowInfo();
            rowInfo.setData(dto.getUtmMedium());
            rowInfo.setDataType("string");
            rowInfo.setKeyName("매체");
            rowInfo.setOriginalKeyName("utmMedium");
            rowInfoList.add(rowInfo);

            rowInfo = new RowInfo();
            rowInfo.setData(dto.getUtmCampaign());
            rowInfo.setDataType("string");
            rowInfo.setKeyName("캠페인");
            rowInfo.setOriginalKeyName("utmCampaign");
            rowInfoList.add(rowInfo);

            rowInfo = new RowInfo();
            rowInfo.setData(dto.getUtmTerm());
            rowInfo.setDataType("string");
            rowInfo.setKeyName("키워드");
            rowInfo.setOriginalKeyName("utmTerm");
            rowInfoList.add(rowInfo);

            rowInfo = new RowInfo();
            rowInfo.setData(dto.getUtmContent());
            rowInfo.setDataType("string");
            rowInfo.setKeyName("콘텐츠");
            rowInfo.setOriginalKeyName("utmContent");
            rowInfoList.add(rowInfo);

            rowInfo = new RowInfo();
            rowInfo.setData(dto.getIp());
            rowInfo.setDataType("string");
            rowInfo.setKeyName("ip");
            rowInfo.setOriginalKeyName("ip");
            rowInfoList.add(rowInfo);

            rowInfo = new RowInfo();
            rowInfo.setData(dto.getCreateDate());
            rowInfo.setDataType("date");
            rowInfo.setKeyName("생성일");
            rowInfo.setOriginalKeyName("createDate");
            rowInfoList.add(rowInfo);

            rowInfo = new RowInfo();
            rowInfo.setData(dto.getModifyDate());
            rowInfo.setDataType("date");
            rowInfo.setKeyName("최종수정일");
            rowInfo.setOriginalKeyName("modifyDate");
            rowInfoList.add(rowInfo);

            EncryptionUtil encryptionUtil = new EncryptionUtil();
            customerBasicConsultationService.getCustomerBasicConsultationByCustId(dto.getIdx())
                    .forEach(customInfo -> {
                        RowInfo tempRowInfo = new RowInfo();
                        tempRowInfo.setData(encryptionUtil.AES256decrypt(customInfo.getValue()));
                        tempRowInfo.setDataType("string");
                        tempRowInfo.setKeyName(customInfo.getKey());
                        tempRowInfo.setOriginalKeyName(customInfo.getKey());
                        rowInfoList.add(tempRowInfo);
                    });

            OutboundDto outbound = outboundService.getOutboundByCustId(dto.getIdx());

            if(outbound.getIdx() == null){
                outbound = new OutboundDto();
                outbound.createId();
                outboundService.save(outbound);
            }

            rowInfo = new RowInfo();
            rowInfo.setData(outbound.getMemo());
            rowInfo.setDataType("string");
            rowInfo.setKeyName("메모");
            rowInfo.setOriginalKeyName("memo");
            rowInfoList.add(rowInfo);

            getCustInfoListOutputDto.getDataList().add(rowInfoList);
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
