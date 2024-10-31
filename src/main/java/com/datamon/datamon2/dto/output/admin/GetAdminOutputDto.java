package com.datamon.datamon2.dto.output.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class GetAdminOutputDto {
    @Schema(description = "유저 계정")
    String userId;
    @Schema(description = "업체명")
    String name;
    @Schema(description = "대표자")
    String ceo;
    @Schema(description = "사업자등록번호")
    String corporateNumber;
    @Schema(description = "소재지")
    String corporateAddress;
    @Schema(description = "메일")
    String corporateMail;
    @Schema(description = "업태")
    String businessStatus;
    @Schema(description = "업종")
    String businessItem;
}
