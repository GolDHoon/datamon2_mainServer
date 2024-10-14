package com.datamon.datamon2.dto.output.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SuccessOutputDto {
    @Schema(description = "메세지")
    String message;
    @Schema(description = "코드")
    int code;
}
