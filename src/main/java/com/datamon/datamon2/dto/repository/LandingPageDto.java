package com.datamon.datamon2.dto.repository;

import com.datamon.datamon2.dto.repository.common.DrivenCommonCheckUserDto;
import com.datamon.datamon2.entity.LandingPageEntity;
import lombok.Data;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO for {@link LandingPageEntity}
 */
@Data
public class LandingPageDto  extends DrivenCommonCheckUserDto implements Serializable {
    Integer idx;
    String lpgeCode;
    String domain;
}