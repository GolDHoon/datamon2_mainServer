package com.datamon.datamon2.dto.repository;

import com.datamon.datamon2.dto.repository.common.DrivenCommonCheckUserDto;
import com.datamon.datamon2.entity.CustomerTagInfomationEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * DTO for {@link CustomerTagInfomationEntity}
 */
@Data
public class CustomerTagInfomationDto extends DrivenCommonCheckUserDto implements Serializable {
    String idx;
    Integer companyId;
    String cdbtLowCode;
    String tagTitle;
    String tagDiscription;
}