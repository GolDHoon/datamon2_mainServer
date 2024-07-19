package com.datamon.datamon2.servcie.logic;

import com.datamon.datamon2.dto.input.custInfo.CustInfoDto;
import com.datamon.datamon2.dto.repository.CustomerBasicConsultationDto;
import com.datamon.datamon2.dto.repository.CustomerInformationDto;
import com.datamon.datamon2.servcie.repository.CustomerBasicConsultationService;
import com.datamon.datamon2.servcie.repository.CustomerInformationService;
import com.datamon.datamon2.util.DateTimeUtil;
import com.datamon.datamon2.util.EncryptionUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CustInfoService {
    private DateTimeUtil dateTimeUtil = new DateTimeUtil();
    private CustomerInformationService customerInformationService;
    private CustomerBasicConsultationService customerBasicConsultationService;

    public CustInfoService(CustomerInformationService customerInformationService, CustomerBasicConsultationService customerBasicConsultationService) {
        this.customerInformationService = customerInformationService;
        this.customerBasicConsultationService = customerBasicConsultationService;
    }

    @Transactional
    public Map<String, Object> getListByLpgeCode(CustInfoDto custInfoDto) throws Exception{
        List<CustomerInformationDto> customerInformationByLpgeCode = customerInformationService.getCustomerInformationByLpgeCode(custInfoDto.getLpgeCode());
        
        List<Long> custIds = customerInformationByLpgeCode.stream()
                .filter(dto -> !dto.getDelYn())
                .map(CustomerInformationDto::getIdx)
                .collect(Collectors.toList());

        List<CustomerBasicConsultationDto> customerBasicConsultationBycustIdList = customerBasicConsultationService.getCustomerBasicConsultationBycustIdList(custIds);

        List<String> keyList = customerBasicConsultationBycustIdList.stream()
                .map(CustomerBasicConsultationDto::getKey)
                .distinct()
                .collect(Collectors.toList());

        List<Map<String, Object>> rows = customerInformationByLpgeCode.stream()
                .filter(dto -> !dto.getDelYn())
                .filter(CustomerInformationDto::getUseYn)
                .map(dto -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("idx", dto.getIdx());
                    map.put("cdbtLowCode", Optional.ofNullable(dto.getCdbtLowCode()).orElse(" "));
                    map.put("sourse", Optional.ofNullable(dto.getUtmSourse()).orElse(" "));
                    map.put("medium", Optional.ofNullable(dto.getUtmMedium()).orElse(" "));
                    map.put("campaign", Optional.ofNullable(dto.getUtmCampaign()).orElse(" "));
                    map.put("term", Optional.ofNullable(dto.getUtmTerm()).orElse(" "));
                    map.put("content", Optional.ofNullable(dto.getUtmContent()).orElse(" "));
                    map.put("IP", dto.getIp());
                    map.put("허수여부", dto.getUseYn().toString());
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
}
