package com.datamon.datamon2.dto.repository;

import com.datamon.datamon2.dto.repository.common.DrivenCommonCheckUserDto;
import com.datamon.datamon2.entity.UserPermissionInfomationEntity;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link UserPermissionInfomationEntity}
 */
@Value
public class UserPermissionInfomationDto extends DrivenCommonCheckUserDto implements Serializable {
    Long idx;
    Integer userId;
    Integer usatCode;
    String cdbtLowCode;
    Integer cdbtCode;
}