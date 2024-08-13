package com.datamon.datamon2.controller;

import com.datamon.datamon2.dto.input.landingPageManage.*;
import com.datamon.datamon2.servcie.logic.LandingPageManageService;
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
@RequestMapping("/landingPageManage")
public class LandingPageManageController {
    private static final Logger logger = LogManager.getLogger(LandingPageManageController.class);
    private LandingPageManageService landingPageManageService;

    public LandingPageManageController(LandingPageManageService landingPageManageService) {
        this.landingPageManageService = landingPageManageService;
    }

    @GetMapping("/list")
    public ResponseEntity<?> getList(HttpServletRequest request, HttpServletResponse response){
        Map<String, List> result;

        try {
            result = landingPageManageService.getList(request);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>("fail - serverEror", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/createLpge")
    public ResponseEntity<?> createLpge(HttpServletRequest request, HttpServletResponse response, @RequestBody LandingPageCreateDto landingPageCreateDto){
        String result;

        try {
            result = landingPageManageService.createLpge(request, landingPageCreateDto);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>("fail - serverEror", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/blockedIpList")
    public ResponseEntity<?> getBlockedIpList(HttpServletRequest request, HttpServletResponse response, @RequestParam("lpgeCode") String lpgeCode){
        List<String> result;

        try {
            result = landingPageManageService.getBlockedIpList(lpgeCode);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>("fail - serverEror", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/deleteBlockedIp")
    public ResponseEntity<?> deleteBlockedId(HttpServletRequest request, HttpServletResponse response, @RequestBody  BlockIpDto blockIpDto){
        String result;

        try {
            result = landingPageManageService.deleteBlockedIp(blockIpDto, request);
        } catch (Exception e) {
            return new ResponseEntity<>("fail - serverEror", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if(!result.equals("success")){
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }else {
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
    }

    @PostMapping("/createBlockedIp")
    public ResponseEntity<?> createBlockedId(HttpServletRequest request, HttpServletResponse response, @RequestBody  BlockIpDto blockIpDto){
        String result;

        try {
            result = landingPageManageService.registerBlockedIp(blockIpDto, request);
        } catch (Exception e) {
            return new ResponseEntity<>("fail - serverEror", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if(!result.equals("success")){
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }else {
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
    }

    @GetMapping("/blockedKeywordList")
    public ResponseEntity<?> getBlockedKeywordList(HttpServletRequest request, HttpServletResponse response, @RequestParam("lpgeCode") String lpgeCode){
        List<String> result;

        try {
            result = landingPageManageService.getBlockedKeywordList(lpgeCode);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>("fail - serverEror", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/deleteBlockedKeyword")
    public ResponseEntity<?> deleteBlockedKeyword(HttpServletRequest request, HttpServletResponse response, @RequestBody BlockKeywordDto blockKeywordDto){
        String result;

        try {
            result = landingPageManageService.deleteBlockedKeyword(blockKeywordDto, request);
        } catch (Exception e) {
            return new ResponseEntity<>("fail - serverEror", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if(!result.equals("success")){
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }else {
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
    }

    @PostMapping("/createBlockedKeyword")
    public ResponseEntity<?> createBlockedKeyword(HttpServletRequest request, HttpServletResponse response, @RequestBody BlockKeywordDto blockKeywordDto){
        String result;

        try {
            result = landingPageManageService.registerBlockedKeyword(blockKeywordDto, request);
        } catch (Exception e) {
            return new ResponseEntity<>("fail - serverEror", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if(!result.equals("success")){
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }else {
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
    }

    @PostMapping("/useUpdate")
    public ResponseEntity<?> updateLpgeCode(HttpServletRequest request, HttpServletResponse response, @RequestBody LandingPageModifyDto landingPageModifyDto){
        String result;

        try {
            result = landingPageManageService.updateLpgeCode(request, landingPageModifyDto);
        } catch (Exception e) {
            return new ResponseEntity<>("fail - serverEror", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if(!result.equals("success")){
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }else {
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
    }

    @PostMapping("/getLandingPageSettings")
    public ResponseEntity<?> getLandingPageSettings (HttpServletRequest request, HttpServletResponse response, @RequestBody GetLandingPageSettingDto getLandingPageSettingDto){
        Map<String, Object> result;

        try {
            result = landingPageManageService.getLandingPageSettings(getLandingPageSettingDto);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>("fail - serverEror", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/updateLandingPageSettings")
    public ResponseEntity<?> saveLandingPageSettings(HttpServletRequest request, HttpServletResponse response, @RequestBody SaveLandingPageSettingsDto saveLandingPageSettingsDto){
        String result;

        try {
            result = landingPageManageService.saveLandingPageSettings(request, saveLandingPageSettingsDto);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>("fail - serverEror", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
