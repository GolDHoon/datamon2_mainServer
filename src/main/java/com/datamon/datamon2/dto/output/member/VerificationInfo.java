package com.datamon.datamon2.dto.output.member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class VerificationInfo {
    @Schema(description = "데이터 ID")
    String idx;
    @Schema(description = "인증번호")
    String verificationCode;
}
