package com.datamon.datamon2.controller.admin;

import com.datamon.datamon2.dto.input.test.Case1InputDto;
import com.datamon.datamon2.dto.input.user.*;
import com.datamon.datamon2.dto.output.common.ErrorOutputDto;
import com.datamon.datamon2.dto.output.test.Case1OutputDto;
import com.datamon.datamon2.dto.output.test.Case2OutputDto;
import com.datamon.datamon2.servcie.logic.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@RequestMapping("/admin")
@Tag(name = "admin", description = "admin계정 관련 API")
public class AdminController {
    private static final Logger logger = LogManager.getLogger(AdminController.class);
    private UserService userService;

    @PostMapping("/approve/req")
    @Operation(summary = "admin계정 신청", description = "admin계정의 신청에 사용되는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 응답이 반환되었습니다.",
                    content = @Content(schema = @Schema(implementation = Case2OutputDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.",
                    content = @Content(schema = @Schema(implementation = ErrorOutputDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류가 발생했습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorOutputDto.class)))
    })
    public ResponseEntity<?> requestApprove(HttpServletRequest request, HttpServletResponse response, @RequestBody Case1InputDto case1InputDto) throws Exception{
        Case2OutputDto case2OutputDto = new Case2OutputDto();
        return new ResponseEntity<>(case2OutputDto, HttpStatus.OK);
    }

    @GetMapping("/approve/list")
    @Operation(summary = "admin계정 등록신청 목록", description = "admin계정 등록신청 목록을 제공하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 응답이 반환되었습니다.",
                    content = @Content(schema = @Schema(implementation = Case1OutputDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.",
                    content = @Content(schema = @Schema(implementation = ErrorOutputDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류가 발생했습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorOutputDto.class)))
    })
    public ResponseEntity<?> getApproveList (HttpServletRequest request, HttpServletResponse response,
                                              @Parameter(description = "입력값 설명 여기에 작성")
                                              @RequestParam String input) {
        Case1OutputDto case1OutputDto = new Case1OutputDto();
        case1OutputDto.setResult("결과");

        return new ResponseEntity<>(case1OutputDto, HttpStatus.OK);
    }

    @PostMapping("/approve/res")
    @Operation(summary = "admin계정 신청 응답", description = "admin 계정 신청에 대한 응답 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 응답이 반환되었습니다.",
                    content = @Content(schema = @Schema(implementation = Case2OutputDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.",
                    content = @Content(schema = @Schema(implementation = ErrorOutputDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류가 발생했습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorOutputDto.class)))
    })
    public ResponseEntity<?> responseApprove(HttpServletRequest request, HttpServletResponse response, @RequestBody Case1InputDto case1InputDto) throws Exception{
        Case2OutputDto case2OutputDto = new Case2OutputDto();
        return new ResponseEntity<>(case2OutputDto, HttpStatus.OK);
    }



    @GetMapping("/list")
    @Operation(summary = "admin계정 목록", description = "admin계정 목록을 제공하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 응답이 반환되었습니다.",
                    content = @Content(schema = @Schema(implementation = Case1OutputDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.",
                    content = @Content(schema = @Schema(implementation = ErrorOutputDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류가 발생했습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorOutputDto.class)))
    })
    public ResponseEntity<?> getList (HttpServletRequest request, HttpServletResponse response,
                                             @Parameter(description = "입력값 설명 여기에 작성")
                                             @RequestParam String input) {
        Case1OutputDto case1OutputDto = new Case1OutputDto();
        case1OutputDto.setResult("결과");

        return new ResponseEntity<>(case1OutputDto, HttpStatus.OK);
    }

    @PostMapping("/modify/master")
    @Operation(summary = "admin계정 수정", description = "admin 계정을 마스터가 수정하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 응답이 반환되었습니다.",
                    content = @Content(schema = @Schema(implementation = Case2OutputDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.",
                    content = @Content(schema = @Schema(implementation = ErrorOutputDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류가 발생했습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorOutputDto.class)))
    })
    public ResponseEntity<?> modifyByMaster(HttpServletRequest request, HttpServletResponse response, @RequestBody Case1InputDto case1InputDto) throws Exception{
        Case2OutputDto case2OutputDto = new Case2OutputDto();
        return new ResponseEntity<>(case2OutputDto, HttpStatus.OK);
    }

    @PostMapping("/modify/admin")
    @Operation(summary = "admin계정 수정요청", description = "admin 계정을 admin 수정요청하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 응답이 반환되었습니다.",
                    content = @Content(schema = @Schema(implementation = Case2OutputDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.",
                    content = @Content(schema = @Schema(implementation = ErrorOutputDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류가 발생했습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorOutputDto.class)))
    })
    public ResponseEntity<?> modifyByAdmin(HttpServletRequest request, HttpServletResponse response, @RequestBody Case1InputDto case1InputDto) throws Exception{
        Case2OutputDto case2OutputDto = new Case2OutputDto();
        return new ResponseEntity<>(case2OutputDto, HttpStatus.OK);
    }

