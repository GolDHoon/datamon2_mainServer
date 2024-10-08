package com.datamon.datamon2.dto.input.sign;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class LoginInuptDto {
    @Schema(description = "유저 계정")
    String userId;
    @Schema(description = "유저 패스워드")
    String password;
    @Schema(description = "업체 데이터 ID")
    int companyIdx;
}
