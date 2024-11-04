package com.datamon.datamon2.dto.input.custDb;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class BlockedIpInfoDto {
    @Schema(description = "DB 코드")
    String dbCode;
    @Schema(description = "ip")
    String ip;
}
