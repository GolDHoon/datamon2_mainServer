package com.datamon.datamon2.dto.input.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AdminUserInfoDto {
    @Schema(description = "유저 데이터 ID(수정, 삭제 시 필수값)")
    int idx;
    @Schema(description = "유저 ID(생성 시 필수값)")
    String userId;
    @Schema(description = "유저 PW(생성 시 필수값)")
    String userPw;
    @Schema(description = "유저 유형(생성, 수정 시 필수값)")
    String userType;
    @Schema(description = "업체명(생성, 수정 시 필수값)")
    String name;
    @Schema(description = "대표자 명(생성, 수정 시 필수값)")
    String ceo;
    @Schema(description = "사업자등록번호(생성, 수정 시 필수값)")
    String corporateNumber;
    @Schema(description = "소재지(생성, 수정 시 필수값)")
    String corporateAddress;
    @Schema(description = "이메일(생성, 수정 시 필수값)")
    String corporateMail;
    @Schema(description = "업종(생성, 수정 시 필수값)")
    String businessStatus;
    @Schema(description = "업태(생성, 수정 시 필수값)")
    String businessItem;
}
