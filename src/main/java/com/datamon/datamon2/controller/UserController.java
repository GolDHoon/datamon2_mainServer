package com.datamon.datamon2.controller;

import com.datamon.datamon2.dto.input.user.*;
import com.datamon.datamon2.dto.input.userAuth.UserAuthModifyDto;
import com.datamon.datamon2.servcie.logic.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    private static final Logger logger = LogManager.getLogger(UserController.class);

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/list")
    public ResponseEntity<?> getList(HttpServletRequest request, HttpServletResponse response, @RequestParam("listType") String listType) throws Exception{
        Map<String, ?> result;

        try {
            if(listType.equals("user")){
                result = userService.getListUser(request);
            }else {
                result = userService.getListCompany(request);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>("fail - serverEror", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/createCompanyUser")
    public ResponseEntity<?> createCompanyUser(HttpServletRequest request, HttpServletResponse response, @RequestBody CreateCompanyUserDto createCompanyUserDto) throws Exception {
        String result;
        try {
            result = userService.createCompanyUser(request, createCompanyUserDto);
        } catch (Exception e) {
            return new ResponseEntity<>("fail - serverEror", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/deleteCompanyUser")
    public ResponseEntity<?> deleteCompanyUser(HttpServletRequest request, HttpServletResponse response, @RequestBody DeleteCompanyUserDto deleteCompanyUserDto) throws Exception {
        String result;
        try {
            result = userService.deleteCompanyUser(request, deleteCompanyUserDto);
        } catch (Exception e) {
            return new ResponseEntity<>("fail - serverEror", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/createMemberUser")
    public ResponseEntity<?> createMemberUser(HttpServletRequest request, HttpServletResponse response, @RequestBody CreateMemberUserDto createMemberUserDto) throws Exception {
        String result;
        try {
            result = userService.createMemberUser(request, createMemberUserDto);
        } catch (Exception e) {
            return new ResponseEntity<>("fail - serverEror", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/deleteMemberUser")
    public ResponseEntity<?> deleteCompanyUser(HttpServletRequest request, HttpServletResponse response, @RequestBody DeleteMemberUserDto deleteMemberUserDto) throws Exception {
        String result;
        try {
            result = userService.deleteMemberUser(request, deleteMemberUserDto);
        } catch (Exception e) {
            return new ResponseEntity<>("fail - serverEror", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
