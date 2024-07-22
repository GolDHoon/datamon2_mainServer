package com.datamon.datamon2.dto.input.user;

import lombok.Data;

@Data
public class CreateCompanyUserDto {
    String userId;
    String pw;
    String name;
    String ceo;
    String userType;
    String corporateNumber;
    String corporateAddress;
    String corporateMail;
    String businessStatus;
    String businessItem;
}
