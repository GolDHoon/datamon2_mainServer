package com.datamon.datamon2.dto.output.custInfo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class GetCustDbCodeListOutputDto {
    @Schema(description = "고객DB유형")
    String custDbType;
    @Schema(description = "고객DB코드")
    List<String> custDbCodeList;
}
