package com.datamon.datamon2.dto.input.member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class MemberUserInfoDto {
    @Schema(description = "유저 데이터 ID(수정, 삭제 시 필수값)")
    int idx;
    @Schema(description = "유저 ID(생성 시 필수값)")
    String userId;
    @Schema(description = "유저 PW(생성시 필수값)")
    String pw;
    @Schema(description = "유저명(생성, 수정 시 필수값)")
    String name;
    @Schema(description = "역할(생성, 수정 시 필수값)")
    String role;
    @Schema(description = "유저 전화번호(생성, 수정 시 필수값)")
    String contactPhone;
    @Schema(description = "유저 메일(생성, 수정 시 필수값)")
    String mail;
}
