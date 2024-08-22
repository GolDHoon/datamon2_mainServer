package com.datamon.datamon2.controller;

import com.datamon.datamon2.dto.input.call.*;
import com.datamon.datamon2.dto.repository.OutboundDto;
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
        List<Map<String, Object>> result;
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
        List<Map<String, Object>> result;
        try {
            result = callService.getCustList(custListDto);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>("fail - serverEror", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/distribution/single")
    public ResponseEntity<?> saveSingleOutBound (HttpServletRequest request, HttpServletResponse response, @RequestBody SaveSingleOutBoundDto saveSingleOutBoundDto){
        String result;
        try {
            result = callService.saveSingleOutBound(request, saveSingleOutBoundDto);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>("fail - serverEror", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/distribution/multi")
    public ResponseEntity<?> saveMultiOutBound (HttpServletRequest request, HttpServletResponse response, @RequestBody SaveMultiOutBoundDto saveMultiOutBoundDto){
        String result;
        try {
            result = callService.saveMultiOutBound(request, saveMultiOutBoundDto);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>("fail - serverEror", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/outbound/list")
    public ResponseEntity<?> getOutBoundList (HttpServletRequest request, HttpServletResponse response){
        List<?> result;
        try {
            result = callService.getOutBoundList(request);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>("fail - serverEror", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/outbound/updateCdbsCode")
    public ResponseEntity<?> updateCdbsCode (HttpServletRequest request, HttpServletResponse response, @RequestBody UpdateCdbsCodeDto updateCdbsCodeDto){
        String result;
        try {
            result = callService.updateCdbsCode(request, updateCdbsCodeDto);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>("fail - serverEror", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/outbound/updateOutbound")
    public ResponseEntity<?> updateOutbound (HttpServletRequest request, HttpServletResponse response, @RequestBody UpdateOutboundDto updateOutboundDto){
        String result;
        try {
            result = callService.updateOutbound(request, updateOutboundDto);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>("fail - serverEror", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/outbound/listByUser/total")
    public ResponseEntity<?> getOutboundTotalList (HttpServletRequest request, HttpServletResponse response){
        List<Map<String, Object>> result;
        try {
            result = callService.getOutboundTotalList(request);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>("fail - serverEror", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
