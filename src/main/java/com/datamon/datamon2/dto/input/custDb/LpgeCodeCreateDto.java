package com.datamon.datamon2.dto.input.custDb;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class LpgeCodeCreateDto {
    @Schema(description = "url")
    String url;
    @Schema(description = "설명")
    String description;
}
