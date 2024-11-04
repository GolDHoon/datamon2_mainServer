package com.datamon.datamon2.dto.output.custDb;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class GetLpgeCodeInfoOutputDto {
    @Schema(description = "코드명")
    String code;
    @Schema(description = "url")
    String url;
    @Schema(description = "중복제거칼럼 정보")
    Map<String, Object> duplColumnInfo;
    @Schema(description = "차단 IP 목록")
    List<String> blockIpList;
    @Schema(description = "차단 키워드 목록")
    List<String> blockKeywordList;
    @Schema(description = "랜딩페이지 정보 목록")
    Map<String, Object> landingInfo;
    @Schema(description = "랜딩페이지 매핑 정보 목록")
    List<Map<String, Object>> landingMappingInfoList;
}
