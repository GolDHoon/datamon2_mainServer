package com.datamon.datamon2.dto.output.test;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class Case1OutputDto {
    @Schema(description = "결과 값")
    private String result;
}
