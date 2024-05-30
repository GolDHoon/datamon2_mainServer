package com.datamon.datamon2.dto.repository;

import com.datamon.datamon2.entity.LandingPageEntity;
import lombok.Data;

import java.io.Serializable;
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
    LocalDate createDate;
    Integer createId;
    LocalDate modifyDate;
    Integer modifyId;
    LocalDate deleteDate;
    Integer deleteId;
}