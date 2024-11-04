package com.datamon.datamon2.dto.input.custDb;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DuplColumnDto {
    @Schema(description = "key 그룹")
    int keyGroup;
    @Schema(description = "DB 코드")
    String dbCode;
    @Schema(description = "중복체크 키 목록")
    List<String> keyList;
    @Schema(description = "전처리 여부")
    boolean preprocessingYn;
    @Schema(description = "후처리 여부")
    boolean postprocessingYn;
}
