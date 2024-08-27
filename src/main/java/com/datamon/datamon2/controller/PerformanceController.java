package com.datamon.datamon2.controller;

import com.datamon.datamon2.servcie.logic.PerformanceService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/performance")
public class PerformanceController {
    private static final Logger logger = LogManager.getLogger(PerformanceController.class);
    private PerformanceService performanceService;

    public PerformanceController(PerformanceService performanceService) {
        this.performanceService = performanceService;
    }

    @GetMapping("/ad/init")
    public ResponseEntity<?> getAdPerformance (HttpServletRequest request, HttpServletResponse response){
        List<Map<String, Object>> result;
        try {
            result = performanceService.getAdPerformance(request);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>("fail - serverEror", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/crm/company/init")
    public ResponseEntity<?> getCrmCompanyPerformance (HttpServletRequest request, HttpServletResponse response){
        List<Map<String, Object>> result;
        try {
            result = performanceService.getCrmCompanyPerformance(request);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>("fail - serverEror", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/ad/member/init")
    public ResponseEntity<?> getCrmMemberPerformance (HttpServletRequest request, HttpServletResponse response, @RequestParam String companyId){
        Map<String, Object> result;
        try {
            result = performanceService.getCrmMemberPerformance(request, companyId);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>("fail - serverEror", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
