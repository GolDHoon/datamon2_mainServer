package com.datamon.datamon2.dto.output.sign;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class LoginOutputDto {
    @Schema(description = "업체 데이터 ID")
    int companyIdx;
}
