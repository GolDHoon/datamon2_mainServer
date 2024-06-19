package com.datamon.datamon2.dto.repository;

import com.datamon.datamon2.dto.repository.common.DrivenCommonCheckUserDto;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link com.datamon.datamon2.entity.PagePermissionInfomationEntity}
 */
@Data
public class PagePermissionInfomationDto extends DrivenCommonCheckUserDto implements Serializable {
    Long idx;
    Integer userId;
    String pageCode;
    String paatCode;
}