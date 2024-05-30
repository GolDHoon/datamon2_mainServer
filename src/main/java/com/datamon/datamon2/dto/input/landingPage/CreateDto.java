package com.datamon.datamon2.dto.input.landingPage;

import lombok.Data;

@Data
public class CreateDto {
    int createId;
    int userId;
    String inputMode;
    String password;
    String pageDescription;
}