package com.datamon.datamon2.dto.repository;

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
public class LandingPageDto implements Serializable {
    Integer idx;
    String lpgeCode;
    String domain;
    Boolean useYn;
    Boolean delYn;
    LocalDateTime createDate;
    Integer createId;
    LocalDateTime modifyDate;
    Integer modifyId;
    LocalDateTime deleteDate;
    Integer deleteId;
}