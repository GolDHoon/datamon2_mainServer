package com.datamon.datamon2.dto.input.landingPage;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class CustDataDto {
    String lpgeCode;
    String utmSource;
    String utmMedium;
    String utmCampaign;
    String utmTerm;
    String utmContent;
    List<Map<String, String>> data;
    String inputMode;
}
