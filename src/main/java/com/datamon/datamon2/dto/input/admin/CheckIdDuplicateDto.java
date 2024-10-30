package com.datamon.datamon2.dto.input.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CheckIdDuplicateDto {
    @Schema(description = "유저 계정")
    String userId;
}
