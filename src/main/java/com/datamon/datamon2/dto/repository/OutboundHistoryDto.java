package com.datamon.datamon2.dto.repository;

import com.datamon.datamon2.dto.repository.common.DrivenCommonCheckUserDto;
import com.datamon.datamon2.entity.OutboundHistoryEntity;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link OutboundHistoryEntity}
 */
@Value
public class OutboundHistoryDto extends DrivenCommonCheckUserDto implements Serializable {
    String idx;
    Integer userId;
    String custId;
    String telColumn;
    String memo;
}