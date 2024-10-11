package com.datamon.datamon2.dto.output.custInfo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class RowInfo {
    @Schema(description = "칼럼 이름")
    String keyName;
    @Schema(description = "원본 칼럼 이름")
    String originalKeyName;
    @Schema(description = "데이터 유형")
    String dataType;
    @Schema(description = "데이터")
    Object data;
}
