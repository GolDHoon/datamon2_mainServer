package com.datamon.datamon2.controller;

import com.datamon.datamon2.dto.input.user.LoginInuptDto;
import com.datamon.datamon2.servcie.logic.UserSignService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;
import org.springframework.web.bind.annotation.*;

@EnableJdbcHttpSession
@RestController
public class UserSignController {
    private static final Logger logger = LogManager.getLogger(UserSignController.class);
    private UserSignService userSignService;

    public UserSignController(UserSignService userSignService) {
        this.userSignService = userSignService;
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginInuptDto loginInuptDto, HttpServletRequest request, HttpServletResponse response){
        String result;
        try {
            result = userSignService.userLogin(loginInuptDto.getUserId(), loginInuptDto.getPassword(), request, response);

        } catch (Exception e) {
            logger.error(e.getMessage());
            result = "server-fail";
        }

        return result;
    }

    @PostMapping("/sessionCheck")
    public String sessionCheck(HttpServletRequest request, HttpServletResponse response){
        try {
            userSignService.sessionTimeReset(request);
            return "success";
        } catch (Exception e) {
            return "server-fail";
        }
    }

    @PostMapping("/sessionCheck2")
    public String sessionCheck2(HttpServletRequest request, HttpServletResponse response){
        try {
            userSignService.sessionCheck(request);
            return "success";
        } catch (Exception e) {

            return "server-fail";
        }
    }

}
