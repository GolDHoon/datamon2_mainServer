package com.datamon.datamon2.dto.repository;

import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link com.datamon.datamon2.entity.CustomerBasicConsultationEntity}
 */
@Data
public class CustomerBasicConsultationDto implements Serializable {
    Integer idx;
    Long custId;
    String key;
    String value;
    Instant createDate;
    Integer createId;
    Instant modiftyDate;
    Integer modifyId;
    Instant deleteDate;
    Integer deleteId;
}