package com.datamon.datamon2.controller;

import com.datamon.datamon2.servcie.logic.CommonService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/common")
public class CommonController {
    private static final Logger logger = LogManager.getLogger(CommonController.class);
    private CommonService commonService;

    public CommonController(CommonService commonService) {
        this.commonService = commonService;
    }

    @GetMapping("/DBList")
    public ResponseEntity<?> getDBList(HttpServletRequest request, HttpServletResponse response){
        List<Map<String, String>> result;

        try {
            result = commonService.getDBList(request);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>("fail - serverEror", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/routingInfo")
    public ResponseEntity<?> getRoutingInfo(HttpServletRequest request, HttpServletResponse response){
        Map<String, String> result;
        try {
            result = commonService.getRoutingInfo(request);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>("fail - serverEror", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/getColumnList")
    public ResponseEntity<?> getColumnList(HttpServletRequest request, HttpServletResponse response, @RequestParam String cdbtCode){
        List<String> result;
        try {
            result = commonService.getColumnList(cdbtCode);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>("fail - serverEror", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
