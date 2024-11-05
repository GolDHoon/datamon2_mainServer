package com.datamon.datamon2.controller.home;

import com.datamon.datamon2.dto.output.common.ErrorOutputDto;
import com.datamon.datamon2.servcie.logic.performance.PerformanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/home")
public class HomeController {
    private static final Logger logger = LogManager.getLogger(HomeController.class);
    private PerformanceService performanceService;

    public HomeController(PerformanceService performanceService) {
        this.performanceService = performanceService;
    }

    @GetMapping("/performance")
    @Operation(summary = "home 수집통계 조회 API", description = "home 수집통계를 조회하는 API.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "데이터 출력 성공."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.",
                    content = @Content(schema = @Schema(implementation = ErrorOutputDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류가 발생했습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorOutputDto.class)))
    })
    public ResponseEntity<?> getCollectionPerformanceData(HttpServletRequest request, HttpServletResponse response) throws Exception{
        Map<String, Object> result;
        List<Map<String, Object>> resultData;
        try {
            result = performanceService.getCollectionPerformanceData(request, null);

            if(result.get("result").toString().equals("S")){
                resultData = (List<Map<String, Object>>) result.get("output");
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
