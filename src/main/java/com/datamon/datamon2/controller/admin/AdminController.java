package com.datamon.datamon2.controller.admin;

import com.datamon.datamon2.dto.input.admin.AdminAccountDto;
import com.datamon.datamon2.dto.input.member.MemberAccountDto;
import com.datamon.datamon2.dto.input.test.Case1InputDto;
import com.datamon.datamon2.dto.input.user.*;
import com.datamon.datamon2.dto.output.admin.GetAdminListOutputDto;
import com.datamon.datamon2.dto.output.admin.GetRequestAdminAccountListOutputDto;
import com.datamon.datamon2.dto.output.common.ErrorOutputDto;
import com.datamon.datamon2.dto.output.common.SuccessOutputDto;
import com.datamon.datamon2.dto.output.member.GetMemberListOutputDto;
import com.datamon.datamon2.dto.output.member.GetRequestMemberAccountListOutputDto;
import com.datamon.datamon2.dto.output.test.Case1OutputDto;
import com.datamon.datamon2.dto.output.test.Case2OutputDto;
import com.datamon.datamon2.servcie.logic.UserService;
import com.datamon.datamon2.servcie.logic.admin.AdminService;
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
    private AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/list")
    @Operation(summary = "admin계정정보 목록 API", description = "admin계정정보 목록을 출력해주는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "데이터 출력 성공.",
                    content = @Content(schema = @Schema(implementation = GetAdminListOutputDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.",
                    content = @Content(schema = @Schema(implementation = ErrorOutputDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류가 발생했습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorOutputDto.class)))
    })
    public ResponseEntity<?> getAdminList(HttpServletRequest request, HttpServletResponse response) throws Exception{
        Map<String, Object> result;
        GetAdminListOutputDto resultData;

        try {
            result = adminService.getAdminList(request);

            if(result.get("result").toString().equals("S")){
                resultData = (GetAdminListOutputDto) result.get("output");
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
    @Operation(summary = "admin 계정 신청 API", description = "admin 계정을 신청하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "데이터 출력 성공.",
                    content = @Content(schema = @Schema(implementation = SuccessOutputDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.",
                    content = @Content(schema = @Schema(implementation = ErrorOutputDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류가 발생했습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorOutputDto.class)))
    })
    public ResponseEntity<?> requestAdminAccount(HttpServletRequest request, HttpServletResponse response
            , @RequestBody AdminAccountDto adminAccountDto) throws Exception {
        Map<String, Object> result;
        SuccessOutputDto resultData;

        try {
            result = adminService.requestAdminAccount(adminAccountDto);

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
    @Operation(summary = "admin 계정신청정보 목록 API", description = "admin계정신청정보 목록을 출력해주는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "데이터 출력 성공.",
                    content = @Content(schema = @Schema(implementation = GetRequestAdminAccountListOutputDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.",
                    content = @Content(schema = @Schema(implementation = ErrorOutputDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류가 발생했습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorOutputDto.class)))
    })
    public ResponseEntity<?> getRequestMemberAccountList(HttpServletRequest request, HttpServletResponse response) throws Exception{
        Map<String, Object> result;
        GetRequestAdminAccountListOutputDto resultData;

        try {
            result = adminService.getRequestAdminAccountList(request);

            if(result.get("result").toString().equals("S")){
                resultData = (GetRequestAdminAccountListOutputDto) result.get("output");
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

}
