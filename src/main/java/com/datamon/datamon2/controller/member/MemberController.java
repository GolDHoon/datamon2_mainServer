package com.datamon.datamon2.controller.member;

import com.datamon.datamon2.dto.input.member.*;
import com.datamon.datamon2.dto.output.common.ErrorOutputDto;
import com.datamon.datamon2.dto.output.common.SuccessOutputDto;
import com.datamon.datamon2.dto.output.member.GetMemberListOutputDto;
import com.datamon.datamon2.dto.output.member.GetMemberOutputDto;
import com.datamon.datamon2.dto.output.member.GetRequestMemberAccountListOutputDto;
import com.datamon.datamon2.servcie.logic.member.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "직원계정정보", description = "직원계정정보 관련 API")
public class MemberController {
    private static final Logger logger = LogManager.getLogger(MemberController.class);

    private MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/")
    @Operation(summary = "직원계정정보 API", description = "직원계정정보를 출력해주는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "데이터 출력 성공.",
                    content = @Content(schema = @Schema(implementation = GetMemberOutputDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.",
                    content = @Content(schema = @Schema(implementation = ErrorOutputDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류가 발생했습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorOutputDto.class)))
    })
    public ResponseEntity<?> getMember(HttpServletRequest request, HttpServletResponse response) throws Exception{
        Map<String, Object> result;
        GetMemberOutputDto resultData;

        try {
            result = memberService.getMember(request);

            if(result.get("result").toString().equals("S")){
                resultData = (GetMemberOutputDto) result.get("output");
            }else{
                ErrorOutputDto errorOutputDto = (ErrorOutputDto) result.get("output");

                if(errorOutputDto.getCode() < 500){
                    return new ResponseEntity<>(errorOutputDto.getDetailReason(), HttpStatus.INTERNAL_SERVER_ERROR);
                }else{
                    return new ResponseEntity<>(errorOutputDto.getDetailReason(), HttpStatus.BAD_REQUEST);
                }
            }
        } catch (Exception e) {
            logger.error(e);
            return new ResponseEntity<>("fail - serverEror", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(resultData, HttpStatus.OK);
    }

    @GetMapping("/list")
    @Operation(summary = "직원계정정보 목록 API", description = "직원계정정보 목록을 출력해주는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "데이터 출력 성공.",
                    content = @Content(schema = @Schema(implementation = GetMemberListOutputDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.",
                    content = @Content(schema = @Schema(implementation = ErrorOutputDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류가 발생했습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorOutputDto.class)))
    })
    public ResponseEntity<?> getMemberList(HttpServletRequest request, HttpServletResponse response) throws Exception{
        Map<String, Object> result;
        GetMemberListOutputDto resultData;

        try {
            result = memberService.getMemberList(request);

            if(result.get("result").toString().equals("S")){
                resultData = (GetMemberListOutputDto) result.get("output");
            }else{
                ErrorOutputDto errorOutputDto = (ErrorOutputDto) result.get("output");

                if(errorOutputDto.getCode() < 500){
                    return new ResponseEntity<>(errorOutputDto.getDetailReason(), HttpStatus.INTERNAL_SERVER_ERROR);
                }else{
                    return new ResponseEntity<>(errorOutputDto.getDetailReason(), HttpStatus.BAD_REQUEST);
                }
            }
        } catch (Exception e) {
            logger.error(e);
            return new ResponseEntity<>("fail - serverEror", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(resultData, HttpStatus.OK);
    }

    @PostMapping("/reqAccount")
    @Operation(summary = "직원계정 신청 API", description = "직원계정을 신청하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "데이터 처리 성공.",
                    content = @Content(schema = @Schema(implementation = SuccessOutputDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.",
                    content = @Content(schema = @Schema(implementation = ErrorOutputDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류가 발생했습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorOutputDto.class)))
    })
    public ResponseEntity<?> requestMemberAccount(HttpServletRequest request, HttpServletResponse response
            , @RequestBody MemberAccountDto memberAccountDto) throws Exception {
        Map<String, Object> result;
        SuccessOutputDto resultData;

        try {
            result = memberService.requestMemberAccount(memberAccountDto, request);

            if(result.get("result").toString().equals("S")){
                resultData = (SuccessOutputDto) result.get("output");
            }else{
                ErrorOutputDto errorOutputDto = (ErrorOutputDto) result.get("output");

                if(errorOutputDto.getCode() < 500){
                    return new ResponseEntity<>(errorOutputDto.getDetailReason(), HttpStatus.INTERNAL_SERVER_ERROR);
                }else{
                    return new ResponseEntity<>(errorOutputDto.getDetailReason(), HttpStatus.BAD_REQUEST);
                }
            }
        } catch (Exception e) {
            logger.error(e);
            return new ResponseEntity<>("fail - serverEror", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(resultData, HttpStatus.OK);
    }

    @PostMapping("/create")
    @Operation(summary = "직원계정 생성 API", description = "직원계정을 생성하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "데이터 처리 성공.",
                    content = @Content(schema = @Schema(implementation = SuccessOutputDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.",
                    content = @Content(schema = @Schema(implementation = ErrorOutputDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류가 발생했습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorOutputDto.class)))
    })
    public ResponseEntity<?> createMemberUser(HttpServletRequest request, HttpServletResponse response, @RequestBody MemberUserInfoDto memberUserInfoDto) throws Exception {
        Map<String, Object> result;
        SuccessOutputDto resultData;

        try {
            result = memberService.createMemberUser(request, memberUserInfoDto);

            if(result.get("result").toString().equals("S")){
                resultData = (SuccessOutputDto) result.get("output");
            }else{
                ErrorOutputDto errorOutputDto = (ErrorOutputDto) result.get("output");

                if(errorOutputDto.getCode() < 500){
                    return new ResponseEntity<>(errorOutputDto.getDetailReason(), HttpStatus.INTERNAL_SERVER_ERROR);
                }else{
                    return new ResponseEntity<>(errorOutputDto.getDetailReason(), HttpStatus.BAD_REQUEST);
                }
            }
        } catch (Exception e) {
            logger.error(e);
            return new ResponseEntity<>("fail - serverEror", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(resultData, HttpStatus.OK);
    }

    @PostMapping("/modify")
    @Operation(summary = "직원계정 수정 API", description = "직원계정을 수정하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "데이터 처리 성공.",
                    content = @Content(schema = @Schema(implementation = SuccessOutputDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.",
                    content = @Content(schema = @Schema(implementation = ErrorOutputDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류가 발생했습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorOutputDto.class)))
    })
    public ResponseEntity<?> modifyMemberUser(HttpServletRequest request, HttpServletResponse response, @RequestBody MemberUserInfoDto memberUserInfoDto) throws Exception {
        Map<String, Object> result;
        SuccessOutputDto resultData;

        try {
            result = memberService.modifyMemberUser(request, memberUserInfoDto);

            if(result.get("result").toString().equals("S")){
                resultData = (SuccessOutputDto) result.get("output");
            }else{
                ErrorOutputDto errorOutputDto = (ErrorOutputDto) result.get("output");

                if(errorOutputDto.getCode() < 500){
                    return new ResponseEntity<>(errorOutputDto.getDetailReason(), HttpStatus.INTERNAL_SERVER_ERROR);
                }else{
                    return new ResponseEntity<>(errorOutputDto.getDetailReason(), HttpStatus.BAD_REQUEST);
                }
            }
        } catch (Exception e) {
            logger.error(e);
            return new ResponseEntity<>("fail - serverEror", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(resultData, HttpStatus.OK);
    }

    @PostMapping("/delete")
    @Operation(summary = "직원계정 삭제 API", description = "직원계정을 삭제하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "데이터 처리 성공.",
                    content = @Content(schema = @Schema(implementation = SuccessOutputDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.",
                    content = @Content(schema = @Schema(implementation = ErrorOutputDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류가 발생했습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorOutputDto.class)))
    })
    public ResponseEntity<?> deleteMemberUser(HttpServletRequest request, HttpServletResponse response, @RequestBody MemberUserInfoDto memberUserInfoDto) throws Exception {
        Map<String, Object> result;
        SuccessOutputDto resultData;

        try {
            result = memberService.deleteMemberUser(request, memberUserInfoDto);

            if(result.get("result").toString().equals("S")){
                resultData = (SuccessOutputDto) result.get("output");
            }else{
                ErrorOutputDto errorOutputDto = (ErrorOutputDto) result.get("output");

                if(errorOutputDto.getCode() < 500){
                    return new ResponseEntity<>(errorOutputDto.getDetailReason(), HttpStatus.INTERNAL_SERVER_ERROR);
                }else{
                    return new ResponseEntity<>(errorOutputDto.getDetailReason(), HttpStatus.BAD_REQUEST);
                }
            }
        } catch (Exception e) {
            logger.error(e);
            return new ResponseEntity<>("fail - serverEror", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(resultData, HttpStatus.OK);
    }

    @PostMapping("/checkIdDuplicate")
    @Operation(summary = "직원계정 ID중복체크 API", description = "직원계정을 ID중복체크하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "데이터 확인 완료.",
                    content = @Content(schema = @Schema(implementation = SuccessOutputDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.",
                    content = @Content(schema = @Schema(implementation = ErrorOutputDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류가 발생했습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorOutputDto.class)))
    })
    public ResponseEntity<?> checkIdDuplicate(HttpServletRequest request, HttpServletResponse response, @RequestBody CheckIdDuplicateDto checkIdDuplicateDto) throws  Exception {
        Map<String, Object> result;
        SuccessOutputDto resultData;

        try {
            result = memberService.checkIdDuplicate(checkIdDuplicateDto);

            if(result.get("result").toString().equals("S")){
                resultData = (SuccessOutputDto) result.get("output");
            }else{
                ErrorOutputDto errorOutputDto = (ErrorOutputDto) result.get("output");

                if(errorOutputDto.getCode() < 500){
                    return new ResponseEntity<>(errorOutputDto.getDetailReason(), HttpStatus.INTERNAL_SERVER_ERROR);
                }else{
                    return new ResponseEntity<>(errorOutputDto.getDetailReason(), HttpStatus.BAD_REQUEST);
                }
            }
        } catch (Exception e) {
            logger.error(e);
            return new ResponseEntity<>("fail - serverEror", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(resultData, HttpStatus.OK);
    }

    @GetMapping("/approval/list")
    @Operation(summary = "직원계정신청정보 목록 API", description = "직원계정신청정보 목록을 출력해주는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "데이터 출력 성공.",
                    content = @Content(schema = @Schema(implementation = GetRequestMemberAccountListOutputDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.",
                    content = @Content(schema = @Schema(implementation = ErrorOutputDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류가 발생했습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorOutputDto.class)))
    })
    public ResponseEntity<?> getRequestMemberAccountList(HttpServletRequest request, HttpServletResponse response) throws Exception{
        Map<String, Object> result;
        GetRequestMemberAccountListOutputDto resultData;

        try {
            result = memberService.getRequestMemberAccountList(request);

            if(result.get("result").toString().equals("S")){
                resultData = (GetRequestMemberAccountListOutputDto) result.get("output");
            }else{
                ErrorOutputDto errorOutputDto = (ErrorOutputDto) result.get("output");

                if(errorOutputDto.getCode() < 500){
                    return new ResponseEntity<>(errorOutputDto.getDetailReason(), HttpStatus.INTERNAL_SERVER_ERROR);
                }else{
                    return new ResponseEntity<>(errorOutputDto.getDetailReason(), HttpStatus.BAD_REQUEST);
                }
            }
        } catch (Exception e) {
            logger.error(e);
            return new ResponseEntity<>("fail - serverEror", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(resultData, HttpStatus.OK);
    }

    @PostMapping("/account/approve")
    @Operation(summary = "직원계정 신청 승인 API", description = "직원계정을 신청을 승인하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "데이터 처리 성공.",
                    content = @Content(schema = @Schema(implementation = SuccessOutputDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.",
                    content = @Content(schema = @Schema(implementation = ErrorOutputDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류가 발생했습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorOutputDto.class)))
    })
    public ResponseEntity<?> approveAccount(HttpServletRequest request, HttpServletResponse response, @RequestBody MemberAccountRequestProcessingDto memberAccountRequestProcessingDto) throws Exception {
        Map<String, Object> result;
        SuccessOutputDto resultData;

        try {
            result = memberService.approveAccount(request, memberAccountRequestProcessingDto);

            if(result.get("result").toString().equals("S")){
                resultData = (SuccessOutputDto) result.get("output");
            }else{
                ErrorOutputDto errorOutputDto = (ErrorOutputDto) result.get("output");

                if(errorOutputDto.getCode() < 500){
                    return new ResponseEntity<>(errorOutputDto.getDetailReason(), HttpStatus.INTERNAL_SERVER_ERROR);
                }else{
                    return new ResponseEntity<>(errorOutputDto.getDetailReason(), HttpStatus.BAD_REQUEST);
                }
            }
        } catch (Exception e) {
            logger.error(e);
            return new ResponseEntity<>("fail - serverEror", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(resultData, HttpStatus.OK);
    }

    @PostMapping("/account/reject")
    @Operation(summary = "직원계정 신청 반려 API", description = "직원계정 신청을 반려하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "데이터 처리 성공.",
                    content = @Content(schema = @Schema(implementation = SuccessOutputDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.",
                    content = @Content(schema = @Schema(implementation = ErrorOutputDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류가 발생했습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorOutputDto.class)))
    })
    public ResponseEntity<?> rejectAccount(HttpServletRequest request, HttpServletResponse response, @RequestBody MemberAccountRequestProcessingDto memberAccountRequestProcessingDto) throws Exception {
        Map<String, Object> result;
        SuccessOutputDto resultData;

        try {
            result = memberService.rejectAccount(request, memberAccountRequestProcessingDto);

            if(result.get("result").toString().equals("S")){
                resultData = (SuccessOutputDto) result.get("output");
            }else{
                ErrorOutputDto errorOutputDto = (ErrorOutputDto) result.get("output");

                if(errorOutputDto.getCode() < 500){
                    return new ResponseEntity<>(errorOutputDto.getDetailReason(), HttpStatus.INTERNAL_SERVER_ERROR);
                }else{
                    return new ResponseEntity<>(errorOutputDto.getDetailReason(), HttpStatus.BAD_REQUEST);
                }
            }
        } catch (Exception e) {
            logger.error(e);
            return new ResponseEntity<>("fail - serverEror", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(resultData, HttpStatus.OK);
    }
}
