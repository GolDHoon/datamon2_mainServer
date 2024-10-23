package com.datamon.datamon2.dto.input.member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CheckIdDuplicateDto {
    @Schema(description = "유저 계정")
    String userId;
    @Schema(description = "업체 데이터 ID")
    int companyId;
}
