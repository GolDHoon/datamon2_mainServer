package com.datamon.datamon2.common;

import com.datamon.datamon2.dto.repository.PageCodeDto;

import java.util.List;

public class CommonCodeCache {
    private static List<PageCodeDto> pageCodes;

    public static List<PageCodeDto> getPageCodes() {
        return pageCodes;
    }

    public static void setPageCodes(List<PageCodeDto> pageCodes) {
        CommonCodeCache.pageCodes = pageCodes;
    }
}
