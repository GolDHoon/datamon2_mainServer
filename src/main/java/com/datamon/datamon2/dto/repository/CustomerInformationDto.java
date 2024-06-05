package com.datamon.datamon2.dto.repository;

import com.datamon.datamon2.entity.CustomerInformationEntity;
import lombok.Data;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;

/**
 * DTO for {@link CustomerInformationEntity}
 */
@Data
public class CustomerInformationDto implements Serializable {
    Long  idx;
    String lpgeCode;
    String utmSourse;
    String utmMedium;
    String utmCampaign;
    String utmTerm;
    String utmContent;
    String ip;
    Boolean useYn;
    Boolean delYn;
    LocalDateTime createDate;
    Integer createId;
    LocalDateTime modifyDate;
    Integer modifyId;
    LocalDateTime deleteDate;
    Integer deleteId;
}