package com.datamon.datamon2.dto.input.landingPage;

import lombok.Data;

@Data
public class CreateDto {
    int createId;
    int userId;
    String inputMode;
    String mode;
    String password;
    String pageDescription;
    String domain;
}