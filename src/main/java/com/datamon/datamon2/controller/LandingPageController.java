package com.datamon.datamon2.controller;

import com.datamon.datamon2.dto.input.landingPage.DomainDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;
import org.springframework.web.bind.annotation.*;

import java.net.*;
import java.util.Enumeration;

@EnableJdbcHttpSession
@RestController
@RequestMapping("/landingpage")
public class LandingPageController {

    @GetMapping("/getIp")
    public String getIp(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return request.getHeader("X-Forwarded-For");
    }

    @PostMapping("/inputLaning")
    public String registerLandingPage(HttpServletRequest request, HttpServletResponse response, @RequestBody DomainDto domainDto) throws Exception{
        String serverName = request.getServerName();
        return "S";
    }
}
