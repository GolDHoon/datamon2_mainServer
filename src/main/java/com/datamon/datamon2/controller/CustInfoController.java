package com.datamon.datamon2.controller;

import com.datamon.datamon2.dto.input.custInfo.CustInfoDto;
import com.datamon.datamon2.servcie.logic.CustInfoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/custInfo")
public class CustInfoController {
    private static final Logger logger = LogManager.getLogger(CustInfoController.class);
    private CustInfoService custInfoService;

    public CustInfoController(CustInfoService custInfoService) {
        this.custInfoService = custInfoService;
    }

    @PostMapping("/list")
    public ResponseEntity<?> getList(HttpServletRequest request, HttpServletResponse response, @RequestBody CustInfoDto custInfoDto) throws Exception{
        Map<String, Object> result;
        try {
            result= custInfoService.getListByLpgeCode(custInfoDto);
        } catch (Exception e) {
            logger.error(e);
            return new ResponseEntity<>("fail - serverEror", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
