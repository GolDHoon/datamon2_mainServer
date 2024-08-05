package com.datamon.datamon2.dto.repository;

import com.datamon.datamon2.entity.LandingPageSettingEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * DTO for {@link LandingPageSettingEntity}
 */
@Data
public class LandingPageSettingDto implements Serializable {
    Long idx;
    String lpgeCode;
    String columnName;
    Boolean duplicationValidationYn;
    Integer duplicationValidationDays;
    Boolean displayOrderingYn;
    Long displayOrderingNumber;
}