package com.datamon.datamon2.dto.repository;

import com.datamon.datamon2.dto.repository.common.DrivenCommonCheckUserDto;
import com.datamon.datamon2.entity.UserPermissionInfomationEntity;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link UserPermissionInfomationEntity}
 */
@Data
public class UserPermissionInfomationDto extends DrivenCommonCheckUserDto implements Serializable {
    Long idx;
    Integer userId;
    String usatCode;
    String cdbtLowCode;
    String cdbtCode;
}