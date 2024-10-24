package com.datamon.datamon2.dto.input.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AdminAccountDto {
    @Schema(description = "유저 ID")
    String userId;
    @Schema(description = "유저 PW")
    String userPw;
    @Schema(description = "업체명")
    String name;
    @Schema(description = "유저유형")
    String userType;
    @Schema(description = "대표자명")
    String ceo;
    @Schema(description = "사업자등록번호")
    String corporateNumber;
    @Schema(description = "소재지")
    String corporateAddress;
    @Schema(description = "이메일")
    String corporateMail;
    @Schema(description = "업태")
    String businessStatus;
    @Schema(description = "업종")
    String businessItem;
    @Schema(description = "신청사유")
    String requestReason;
}
