package com.datamon.datamon2.common;

import com.datamon.datamon2.dto.repository.LpgeCodeDto;
import com.datamon.datamon2.dto.repository.PaatCodeDto;
import com.datamon.datamon2.dto.repository.PageCodeDto;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

public class CommonCodeCache {
    @Value("{system.Id.idx}")
    static String systemIdIdxStr;
    private static int systemIdIdx;
    private static List<PageCodeDto> pageCodes;
    private static List<PaatCodeDto> paatCodes;
    private static List<LpgeCodeDto> lpgeCodes;

    static {
        try {
            systemIdIdx = Integer.parseInt(systemIdIdxStr);   // value를 가져와서 형변환
        } catch(NumberFormatException e) {
            systemIdIdx = 0;     // 형변환에 실패한 경우 기본값 설정
        }
    }

    public static int getSystemIdIdx() {
        return systemIdIdx;
    }

    public static List<LpgeCodeDto> getLpgeCodes() {
        return lpgeCodes;
    }

    public static void setLpgeCodes(List<LpgeCodeDto> lpgeCodes) {
        CommonCodeCache.lpgeCodes = lpgeCodes;
    }

    public static List<PageCodeDto> getPageCodes() {
        return pageCodes;
    }

    public static void setPageCodes(List<PageCodeDto> pageCodes) {
        CommonCodeCache.pageCodes = pageCodes;
    }

    public static List<PaatCodeDto> getPaatCodes() {
        return paatCodes;
    }

    public static void setPaatCodes(List<PaatCodeDto> paatCodes) {
        CommonCodeCache.paatCodes = paatCodes;
    }
}
