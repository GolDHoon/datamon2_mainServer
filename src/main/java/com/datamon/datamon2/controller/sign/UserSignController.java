package com.datamon.datamon2.controller.sign;

import com.datamon.datamon2.dto.input.sign.LoginInuptDto;
import com.datamon.datamon2.dto.output.common.ErrorOutputDto;
import com.datamon.datamon2.dto.output.sign.LoginOutputDto;
import com.datamon.datamon2.dto.output.test.Case1OutputDto;
import com.datamon.datamon2.servcie.logic.UserSignService;
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
@RequestMapping("/sign")
@Tag(name = "sing", description = "로그인 및 세션관련 API")
public class UserSignController {
    private static final Logger logger = LogManager.getLogger(com.datamon.datamon2.controller.sign.UserSignController.class);
    private UserSignService userSignService;

    public UserSignController(UserSignService userSignService) {
        this.userSignService = userSignService;
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "api 설명")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공.",
                    content = @Content(schema = @Schema(implementation = Case1OutputDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.",
                    content = @Content(schema = @Schema(implementation = ErrorOutputDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류가 발생했습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorOutputDto.class)))
    })
    public ResponseEntity<?> login(@RequestBody LoginInuptDto loginInuptDto, HttpServletRequest request, HttpServletResponse response){
        Map<String, Object> result;
        LoginOutputDto resultData;
        try {
            result = userSignService.userLogin(loginInuptDto, request, response);
            if(result.get("result").toString().equals("S")){
                resultData = (LoginOutputDto) result.get("output");
            }else{
                ErrorOutputDto errorOutputDto = (ErrorOutputDto) result.get("output");

                if(errorOutputDto.getCode() < 500){
                    return new ResponseEntity<>(errorOutputDto.getDetailReason(), HttpStatus.INTERNAL_SERVER_ERROR);
                }else{
                    return new ResponseEntity<>(errorOutputDto.getDetailReason(), HttpStatus.BAD_REQUEST);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>("serverEror", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(resultData, HttpStatus.OK);
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

    @GetMapping("/getCompanyName")
    public ResponseEntity<?> getCompanyName(HttpServletRequest request, HttpServletResponse response, @RequestParam String companyId){
        String result;
        try {
            result = userSignService.getCompanyName(companyId);
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
