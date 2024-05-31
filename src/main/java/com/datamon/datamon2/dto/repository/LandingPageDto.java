package com.datamon.datamon2.dto.repository;

import com.datamon.datamon2.entity.LandingPageEntity;
import lombok.Data;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;

/**
 * DTO for {@link LandingPageEntity}
 */
@Data
public class LandingPageDto implements Serializable {
    Integer idx;
    String lpgeCode;
    String domain;
    Boolean useYn;
    Boolean delYn;
    Instant createDate;
    Integer createId;
    Instant modifyDate;
    Integer modifyId;
    Instant deleteDate;
    Integer deleteId;
}