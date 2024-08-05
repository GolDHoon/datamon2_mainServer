package com.datamon.datamon2.controller;

import com.datamon.datamon2.dto.input.call.CustListDto;
import com.datamon.datamon2.servcie.logic.CallService;
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
@RequestMapping("/call")
public class CallController {
    private static final Logger logger = LogManager.getLogger(CallController.class);
    private CallService callService;

    public CallController(CallService callService) {
        this.callService = callService;
    }

    @GetMapping("/custDbList")
    public ResponseEntity<?> getCustDbList (HttpServletRequest request, HttpServletResponse response) {
        List<Map<String, String>> result;
        try {
            result = callService.getCustDbList(request);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>("fail - serverEror", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/employeeList")
    public ResponseEntity<?> getEmployeeList (HttpServletRequest request, HttpServletResponse response) {
        List<Map<String, String>> result;
        try {
            result = callService.getEmployeeList(request);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>("fail - serverEror", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/custList")
    public ResponseEntity<?> getCustList (HttpServletRequest request, HttpServletResponse response, @RequestBody CustListDto custListDto){
        List<Map<String, String>> result;
        try {
            result = callService.getCustList(custListDto);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>("fail - serverEror", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
