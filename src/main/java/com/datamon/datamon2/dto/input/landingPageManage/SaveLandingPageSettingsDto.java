package com.datamon.datamon2.dto.input.landingPageManage;

import lombok.Data;

import java.util.List;

@Data
public class SaveLandingPageSettingsDto {
    String lpgeCode;
    String subTitle;
    List<?> saveSettings;
}
