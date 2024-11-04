package com.datamon.datamon2.dto.input.custDb;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class BlockedIpCopyDto {
    @Schema(description = "대상 DB 코드")
    String targetDbCode;
    @Schema(description = "적용 DB 코드")
    String applyDbCode;
}
