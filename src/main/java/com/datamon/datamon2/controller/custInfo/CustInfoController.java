package com.datamon.datamon2.controller.custInfo;

import com.datamon.datamon2.dto.input.custInfo.CustInfoDto;
import com.datamon.datamon2.dto.input.custInfo.ModifyCustInfoDto;
import com.datamon.datamon2.dto.output.common.ErrorOutputDto;
import com.datamon.datamon2.servcie.logic.custInfo.CustInfoService;
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
@RequestMapping("/custInfo")
@Tag(name = "고객정보", description = "고객정보 관련 API")
public class CustInfoController {
    private static final Logger logger = LogManager.getLogger(CustInfoController.class);
    private CustInfoService custInfoService;

    public CustInfoController(CustInfoService custInfoService) {
        this.custInfoService = custInfoService;
    }

    @GetMapping("/list")
    @Operation(summary = "고객정보 목록 API", description = "고객정보 목록을 출력해주는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "데이터 출력 성공."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.",
                    content = @Content(schema = @Schema(implementation = ErrorOutputDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류가 발생했습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorOutputDto.class)))
    })
    public ResponseEntity<?> getCustInfoList(HttpServletRequest request, HttpServletResponse response
            , @Parameter(description = "고객DB유형") @RequestParam String custDBType
            , @Parameter(description = "고객DB코드") @RequestParam String custDBCode) throws Exception{
         return null;
    }

    @GetMapping("/custDBCode/list")
    @Operation(summary = "고객정보 목록 API", description = "고객정보 목록을 출력해주는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "데이터 출력 성공."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.",
                    content = @Content(schema = @Schema(implementation = ErrorOutputDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류가 발생했습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorOutputDto.class)))
    })
    public ResponseEntity<?> getCustDBCodeList(HttpServletRequest request, HttpServletResponse response) throws Exception{
        return null;
    }

    @PostMapping("/list2")
    public ResponseEntity<?> getList2(HttpServletRequest request, HttpServletResponse response, @RequestBody CustInfoDto custInfoDto) throws Exception{
        Map<String, Object> result;
        try {
            result= custInfoService.getListByLpgeCode(custInfoDto);
        } catch (Exception e) {
            logger.error(e);
            return new ResponseEntity<>("fail - serverEror", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/unUseCustInfo")
    public ResponseEntity<?> modifyCustInfo(HttpServletRequest request, HttpServletResponse response, @RequestBody ModifyCustInfoDto modifyCustInfoDto){
        String result;
        try {
            result = custInfoService.modifyCustInfo(request, modifyCustInfoDto, "useYn");
        } catch (Exception e) {
            logger.error(e);
            return new ResponseEntity<>("fail - serverEror", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/deleteCustInfo")
    public ResponseEntity<?> deleteCustInfo(HttpServletRequest request, HttpServletResponse response, @RequestBody ModifyCustInfoDto modifyCustInfoDto){
        String result;
        try {
            result = custInfoService.modifyCustInfo(request, modifyCustInfoDto, "delYn");
        } catch (Exception e) {
            logger.error(e);
            return new ResponseEntity<>("fail - serverEror", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
