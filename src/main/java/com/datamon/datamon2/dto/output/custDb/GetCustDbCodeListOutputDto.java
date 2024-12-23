package com.datamon.datamon2.dto.output.custDb;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class GetCustDbCodeListOutputDto {
    @Schema(description = "고객DB유형")
    String custDbType;
    @Schema(description = "고객DB코드")
    List<Map> custDbCodeList;
}
