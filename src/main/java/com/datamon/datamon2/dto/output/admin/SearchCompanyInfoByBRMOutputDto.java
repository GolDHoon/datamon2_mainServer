package com.datamon.datamon2.dto.output.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SearchCompanyInfoByBRMOutputDto {
    @Schema(description = "사업자 계정")
    String companyId;
}