    @PostMapping("/delete")
    @Operation(summary = "admin 계정 삭제", description = "api 설명")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 응답이 반환되었습니다.",
                    content = @Content(schema = @Schema(implementation = Case2OutputDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.",
                    content = @Content(schema = @Schema(implementation = ErrorOutputDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류가 발생했습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorOutputDto.class)))
    })
    public ResponseEntity<?> delete(HttpServletRequest request, HttpServletResponse response, @RequestBody Case1InputDto case1InputDto) throws Exception{
        Case2OutputDto case2OutputDto = new Case2OutputDto();
        return new ResponseEntity<>(case2OutputDto, HttpStatus.OK);
    }

//    @GetMapping("/list")
//    public ResponseEntity<?> getList(HttpServletRequest request, HttpServletResponse response, @RequestParam("listType") String listType) throws Exception{
//        Map<String, ?> result;
//
//        try {
//            if(listType.equals("user")){
//                result = userService.getListUser(request);
//            }else {
//                result = userService.getListCompany(request);
//            }
//        } catch (Exception e) {
//            logger.error(e.getMessage());
//            return new ResponseEntity<>("fail - serverEror", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//
//        return new ResponseEntity<>(result, HttpStatus.OK);
//    }
//
//    @PostMapping("/createCompanyUser")
//    public ResponseEntity<?> createCompanyUser(HttpServletRequest request, HttpServletResponse response, @RequestBody CreateCompanyUserDto createCompanyUserDto) throws Exception {
//        String result;
//        try {
//            result = userService.createCompanyUser(request, createCompanyUserDto);
//        } catch (Exception e) {
//            return new ResponseEntity<>("fail - serverEror", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//
//        return new ResponseEntity<>(result, HttpStatus.OK);
//    }
//
//    @PostMapping("/deleteCompanyUser")
//    public ResponseEntity<?> deleteCompanyUser(HttpServletRequest request, HttpServletResponse response, @RequestBody DeleteCompanyUserDto deleteCompanyUserDto) throws Exception {
//        String result;
//        try {
//            result = userService.deleteCompanyUser(request, deleteCompanyUserDto);
//        } catch (Exception e) {
//            return new ResponseEntity<>("fail - serverEror", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//
//        return new ResponseEntity<>(result, HttpStatus.OK);
//    }
//
//    @PostMapping("/createMemberUser")
//    public ResponseEntity<?> createMemberUser(HttpServletRequest request, HttpServletResponse response, @RequestBody CreateMemberUserDto createMemberUserDto) throws Exception {
//        String result;
//        try {
//            result = userService.createMemberUser(request, createMemberUserDto);
//        } catch (Exception e) {
//            return new ResponseEntity<>("fail - serverEror", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//
//        return new ResponseEntity<>(result, HttpStatus.OK);
//    }
//
//    @PostMapping("/deleteMemberUser")
//    public ResponseEntity<?> deleteCompanyUser(HttpServletRequest request, HttpServletResponse response, @RequestBody DeleteMemberUserDto deleteMemberUserDto) throws Exception {
//        String result;
//        try {
//            result = userService.deleteMemberUser(request, deleteMemberUserDto);
//        } catch (Exception e) {
//            return new ResponseEntity<>("fail - serverEror", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//
//        return new ResponseEntity<>(result, HttpStatus.OK);
//    }
//
//    @PostMapping("/checkMemberIdDuplicate")
//    public ResponseEntity<?> checkMemberIdDuplicate(HttpServletRequest request, HttpServletResponse response, @RequestBody CheckIdDuplicateDto checkIdDuplicateDto) throws  Exception {
//        String result;
//        try {
//            result = userService.checkMemberIdDuplicate(checkIdDuplicateDto);
//        } catch (Exception e) {
//            return new ResponseEntity<>("fail - serverEror", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//
//        return new ResponseEntity<>(result, HttpStatus.OK);
//    }
//
//    @PostMapping("/checkCompanyIdDuplicate")
//    public ResponseEntity<?> checkCompanyIdDuplicate(HttpServletRequest request, HttpServletResponse response, @RequestBody CheckIdDuplicateDto checkIdDuplicateDto) throws  Exception {
//        String result;
//        try {
//            result = userService.checkCompanyIdDuplicate(checkIdDuplicateDto);
//        } catch (Exception e) {
//            return new ResponseEntity<>("fail - serverEror", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//
//        return new ResponseEntity<>(result, HttpStatus.OK);
//    }
}
