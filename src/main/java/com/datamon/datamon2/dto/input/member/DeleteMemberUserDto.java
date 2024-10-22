package com.datamon.datamon2.dto.input.member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class DeleteMemberUserDto {
    @Schema(description = "유저 데이터 ID")
    int idx;
}
