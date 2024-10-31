package com.datamon.datamon2.dto.input.member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class MemberAccountDto {
    @Schema(description = "신청구분")
    String requestType;
    @Schema(description = "유저 ID")
    String userId;
    @Schema(description = "유저 PW")
    String userPw;
    @Schema(description = "업체 데이터 id")
    Integer companyId;
    @Schema(description = "유저명")
    String name;
    @Schema(description = "유저 역할")
    String role;
    @Schema(description = "유저 전화번호")
    String contactPhone;
    @Schema(description = "유저 이메일")
    String contactMail;
    @Schema(description = "신청사유")
    String requestReason;
}
