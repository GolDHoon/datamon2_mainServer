package com.datamon.datamon2.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @RequestMapping("/login")
    public ResponseEntity<Object> login(@RequestParam String userId, @RequestParam String password){

        return null;
    }
}
