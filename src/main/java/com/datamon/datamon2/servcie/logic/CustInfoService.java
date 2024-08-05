package com.datamon.datamon2.servcie.logic;

import com.datamon.datamon2.dto.input.custInfo.CustInfoDto;
import com.datamon.datamon2.dto.input.custInfo.ModifyCustInfoDto;
import com.datamon.datamon2.dto.repository.CustomerBasicConsultationDto;
import com.datamon.datamon2.dto.repository.CustomerInformationDto;
import com.datamon.datamon2.servcie.repository.CustomerBasicConsultationService;
import com.datamon.datamon2.servcie.repository.CustomerInformationService;
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
    private DateTimeUtil dateTimeUtil = new DateTimeUtil();
    private CustomerInformationService customerInformationService;
    private CustomerBasicConsultationService customerBasicConsultationService;
    private JwtUtil jwtUtil;

    public CustInfoService(CustomerInformationService customerInformationService, CustomerBasicConsultationService customerBasicConsultationService, JwtUtil jwtUtil) {
        this.customerInformationService = customerInformationService;
        this.customerBasicConsultationService = customerBasicConsultationService;
        this.jwtUtil = jwtUtil;
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
                    map.put("sourse", Optional.ofNullable(dto.getUtmSourse()).orElse(" "));
                    map.put("medium", Optional.ofNullable(dto.getUtmMedium()).orElse(" "));
                    map.put("campaign", Optional.ofNullable(dto.getUtmCampaign()).orElse(" "));
                    map.put("term", Optional.ofNullable(dto.getUtmTerm()).orElse(" "));
                    map.put("content", Optional.ofNullable(dto.getUtmContent()).orElse(" "));
                    map.put("IP", dto.getIp());
                    map.put("허수여부", useYn.toString());
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
