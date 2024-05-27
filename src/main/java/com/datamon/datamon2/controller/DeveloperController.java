package com.datamon.datamon2.controller;

import com.datamon.datamon2.dto.repository.UserBaseDto;
import com.datamon.datamon2.servcie.logic.DeveloperService;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@EnableJdbcHttpSession
@RestController
@RequestMapping("/developer")
public class DeveloperController {
    private DeveloperService developerService;

    public DeveloperController(DeveloperService developerService) {
        this.developerService = developerService;
    }

    @PostMapping("/userlist")
    public List<UserBaseDto> retrieveUserList(){
        return developerService.getUserList();
    }

    @GetMapping("/passwordsetting")
    public String setPassword(@RequestParam int idx, @RequestParam String userPw){
        String result = developerService.setPassword(idx, userPw);
        return result;
    }
}
