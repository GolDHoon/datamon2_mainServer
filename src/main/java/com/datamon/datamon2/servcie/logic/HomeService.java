package com.datamon.datamon2.servcie.logic;

import com.datamon.datamon2.common.CommonCodeCache;
import com.datamon.datamon2.dto.repository.CustomerInformationDto;
import com.datamon.datamon2.dto.repository.LpgeCodeDto;
import com.datamon.datamon2.dto.repository.UserCdbtMappingDto;
import com.datamon.datamon2.servcie.repository.*;
import com.datamon.datamon2.util.HttpSessionUtil;
import com.datamon.datamon2.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class HomeService {
    private JwtUtil jwtUtil;
    private CustomerInformationService customerInformationService;
    private UserCdbtMappingService userCdbtMappingService;

    public HomeService(JwtUtil jwtUtil, CustomerInformationService customerInformationService, UserCdbtMappingService userCdbtMappingService) {
        this.jwtUtil = jwtUtil;
        this.customerInformationService = customerInformationService;
        this.userCdbtMappingService = userCdbtMappingService;
    }

    @Transactional
    public List<Map<String, Object>> homeStatistics(HttpServletRequest request) throws Exception{
        List<Map<String, Object>> result = new ArrayList<>();
        HttpSessionUtil httpSessionUtil = new HttpSessionUtil(request.getSession(false));
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        int userId = jwtUtil.getUserId(httpSessionUtil.getAttribute("jwt").toString());

        List<UserCdbtMappingDto> userCdbtListByUserId = userCdbtMappingService.getUserCdbtListByUserId(userId);


        userCdbtListByUserId.forEach(userCdbtMapping -> {
            Map<String, Object> resultData = new HashMap<>();

            Map<String, Long> collect = customerInformationService.getCustomerInformationByLpgeCode(userCdbtMapping.getCdbtLowCode()).stream()
                    .filter(CustomerInformationDto::getUseYn)
                    .filter(dto -> !dto.getDelYn())
                    .collect(Collectors.groupingBy(dto -> dateTimeFormatter.format(dto.getCreateDate()), Collectors.counting()));

            switch (userCdbtMapping.getCdbtCode()){
                case "LPGE":
                    resultData.put("DBName", CommonCodeCache.getLpgeCodes().stream()
                            .filter(dto -> dto.getCodeFullName().equals(userCdbtMapping.getCdbtLowCode()))
                            .findFirst().orElse(new LpgeCodeDto()).getCodeValue());
                    break;
                default:
                    break;
            }

            resultData.put("data", collect);
            result.add(resultData);
        });
        return result;
    }
}
