package com.datamon.datamon2.controller;

import com.datamon.datamon2.servcie.logic.HomeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class HomeController {
    private static final Logger logger = LogManager.getLogger(HomeController.class);
    private HomeService homeService;

    public HomeController(HomeService homeService) {
        this.homeService = homeService;
    }

    @PostMapping("/homeStatistics")
    public ResponseEntity<?> homeStatistics(HttpServletRequest request, HttpServletResponse response) {
        List<Map<String, Object>> result;
        try {
            result = homeService.homeStatistics(request);
        } catch (Exception e) {
            logger.error(e);
            return new ResponseEntity<>("fail - serverEror", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
