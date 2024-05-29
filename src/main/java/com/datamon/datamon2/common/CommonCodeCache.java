package com.datamon.datamon2.common;

import com.datamon.datamon2.dto.repository.PaatCodeDto;
import com.datamon.datamon2.dto.repository.PageCodeDto;
import lombok.Data;

import java.util.List;

public class CommonCodeCache {
    private static List<PageCodeDto> pageCodes;
    private static List<PaatCodeDto> paatCodes;

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
