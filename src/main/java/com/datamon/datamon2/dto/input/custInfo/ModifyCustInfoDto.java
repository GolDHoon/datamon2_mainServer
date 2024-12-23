package com.datamon.datamon2.dto.input.custInfo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ModifyCustInfoDto {
    @Schema(description = "고객정보 데이터 ID")
    String custInfoIdx;
    @Schema(description = "수정데이터")
    List<Map<String, Object>> dataList;
}
