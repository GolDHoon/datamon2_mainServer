package com.datamon.datamon2.common;

import com.datamon.datamon2.dto.repository.*;
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
    private static List<UsatCodeDto> usatCodes;
    private static List<CdbqCodeDto> cdbqCodes;
    private static List<CdbsCodeDto> cdbsCodes;
    private static List<UstyCodeDto> masterCodes;
    private static List<UstyCodeDto> memberCodes;
    private static List<UstyCodeDto> companyCode;
    private static List<UsatCodeDto> commonPermissionCodes;
    private static List<CdbsCodeDto> distributionAllowedCodes;

    public static List<CdbsCodeDto> getDistributionAllowedCodes() {
        return distributionAllowedCodes;
    }

    public static List<CdbsCodeDto> getCdbsCodes() {
        return cdbsCodes;
    }

    public static void setCdbsCodes(List<CdbsCodeDto> cdbsCodes) {
        CommonCodeCache.cdbsCodes = cdbsCodes;

        List<String> distributionAllowedList = new ArrayList<>();
        distributionAllowedList.add("대기중");
        distributionAllowedList.add("상담종료");
        distributionAllowedList.add("최종완료");
        distributionAllowedList.add("관리대상");

        distributionAllowedCodes = cdbsCodes.stream()
                .filter(dto -> distributionAllowedList.contains(dto.getCodeValue()))
                .collect(Collectors.toList());

    }

    public static List<CdbqCodeDto> getCdbqCodes() {
        return cdbqCodes;
    }

    public static void setCdbqCodes(List<CdbqCodeDto> cdbqCodes) {
        CommonCodeCache.cdbqCodes = cdbqCodes;
    }

    public static List<UsatCodeDto> getUsatCodes() {
        return usatCodes;
    }

    public static void setUsatCodes(List<UsatCodeDto> usatCodes) {
        CommonCodeCache.usatCodes = usatCodes;

        commonPermissionCodes = usatCodes.stream()
                .filter(UsatCodeDto::getUseYn)
                .filter(dto -> !dto.getDelYn())
                .filter(dto -> dto.getCodeValue().equals("관리자") || dto.getCodeValue().equals("편집자") || dto.getCodeValue().equals("뷰어"))
                .collect(Collectors.toList());
    }

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
                .filter(dto -> masterCode.contains(dto.getCodeFullName()))
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

    public static List<UsatCodeDto> getCommonPermissionCodes() {
        return commonPermissionCodes;
    }

}
