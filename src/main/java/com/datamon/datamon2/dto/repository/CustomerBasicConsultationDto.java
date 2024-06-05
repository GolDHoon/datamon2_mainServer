package com.datamon.datamon2.dto.repository;

import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.datamon.datamon2.entity.CustomerBasicConsultationEntity}
 */
@Data
public class CustomerBasicConsultationDto implements Serializable {
    Long idx;
    Long custId;
    String key;
    String value;
    LocalDateTime createDate;
    Integer createId;
    LocalDateTime modiftyDate;
    Integer modifyId;
    LocalDateTime deleteDate;
    Integer deleteId;
}