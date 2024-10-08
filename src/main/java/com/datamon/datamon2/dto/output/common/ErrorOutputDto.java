package com.datamon.datamon2.dto.output.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ErrorOutputDto {
    @Schema(description = "상세사유")
    String detailReason;
    @Schema(description = "에러코드")
    int code;
}
