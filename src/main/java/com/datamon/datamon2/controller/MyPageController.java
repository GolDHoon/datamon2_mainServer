package com.datamon.datamon2.controller;

import com.datamon.datamon2.dto.input.myPage.ModifyCompanyDto;
import com.datamon.datamon2.dto.input.myPage.ModifyMemberDto;
import com.datamon.datamon2.dto.input.myPage.PasswordSetDto;
import com.datamon.datamon2.servcie.logic.MyPageService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/myPage")
public class MyPageController {
    private static final Logger logger = LogManager.getLogger(MyPageController.class);
    private MyPageService myPageService;

    public MyPageController(MyPageService myPageService) {
        this.myPageService = myPageService;
    }

    @GetMapping("/info")
    public ResponseEntity<?> getInfo(HttpServletRequest request, HttpServletResponse response){
        Map<String, String> result;
        try {
            result = myPageService.getInfo(request);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>("fail - serverEror", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/modifyMember")
    public ResponseEntity<?> modifyMember(HttpServletRequest request, HttpServletResponse response, @RequestBody ModifyMemberDto modifyMemberDto){
        String result;
        try {
            result = myPageService.modifyMember(request, modifyMemberDto);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>("fail - serverEror", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/modifyCompany")
    public ResponseEntity<?> modifyCompany(HttpServletRequest request, HttpServletResponse response, @RequestBody ModifyCompanyDto modifyCompanyDto){
        String result;

        try {
            result = myPageService.modifyCompany(request, modifyCompanyDto);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>("fail - serverEror", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/setPassword")
    public ResponseEntity<?> setPassword(HttpServletRequest request, HttpServletResponse response, @RequestBody PasswordSetDto passwordSetDto){
        String result;

        try {
            result = myPageService.setPassword(request, passwordSetDto);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>("fail - serverEror", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
