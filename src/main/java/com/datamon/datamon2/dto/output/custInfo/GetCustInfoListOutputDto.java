package com.datamon.datamon2.dto.output.custInfo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class GetCustInfoListOutputDto {
    @Schema(description = "칼럼 정보 목록")
    List<ColumnInfo> columnInfoList;
    @Schema(description = "데이터 목록")
    List<Map<String, Object>> DataList;
}
