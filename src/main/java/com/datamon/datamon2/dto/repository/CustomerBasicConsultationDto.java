package com.datamon.datamon2.dto.repository;

import com.datamon.datamon2.dto.repository.common.DrivenCommonUserDto;
import com.datamon.datamon2.entity.CustomerBasicConsultationEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * DTO for {@link CustomerBasicConsultationEntity}
 */
@Data
public class CustomerBasicConsultationDto extends DrivenCommonUserDto implements Serializable {
    Long idx;
    String custId;
    String key;
    String value;
}