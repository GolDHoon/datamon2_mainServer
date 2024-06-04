package com.datamon.datamon2.controller;

import com.datamon.datamon2.servcie.logic.LandingPageManageService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/landingPageManage")
public class LandingPageManageController {
    private static final Logger logger = LogManager.getLogger(LandingPageManageController.class);
    private LandingPageManageService landingPageManageService;

    public LandingPageManageController(LandingPageManageService landingPageManageService) {
        this.landingPageManageService = landingPageManageService;
    }

    @GetMapping("/list")
    public ResponseEntity<?> getList(HttpServletRequest request, HttpServletResponse response){
        List<Map<String, String>> result;

        try {
            result = landingPageManageService.getListByUserIdForSession(request);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>("fail - serverEror", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
