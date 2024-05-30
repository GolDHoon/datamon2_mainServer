package com.datamon.datamon2.dto.repository;

import com.datamon.datamon2.entity.LandingPageBlockedKeywordEntity;
import lombok.Data;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link LandingPageBlockedKeywordEntity}
 */
@Data
public class LandingPageBlockedKeywordDto implements Serializable {
    Integer idx;
    String lpgeCode;
    String keyword;
    Boolean useYn;
    Boolean delYn;
    Instant createDate;
    Integer createId;
    Instant modifyDate;
    Integer modifyId;
    Instant deleteDate;
    Integer deleteId;
}