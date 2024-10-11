package com.datamon.datamon2.dto.output.custInfo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ColumnInfo {
    @Schema(description = "필터 타입")
    String filterType;
    @Schema(description = "칼럼 타입")
    String columnType;
    @Schema(description = "칼럼 이름")
    String name;
    @Schema(description = "원본 칼럼 이름")
    String key;
}
