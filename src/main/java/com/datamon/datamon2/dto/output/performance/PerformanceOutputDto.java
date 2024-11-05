package com.datamon.datamon2.dto.output.performance;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class PerformanceOutputDto {
    @Schema(description = "날짜")
    String name;
    @Schema(description = "소스 건수")
    int source;
    @Schema(description = "매체 건수")
    int medium;
}
