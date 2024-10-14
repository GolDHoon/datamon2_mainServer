package com.datamon.datamon2.controller.custInfo;

import com.datamon.datamon2.dto.input.custInfo.DeleteCustInfoDto;
import com.datamon.datamon2.dto.input.custInfo.ModifyCustInfoDto;
import com.datamon.datamon2.dto.output.common.ErrorOutputDto;
import com.datamon.datamon2.dto.output.custInfo.GetCustDbCodeListOutputDto;
import com.datamon.datamon2.dto.output.custInfo.GetCustInfoListOutputDto;
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

import java.util.List;
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
            @ApiResponse(responseCode = "200", description = "데이터 출력 성공.",
                    content = @Content(schema = @Schema(implementation = GetCustInfoListOutputDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.",
                    content = @Content(schema = @Schema(implementation = ErrorOutputDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류가 발생했습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorOutputDto.class)))
    })
    public ResponseEntity<?> getCustInfoList(HttpServletRequest request, HttpServletResponse response
            , @Parameter(description = "고객DB유형") @RequestParam String custDBType
            , @Parameter(description = "고객DB코드") @RequestParam String custDBCode) throws Exception{
        Map<String, Object> result;
        Object resultData;
        try {
            result = custInfoService.getCustInfoList(request, custDBType, custDBCode);

            if(result.get("result").toString().equals("S")){
                resultData = result.get("output");
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

    @GetMapping("/custDBCode/list")
    @Operation(summary = "고객DB 목록 API", description = "요청한 유저의 보유 고객DB코드 목록을 출력해주는 API \n※출력데이터는 array 형태로 출력된다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "데이터 출력 성공.",
                    content = @Content(schema = @Schema(implementation = GetCustDbCodeListOutputDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.",
                    content = @Content(schema = @Schema(implementation = ErrorOutputDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류가 발생했습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorOutputDto.class)))
    })
    public ResponseEntity<?> getCustDBCodeList(HttpServletRequest request, HttpServletResponse response) throws Exception{
        Map<String, Object> result;
        List<GetCustDbCodeListOutputDto> resultData;
        try {
            result = custInfoService.getCustDBCodeList(request);

            if(result.get("result").toString().equals("S")){
                resultData = (List) result.get("output");
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

    @PostMapping("/delete")
    @Operation(summary = "고객정보 삭제", description = "요청한 고객정보를 삭제하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "데이터 출력 성공."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.",
                    content = @Content(schema = @Schema(implementation = ErrorOutputDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류가 발생했습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorOutputDto.class)))
    })
    public ResponseEntity<?> deleteCustInfo(HttpServletRequest request, HttpServletResponse response, @RequestBody DeleteCustInfoDto deleteCustInfoDto){
        String result;
        try {
            result = custInfoService.deleteCustInfo(request, deleteCustInfoDto);
        } catch (Exception e) {
            logger.error(e);
            return new ResponseEntity<>("fail - serverEror", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/modify")
    @Operation(summary = "고객정보 수정", description = "고객정보를 수정하는 API,\n※입력데이터에 대한 정의, 수정시 프론트쪽과 합의가 필요함.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "데이터 출력 성공."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.",
                    content = @Content(schema = @Schema(implementation = ErrorOutputDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류가 발생했습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorOutputDto.class)))
    })
    public ResponseEntity<?> modifyCustInfo(HttpServletRequest request, HttpServletResponse response, @RequestBody ModifyCustInfoDto modifyCustInfoDto){
        String result;
        try {
            result = custInfoService.modifyCustInfo(request, modifyCustInfoDto);
        } catch (Exception e) {
            logger.error(e);
            return new ResponseEntity<>("fail - serverEror", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
