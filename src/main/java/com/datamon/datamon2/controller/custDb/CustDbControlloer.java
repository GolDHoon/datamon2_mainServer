package com.datamon.datamon2.controller.custDb;

import com.datamon.datamon2.dto.input.custDb.BlockedIpCopyDto;
import com.datamon.datamon2.dto.input.custDb.BlockedIpInfoDto;
import com.datamon.datamon2.dto.input.custDb.LpgeCodeCreateDto;
import com.datamon.datamon2.dto.output.common.ErrorOutputDto;
import com.datamon.datamon2.dto.output.common.SuccessOutputDto;
import com.datamon.datamon2.dto.output.custDb.BlockedIpCopyOutputDto;
import com.datamon.datamon2.dto.output.custDb.GetCustDbCodeListOutputDto;
import com.datamon.datamon2.dto.output.custDb.GetLpgeCodeInfoOutputDto;
import com.datamon.datamon2.dto.output.custDb.GetLpgeDbListOutputDto;
import com.datamon.datamon2.servcie.logic.custDb.CustDbService;
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
@RequestMapping("/custDb")
@Tag(name = "고객DB", description = "고객DB 관련 API")
public class CustDbControlloer {
    private static final Logger logger = LogManager.getLogger(CustDbControlloer.class);
    private CustDbService custDbService;

    public CustDbControlloer(CustDbService custDbService) {
        this.custDbService = custDbService;
    }

    @PostMapping("/blockedIp/copy")
    @Operation(summary = "차단IP 복사 API", description = "차단IP를 복사하는 API.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "데이터 처리 성공.",
                    content = @Content(schema = @Schema(implementation = SuccessOutputDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.",
                    content = @Content(schema = @Schema(implementation = ErrorOutputDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류가 발생했습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorOutputDto.class)))
    })
    public ResponseEntity<?> copyBlockedIp(HttpServletRequest request, HttpServletResponse response, @RequestBody BlockedIpCopyDto blockedIpCopyDto) throws Exception{
        Map<String, Object> result;
        BlockedIpCopyOutputDto resultData;
        try {
            result = custDbService.copyBlockedIp(request, blockedIpCopyDto);

            if(result.get("result").toString().equals("S")){
                resultData = (BlockedIpCopyOutputDto) result.get("output");
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

    @PostMapping("/blockedIp/create")
    @Operation(summary = "차단IP 등록 API", description = "차단IP를 등록하는 API.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "데이터 처리 성공.",
                    content = @Content(schema = @Schema(implementation = SuccessOutputDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.",
                    content = @Content(schema = @Schema(implementation = ErrorOutputDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류가 발생했습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorOutputDto.class)))
    })
    public ResponseEntity<?> createBlockedIp(HttpServletRequest request, HttpServletResponse response, @RequestBody BlockedIpInfoDto blockedIpInfoDto) throws Exception{
        Map<String, Object> result;
        SuccessOutputDto resultData;
        try {
            result = custDbService.createBlockedIp(request, blockedIpInfoDto);

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
            logger.error(e.getMessage());
            return new ResponseEntity<>("serverEror", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(resultData, HttpStatus.OK);
    }

    @PostMapping("/blockedIp/delete")
    @Operation(summary = "차단IP 삭제 API", description = "차단IP를 삭제하는 API.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "데이터 처리 성공.",
                    content = @Content(schema = @Schema(implementation = SuccessOutputDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.",
                    content = @Content(schema = @Schema(implementation = ErrorOutputDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류가 발생했습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorOutputDto.class)))
    })
    public ResponseEntity<?> deleteBlockedIp(HttpServletRequest request, HttpServletResponse response, @RequestBody BlockedIpInfoDto blockedIpInfoDto) throws Exception{
        Map<String, Object> result;
        SuccessOutputDto resultData;
        try {
            result = custDbService.deleteBlockedIp(blockedIpInfoDto);

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
            logger.error(e.getMessage());
            return new ResponseEntity<>("serverEror", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(resultData, HttpStatus.OK);
    }

    @PostMapping("/lpge/create")
    @Operation(summary = "랜딩페이지DB 생성 API", description = "랜딩페이지DB를 생성하는 API.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "데이터 처리 성공.",
                    content = @Content(schema = @Schema(implementation = SuccessOutputDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.",
                    content = @Content(schema = @Schema(implementation = ErrorOutputDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류가 발생했습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorOutputDto.class)))
    })
    public ResponseEntity<?> createLpgeCode(HttpServletRequest request, HttpServletResponse response, @RequestBody LpgeCodeCreateDto lpgeCodeCreateDto) throws Exception{
        Map<String, Object> result;
        SuccessOutputDto resultData;
        try {
            result = custDbService.createLpgeCode(request, lpgeCodeCreateDto);

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
            logger.error(e.getMessage());
            return new ResponseEntity<>("serverEror", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(resultData, HttpStatus.OK);
    }

    @GetMapping("/lpge/info")
    @Operation(summary = "랜딩페이지DB 상세정보 조회 API", description = "랜딩페이지DB 상세정보를 조회하는 API.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "데이터 출력 성공.",
                    content = @Content(schema = @Schema(implementation = GetLpgeCodeInfoOutputDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.",
                    content = @Content(schema = @Schema(implementation = ErrorOutputDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류가 발생했습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorOutputDto.class)))
    })
    public ResponseEntity<?> getLpgeCodeInfo(HttpServletRequest request, HttpServletResponse response,
                                             @RequestParam
                                             @Parameter(description = "랜딩페이지 데이터 ID")
                                             int idx,
                                             @RequestParam
                                             @Parameter(description = "업체 데이터 ID")
                                             int companyId) throws Exception{
        Map<String, Object> result;
        GetLpgeCodeInfoOutputDto resultData;
        try {
            result = custDbService.getLpgeCodeInfo(request, idx, companyId);

            if(result.get("result").toString().equals("S")){
                resultData = (GetLpgeCodeInfoOutputDto) result.get("output");
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

    @GetMapping("/lpge/list")
    @Operation(summary = "랜딩페이지DB 목록 API", description = "요청한 유저의 보유 랜딩페이지DB 목록을 출력해주는 API \n※출력데이터는 array 형태로 출력된다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "데이터 출력 성공.",
                    content = @Content(schema = @Schema(implementation = GetLpgeDbListOutputDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.",
                    content = @Content(schema = @Schema(implementation = ErrorOutputDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류가 발생했습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorOutputDto.class)))
    })
    public ResponseEntity<?> getLpgeDbList(HttpServletRequest request, HttpServletResponse response) throws Exception{
        Map<String, Object> result;
        GetLpgeDbListOutputDto resultData;
        try {
            result = custDbService.getLpgeDbList(request);

            if(result.get("result").toString().equals("S")){
                resultData = (GetLpgeDbListOutputDto) result.get("output");
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

    @GetMapping("/list")
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
            result = custDbService.getCustDBCodeList(request);

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
}
