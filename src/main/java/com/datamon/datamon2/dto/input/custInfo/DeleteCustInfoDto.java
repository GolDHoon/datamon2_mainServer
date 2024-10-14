package com.datamon.datamon2.dto.input.custInfo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class DeleteCustInfoDto {
    @Schema(description = "고객정보 데이터 ID")
    String custInfoIdx;
}
