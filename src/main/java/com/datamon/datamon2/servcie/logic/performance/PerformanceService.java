package com.datamon.datamon2.servcie.logic.performance;

import com.datamon.datamon2.dto.output.common.ErrorOutputDto;
import com.datamon.datamon2.dto.repository.CustomerInformationDto;
import com.datamon.datamon2.servcie.repository.CustomerInformationService;
import com.datamon.datamon2.servcie.repository.UserCdbtMappingService;
import com.datamon.datamon2.util.HttpSessionUtil;
import com.datamon.datamon2.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PerformanceService {
    private JwtUtil jwtUtil;
    private CustomerInformationService customerInformationService;
    private UserCdbtMappingService userCdbtMappingService;

    public PerformanceService(JwtUtil jwtUtil, CustomerInformationService customerInformationService, UserCdbtMappingService userCdbtMappingService) {
        this.jwtUtil = jwtUtil;
        this.customerInformationService = customerInformationService;
        this.userCdbtMappingService = userCdbtMappingService;
    }

    @Transactional
    public Map<String, Object> getCollectionPerformanceData(HttpServletRequest request, String dbCode) throws Exception {
        HttpSessionUtil httpSessionUtil = new HttpSessionUtil(request.getSession(false));
        List<Map<String, Object>> performanceDataList = new ArrayList<>();
        Map<String, Object> result = new HashMap<>();
        result.put("result", "E");

        int userId = jwtUtil.getUserId(httpSessionUtil.getAttribute("jwt").toString());

        List<CustomerInformationDto> customerInformationDtoList;

        if (dbCode == null) {
            List<String> dbCodeList = userCdbtMappingService.getUserCdbtListByUserId(userId).stream()
                    .map(dto -> dto.getCdbtLowCode())
                    .toList();

            customerInformationDtoList = customerInformationService.getCustomerInformationByCdbtLowCodeList(dbCodeList).stream()
                    .filter(CustomerInformationDto::getUseYn)
                    .filter(custInfo -> !custInfo.getDelYn())
                    .toList();
        } else {
            customerInformationDtoList = customerInformationService.getCustomerInformationByCdbtLowCode(dbCode).stream()
                    .filter(CustomerInformationDto::getUseYn)
                    .filter(custInfo -> !custInfo.getDelYn())
                    .toList();
        }

        customerInformationDtoList = customerInformationDtoList.stream()
                .sorted(Comparator.comparing(dto -> dto.getCreateDate().toLocalDate()))
                .collect(Collectors.toList());

        Map<LocalDate, Map<String, Long>> groupedData = customerInformationDtoList.stream()
                .collect(Collectors.groupingBy(
                        dto -> dto.getCreateDate().toLocalDate(),
                        Collectors.groupingBy(
                                dto -> {
                                    String source = dto.getUtmSource();
                                    String medium = dto.getUtmMedium();
                                    if (source == null && medium == null) {
                                        return "unknown";
                                    } else {
                                        source = source != null ? source : "unknown";
                                        medium = medium != null ? medium : "unknown";
                                        return source + "/" + medium;
                                    }
                                },
                                Collectors.counting()
                        )
                ));

        groupedData.forEach((date, sourceMediumMap) -> {
            Map<String, Object> performanceData = new HashMap<>();
            performanceData.put("name", date);
            sourceMediumMap.forEach(performanceData::put);
            performanceDataList.add(performanceData);
        });

        performanceDataList.sort(Comparator.comparing(data -> (LocalDate) data.get("name")));
        result.put("output", performanceDataList);
        result.put("result", "S");

        return result;
    }
}