package com.datamon.datamon2.controller;

import com.datamon.datamon2.dto.input.user.CopanyAndCdbtDto;
import com.datamon.datamon2.dto.input.user.CopanyListAndCdbtDto;
import com.datamon.datamon2.dto.input.user.UserAndCdbtDto;
import com.datamon.datamon2.dto.input.userAuth.UserAuthModifyDto;
import com.datamon.datamon2.dto.input.userAuth.UserListForUserCdbtByCdbtLowCodeDto;
import com.datamon.datamon2.servcie.logic.UserAuthService;
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
@RequestMapping("/userAuth")
public class UserAuthController {
    private static final Logger logger = LogManager.getLogger(UserAuthController.class);
    private UserAuthService userAuthService;

    public UserAuthController(UserAuthService userAuthService) {
        this.userAuthService = userAuthService;
    }

    @GetMapping("/cdbtList")
    public ResponseEntity<?> getUserCdbtLowCodeList (HttpServletRequest request, HttpServletResponse response){
        List<Map<String, String>> result;
        try {
            result = userAuthService.getUserCdbtLowCodeList(request);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>("fail - serverEror", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/userListByCdbtCode")
    public ResponseEntity<?> getUserListForUserCdbtByCdbtLowCode (HttpServletRequest request, HttpServletResponse response, @RequestBody UserListForUserCdbtByCdbtLowCodeDto userListForUserCdbtByCdbtLowCodeDto){
        Map<String, List> result;

        try {
            result = userAuthService.getUserListForUserCdbtByCdbtLowCode(request, userListForUserCdbtByCdbtLowCodeDto);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>("fail - serverEror", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/getUserListByCopanyAndCdbt")
    public ResponseEntity<?> getUserListByCopanyAndCdbt(HttpServletRequest request, HttpServletResponse response, @RequestBody CopanyAndCdbtDto userListByCopanyAndCdbtDto) throws Exception {
        List<Map<String, String>> result = new ArrayList<>();
        try {
            result = userAuthService.getUserListByCompanyAndCdbt(request, userListByCopanyAndCdbtDto);
        } catch (Exception e) {
            return new ResponseEntity<>("fail - serverEror", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/createUserCdbtMappingByCopanyAndCdbt")
    public ResponseEntity<?> createUserCdbtMappingByCopanyAndCdbt (HttpServletRequest request, HttpServletResponse response, @RequestBody CopanyListAndCdbtDto copanyListAndCdbtDto) throws Exception {
        String result;
        try {
            result = userAuthService.createUserCdbtMappingByCopanyAndCdbt(request, copanyListAndCdbtDto);
        } catch (Exception e) {
            return new ResponseEntity<>("fail - serverEror", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/deleteUserCdbtMappingByCopanyAndCdbt")
    public ResponseEntity<?> deleteUserCdbtMappingByCopanyAndCdbt (HttpServletRequest request, HttpServletResponse response, @RequestBody UserAndCdbtDto userAndCdbtDto) throws Exception {
        String result;
        try {
            result = userAuthService.deleteUserCdbtMappingByCopanyAndCdbt(userAndCdbtDto);
        } catch (Exception e) {
            return new ResponseEntity<>("fail - serverEror", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/modifyUserAuth")
    public ResponseEntity<?> modifyUserAuth(HttpServletRequest request, HttpServletResponse response, @RequestBody UserAuthModifyDto userAuthModifyDto) throws Exception {
        String result;
        try {
            result = userAuthService.modifyUserAuth(request, userAuthModifyDto);
        } catch (Exception e) {
            return new ResponseEntity<>("fail - serverEror", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
