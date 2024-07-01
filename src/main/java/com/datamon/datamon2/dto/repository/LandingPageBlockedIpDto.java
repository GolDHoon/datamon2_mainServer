package com.datamon.datamon2.dto.repository;

import com.datamon.datamon2.dto.repository.common.DrivenCommonCheckUserDto;
import com.datamon.datamon2.entity.LandingPageBlockedIpEntity;
import lombok.Data;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link LandingPageBlockedIpEntity}
 */
@Data
public class LandingPageBlockedIpDto extends DrivenCommonCheckUserDto implements Serializable {
    Integer idx;
    String lpgeCode;
    Short ip1;
    Short ip2;
    Short ip3;
    Short ip4;
}