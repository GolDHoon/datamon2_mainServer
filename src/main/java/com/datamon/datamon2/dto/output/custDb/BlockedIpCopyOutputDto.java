package com.datamon.datamon2.dto.output.custDb;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class BlockedIpCopyOutputDto {
    @Schema(description = "복사갯수")
    int copyCnt;
    @Schema(description = "중복갯수")
    int duplCnt;
}
