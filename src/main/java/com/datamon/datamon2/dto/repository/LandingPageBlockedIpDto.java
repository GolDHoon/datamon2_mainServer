package com.datamon.datamon2.dto.repository;

import com.datamon.datamon2.entity.LandingPageBlockedIpEntity;
import lombok.Data;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link LandingPageBlockedIpEntity}
 */
@Data
public class LandingPageBlockedIpDto implements Serializable {
    Integer idx;
    String lpgeCode;
    Short ip1;
    Short ip2;
    Short ip3;
    Short ip4;
    Boolean useYn;
    Boolean delYn;
    Instant createDate;
    Integer createId;
    Instant modifyDate;
    Integer modifyId;
    Instant deleteDate;
    Integer deleteId;
}