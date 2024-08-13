package com.datamon.datamon2.dto.repository;

import com.datamon.datamon2.dto.repository.common.DrivenCommonCheckUserDto;
import com.datamon.datamon2.entity.OutboundHistoryEntity;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link OutboundHistoryEntity}
 */
@Data
public class OutboundHistoryDto extends DrivenCommonCheckUserDto implements Serializable {
    String idx;
    String originalIdx;
    int sort;
    String saveReason;
    Integer userId;
    String custId;
    String telColumn;
    String memo;
    String orderMemo;
    LocalDateTime scheduledCallbackDate;
    LocalDateTime scheduledConversionDate;

    public void createId () {
        idx = custId + "_" + String.format("%010d", sort);
    }
}