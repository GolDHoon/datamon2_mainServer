package com.datamon.datamon2.controller.sign;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sign")
@Tag(name = "sing", description = "로그인 및 세션관련 API")
public class SignController {
    @GetMapping("/case1")
    @Operation(summary = "이건 어디에 뜨는거야1?", description = "이건 어디에 뜨는거야2?")
    public ResponseEntity<?> getEmployeeList (HttpServletRequest request, HttpServletResponse response, @RequestParam String input) {
        Map<String, Object> result = new HashMap<>();


        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
