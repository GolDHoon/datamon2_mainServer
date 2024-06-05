package com.datamon.datamon2.dto.repository;

import com.datamon.datamon2.entity.LpgeCodeEntity;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;

/**
 * DTO for {@link LpgeCodeEntity}
 */
@Data
public class LpgeCodeDto implements Serializable {
    Integer idx;
    Integer codeName;
    String codeFullName;
    String codeValue;
    String codeDescript;
    Boolean useYn;
    Boolean delYn;
    LocalDateTime createDate;
    Integer createId;
    LocalDateTime modifyDate;
    Integer modifyId;
    LocalDateTime deleteDate;
    Integer deleteId;
}