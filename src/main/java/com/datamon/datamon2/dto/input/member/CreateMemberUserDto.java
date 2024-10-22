package com.datamon.datamon2.dto.input.member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CreateMemberUserDto {
    @Schema(description = "유저 ID")
    String userId;
    @Schema(description = "유저 PW")
    String pw;
    @Schema(description = "유저명")
    String name;
    @Schema(description = "역할")
    String role;
    @Schema(description = "유저 전화번호")
    String contactPhone;
    @Schema(description = "유저 메일")
    String mail;
    @Schema(description = "업체 데이터 id")
    String companyId;
}
