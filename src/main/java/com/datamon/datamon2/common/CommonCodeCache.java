package com.datamon.datamon2.common;

import com.datamon.datamon2.dto.repository.LpgeCodeDto;
import com.datamon.datamon2.dto.repository.PaatCodeDto;
import com.datamon.datamon2.dto.repository.PageCodeDto;
import com.datamon.datamon2.dto.repository.UstyCodeDto;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class CommonCodeCache {
    private static int systemIdIdx;
    private static List<PageCodeDto> pageCodes;
    private static List<PaatCodeDto> paatCodes;
    private static List<LpgeCodeDto> lpgeCodes;
    private static List<UstyCodeDto> ustyCodes;

    public static List<UstyCodeDto> getUstyCodes() {
        return ustyCodes;
    }

    public static void setUstyCodes(List<UstyCodeDto> ustyCodes) {
        CommonCodeCache.ustyCodes = ustyCodes;
    }

    public static void setSystemIdIdx(int systemIdIdx) {
        CommonCodeCache.systemIdIdx = systemIdIdx;
    }

    public static int getSystemIdIdx() {return systemIdIdx;}

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
