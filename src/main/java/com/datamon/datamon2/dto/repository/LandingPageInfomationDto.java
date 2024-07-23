package com.datamon.datamon2.dto.repository;

import com.datamon.datamon2.entity.LandingPageInfomationEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * DTO for {@link LandingPageInfomationEntity}
 */
@Data
public class LandingPageInfomationDto implements Serializable {
    Integer idx;
    String lpgeCode;
    String subTitle;
}