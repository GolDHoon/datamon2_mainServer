package com.datamon.datamon2.dto.repository;

import com.datamon.datamon2.entity.LandingPageSettingEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * DTO for {@link LandingPageSettingEntity}
 */
@Data
public class LandingPageSettingDto implements Serializable {
    Integer idx;
    String lpgeCode;
    String duplicationColumn;
    Integer duplicationValidationDays;
}