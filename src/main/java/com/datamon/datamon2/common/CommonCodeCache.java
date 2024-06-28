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
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class CommonCodeCache {
    private static int systemIdIdx;
    private static List<PageCodeDto> pageCodes;
    private static List<PaatCodeDto> paatCodes;
    private static List<LpgeCodeDto> lpgeCodes;
    private static List<UstyCodeDto> ustyCodes;
    private static List<UstyCodeDto> masterCodes;
    private static List<UstyCodeDto> memberCodes;
    private static List<UstyCodeDto> companyCode;

    public static List<UstyCodeDto> getMasterCodes() {
        return masterCodes;
    }

    public static List<UstyCodeDto> getMemberCodes() {
        return memberCodes;
    }

    public static List<UstyCodeDto> getCompanyCode() {
        return companyCode;
    }

    public static List<UstyCodeDto> getUstyCodes() {
        return ustyCodes;
    }

    public static void setUstyCodes(List<UstyCodeDto> ustyCodes) {
        List<String> masterCode = new ArrayList<>();
        masterCode.add("USTY_MAST");
        masterCode.add("USTY_DEVL");
        masterCode.add("USTY_INME");

        CommonCodeCache.ustyCodes = ustyCodes;

        masterCodes = ustyCodes.stream()
                .filter(dto -> masterCode.contains(dto.getCodeValue()))
                .filter(UstyCodeDto::getUseYn)
                .filter(dto -> !dto.getDelYn())
                .collect(Collectors.toList());

        memberCodes = ustyCodes.stream()
                .filter(dto -> dto.getCodeValue().contains("Member"))
                .filter(UstyCodeDto::getUseYn)
                .filter(dto -> !dto.getDelYn())
                .collect(Collectors.toList());

        companyCode = ustyCodes.stream()
                .filter(dto -> dto.getCodeValue().contains("Company"))
                .filter(UstyCodeDto::getUseYn)
                .filter(dto -> !dto.getDelYn())
                .collect(Collectors.toList());
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
