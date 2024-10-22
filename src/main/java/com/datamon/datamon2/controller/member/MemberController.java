package com.datamon.datamon2.controller.member;

import com.datamon.datamon2.dto.input.member.MemberAccountDto;
import com.datamon.datamon2.dto.input.user.*;
import com.datamon.datamon2.servcie.logic.UserService;
import com.datamon.datamon2.servcie.logic.member.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/member")
public class MemberController {
    private static final Logger logger = LogManager.getLogger(MemberController.class);

    private MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/list")
    public ResponseEntity<?> getList(HttpServletRequest request, HttpServletResponse response) throws Exception{
        Map<String, ?> result;

        try {
            result = memberService.getListUser(request);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>("fail - serverEror", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/reqAccount")
    public ResponseEntity<?> requestMemberAccount(HttpServletRequest request, HttpServletResponse response, @RequestBody MemberAccountDto memberAccountDto) throws Exception {
        String result;
        try {
            result = memberService.requestMemberAccount(memberAccountDto, request);
        } catch (Exception e) {
            return new ResponseEntity<>("fail - serverEror", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createMemberUser(HttpServletRequest request, HttpServletResponse response, @RequestBody CreateMemberUserDto createMemberUserDto) throws Exception {
        String result;
        try {
            result = memberService.createUser(request, createMemberUserDto);
        } catch (Exception e) {
            return new ResponseEntity<>("fail - serverEror", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteMemberUser(HttpServletRequest request, HttpServletResponse response, @RequestBody DeleteMemberUserDto deleteMemberUserDto) throws Exception {
        String result;
        try {
            result = memberService.deleteMemberUser(request, deleteMemberUserDto);
        } catch (Exception e) {
            return new ResponseEntity<>("fail - serverEror", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/checkIdDuplicate")
    public ResponseEntity<?> checkIdDuplicate(HttpServletRequest request, HttpServletResponse response, @RequestBody CheckIdDuplicateDto checkIdDuplicateDto) throws  Exception {
        String result;
        try {
            result = memberService.checkIdDuplicate(checkIdDuplicateDto);
        } catch (Exception e) {
            return new ResponseEntity<>("fail - serverEror", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
