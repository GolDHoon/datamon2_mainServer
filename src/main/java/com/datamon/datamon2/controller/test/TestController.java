package com.datamon.datamon2.controller.test;

import com.datamon.datamon2.dto.input.test.Case1InputDto;
import com.datamon.datamon2.dto.output.common.ErrorOutputDto;
import com.datamon.datamon2.dto.output.test.Case1OutputDto;
import com.datamon.datamon2.dto.output.test.Case2OutputDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
@Tag(name = "Test", description = "스웨거 테스트 해보는거")
public class TestController {

    @GetMapping("/case1")
    @Operation(summary = "api 타이틀", description = "api 설명")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 응답이 반환되었습니다.",
                    content = @Content(schema = @Schema(implementation = Case1OutputDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.",
                    content = @Content(schema = @Schema(implementation = ErrorOutputDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류가 발생했습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorOutputDto.class)))
    })
    public ResponseEntity<?> case1 (HttpServletRequest request, HttpServletResponse response,
                                              @Parameter(description = "입력값 설명 여기에 작성")
                                              @RequestParam String input) {
        Case1OutputDto case1OutputDto = new Case1OutputDto();
        case1OutputDto.setResult("결과");

        return new ResponseEntity<>(case1OutputDto, HttpStatus.OK);
    }

    @PostMapping("/case2")
    @Operation(summary = "api 타이틀", description = "api 설명")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 응답이 반환되었습니다.",
                    content = @Content(schema = @Schema(implementation = Case2OutputDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.",
                    content = @Content(schema = @Schema(implementation = ErrorOutputDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류가 발생했습니다.",
                    content = @Content(schema = @Schema(implementation = ErrorOutputDto.class)))
    })
    public ResponseEntity<?> case2(HttpServletRequest request, HttpServletResponse response, @RequestBody Case1InputDto case1InputDto) throws Exception{
        Case2OutputDto case2OutputDto = new Case2OutputDto();
        return new ResponseEntity<>(case2OutputDto, HttpStatus.OK);
    }
}