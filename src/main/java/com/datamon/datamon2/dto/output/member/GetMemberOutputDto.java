package com.datamon.datamon2.dto.output.member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class GetMemberOutputDto {
    @Schema(description = "유저 계정")
    String userId;
    @Schema(description = "유저 명")
    String name;
    @Schema(description = "역할")
    String role;
    @Schema(description = "연락처")
    String contactPhone;
    @Schema(description = "이메일")
    String contactMail;
}
