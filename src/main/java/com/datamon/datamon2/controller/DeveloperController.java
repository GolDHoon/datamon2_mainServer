package com.datamon.datamon2.controller;

import com.datamon.datamon2.dto.repository.UserBaseDto;
import com.datamon.datamon2.servcie.logic.DeveloperService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/developer")
public class DeveloperController {
    private DeveloperService developerService;

    public DeveloperController(DeveloperService developerService) {
        this.developerService = developerService;
    }

    @PostMapping("/userlist")
    public List<UserBaseDto> retrieveUserList(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<UserBaseDto> userList = developerService.getUserList();
        return userList;
    }

    @GetMapping("/checkServer")
    public Boolean checkServer(){
        return true;
    }

    @GetMapping("/passwordsetting")
    public String setPassword(@RequestParam int idx, @RequestParam String userPw){
        String result = developerService.setPassword(idx, userPw);
        return result;
    }
}
