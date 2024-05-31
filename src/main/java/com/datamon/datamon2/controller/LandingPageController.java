package com.datamon.datamon2.controller;

import com.datamon.datamon2.dto.input.landingPage.CreateDto;
import com.datamon.datamon2.dto.input.landingPage.CustDataDto;
import com.datamon.datamon2.dto.output.landingPage.InitalDataDto;
import com.datamon.datamon2.servcie.logic.LandingPageService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EnableJdbcHttpSession
@RestController
@RequestMapping("/landingPage")
public class LandingPageController {
    private static final Logger logger = LogManager.getLogger(LandingPageController.class);
    private LandingPageService landingPageService;

    public LandingPageController(LandingPageService landingPageService) {
        this.landingPageService = landingPageService;
    }

    @GetMapping("/getInitialData")
    public ResponseEntity<?> getInitialData(HttpServletRequest request, HttpServletResponse response,@RequestParam String domain){
        InitalDataDto result;
        try {
            result = landingPageService.getInitialData(domain);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>("fail - serverEror", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/inputLanding")
    public ResponseEntity<?> registerLandingPage(HttpServletRequest request, HttpServletResponse response, @RequestBody CreateDto createDto){
        Map<String, String> result = new HashMap<>();
        String resultStr = "";
        try {
            resultStr = landingPageService.registerLandingPage(createDto);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>("fail - serverEror", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        result.put("result", resultStr);

        return switch (resultStr) {
            case "fail - Domain duplicate" -> new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
            case "fail - Password is different" -> new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
            case "fail - CustUserMapping fail" -> new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
            default -> new ResponseEntity<>(result, HttpStatus.OK);
        };
    }

    @GetMapping("/getBlockedKeyword")
    public ResponseEntity<?> getBlockedKeyword(@RequestParam String lpgeCode){
        List<String> result;
        try {
            result = landingPageService.getBlockedKeyword(lpgeCode);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>("fail - serverEror", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/inputCustData")
    public ResponseEntity<?> registerCustData(HttpServletRequest request, HttpServletResponse response, @RequestBody CustDataDto custDataDto){
        Map<String, String> result = new HashMap<>();
        String resultStr = "";
        try {
            resultStr = landingPageService.registerCustData(request.getHeader("X-Forwarded-For"), custDataDto);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>("fail - serverEror", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        result.put("result", resultStr);
        return switch (resultStr) {
            case "fail - Blocked IP" -> new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
            default -> new ResponseEntity<>(result, HttpStatus.OK);
        };
    }
}
