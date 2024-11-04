package com.datamon.datamon2.dto.input.custDb;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class LandingPageInfoDto {
    @Schema(description = "DB 코드")
    String dbCode;
    @Schema(description = "head 태그")
    String head;
    @Schema(description = "body 태그")
    String body;
    @Schema(description = "title")
    String title;
    @Schema(description = "description")
    String description;
}
