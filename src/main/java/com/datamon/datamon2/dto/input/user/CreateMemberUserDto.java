package com.datamon.datamon2.dto.input.user;

import lombok.Data;

@Data
public class CreateMemberUserDto {
    String userId;
    String pw;
    String name;
    String role;
    String contactPhone;
    String mail;
}
