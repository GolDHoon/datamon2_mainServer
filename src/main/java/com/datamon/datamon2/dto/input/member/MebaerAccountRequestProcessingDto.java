package com.datamon.datamon2.dto.input.member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class MebaerAccountRequestProcessingDto {
    @Schema(description = "승인 데이터 ID")
    String idx;
    @Schema(description = "반려 사유 ID")
    String rejectionReason;
}
