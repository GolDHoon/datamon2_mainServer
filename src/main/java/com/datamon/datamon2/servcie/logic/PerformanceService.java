package com.datamon.datamon2.servcie.logic;

import com.datamon.datamon2.servcie.repository.CustomerBasicConsultationService;
import com.datamon.datamon2.servcie.repository.CustomerInformationService;
import com.datamon.datamon2.servcie.repository.UserCdbtMappingService;
import com.datamon.datamon2.util.HttpSessionUtil;
import com.datamon.datamon2.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PerformanceService {
    private JwtUtil jwtUtil;
    private CustomerInformationService customerInformationService;
    private CustomerBasicConsultationService customerBasicConsultationService;
    private UserCdbtMappingService userCdbtMappingService;

    public Map<String, Object> getAdCompanyPerformance(HttpServletRequest request) throws Exception{
        HttpSessionUtil httpSessionUtil = new HttpSessionUtil(request.getSession(false));

        int userId = jwtUtil.getUserId(httpSessionUtil.getAttribute("jwt").toString());

        userCdbtMappingService.getUserCdbtListByUserId(userId);


        return null;
    }
}