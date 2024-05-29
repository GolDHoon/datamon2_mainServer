package com.datamon.datamon2.dto.repository;

import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link com.datamon.datamon2.entity.PaatCodeEntity}
 */
@Data
public class PaatCodeDto implements Serializable {
    Integer idx;
    Integer codeName;
    String codeFullName;
    String codeValue;
    String codeDescript;
    Boolean useYn;
    Boolean delYn;
    Instant createDate;
    Instant modifyDate;
    Instant deleteDate;
    Integer deleteId;
}