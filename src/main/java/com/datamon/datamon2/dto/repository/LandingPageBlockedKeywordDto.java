package com.datamon.datamon2.dto.repository;

import com.datamon.datamon2.dto.repository.common.DrivenCommonCheckUserDto;
import com.datamon.datamon2.entity.LandingPageBlockedKeywordEntity;
import lombok.Data;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link LandingPageBlockedKeywordEntity}
 */
@Data
public class LandingPageBlockedKeywordDto extends DrivenCommonCheckUserDto implements Serializable {
    Integer idx;
    String lpgeCode;
    String keyword;
}