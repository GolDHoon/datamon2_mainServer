package com.datamon.datamon2.dto.input.test;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class Case1InputDto {
    @Schema(description = "입력 값")
    private String input;
}
