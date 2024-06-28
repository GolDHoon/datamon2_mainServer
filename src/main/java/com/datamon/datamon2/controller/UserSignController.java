package com.datamon.datamon2.controller;

import com.datamon.datamon2.dto.input.user.LoginInuptDto;
import com.datamon.datamon2.servcie.logic.UserSignService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserSignController {
    private static final Logger logger = LogManager.getLogger(UserSignController.class);
    private UserSignService userSignService;

    public UserSignController(UserSignService userSignService) {
        this.userSignService = userSignService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginInuptDto loginInuptDto, HttpServletRequest request, HttpServletResponse response){
        String result;
        try {
            result = userSignService.userLogin(loginInuptDto.getUserId(), loginInuptDto.getPassword(), request, response);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>("fail - serverEror", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> userLogout(HttpServletRequest request, HttpServletResponse response){
        String result;
        try {
            result = userSignService.userLogout(request);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>("fail - serverEror", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/sessionCheck")
    public ResponseEntity<?> sessionCheck(HttpServletRequest request, HttpServletResponse response){
        try {
            userSignService.sessionTimeReset(request);
            return new ResponseEntity<>("success", HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>("fail - serverEror", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/sessionCheck2")
    public ResponseEntity<?> sessionCheck2(HttpServletRequest request, HttpServletResponse response){
        try {
            userSignService.sessionCheck(request);
            return new ResponseEntity<>("success", HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>("fail - serverEror", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}