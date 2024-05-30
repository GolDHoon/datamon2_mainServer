package com.datamon.datamon2.dto.repository;

import com.datamon.datamon2.entity.CustomerInformationEntity;
import lombok.Data;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link CustomerInformationEntity}
 */
@Data
public class CustomerInformationDto implements Serializable {
    Long id;
    Integer lpgeCode;
    String utmSourse;
    String utmMedium;
    String utmCampaign;
    String utmTerm;
    String utmContent;
    String ip;
    Boolean useYn;
    Boolean delYn;
    Instant createDate;
    Integer create;
    Instant modifyDate;
    Integer modify;
    Instant deleteDate;
    Integer deleteId;
}