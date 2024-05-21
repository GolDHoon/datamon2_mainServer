package com.datamon.datamon2.controller;

import com.datamon.datamon2.dto.input.user.LoginInuptDto;
import com.datamon.datamon2.servcie.logic.UserSignService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserSignController {
    private UserSignService userSignService;

    public UserSignController(UserSignService userSignService) {
        this.userSignService = userSignService;
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginInuptDto loginInuptDto, HttpServletRequest request, HttpServletResponse response){
        String result;
        try {
            result = userSignService.userLogin(loginInuptDto.getUserId(), loginInuptDto.getPassword(), request);

        } catch (Exception e) {
            result = "error";
        }

        return result;
    }

    @PostMapping("/sessionCheck")
    public String sessionCheck(HttpServletRequest request, HttpServletResponse response){
        String result;

        try {
            result = userSignService.sessionCheck(request);
        } catch (Exception e) {
            result = "error";
        }
        return result;
    }
}
