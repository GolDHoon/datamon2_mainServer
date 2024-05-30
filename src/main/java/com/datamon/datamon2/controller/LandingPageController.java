package com.datamon.datamon2.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
