package com.datamon.datamon2.dto.repository;

import com.datamon.datamon2.entity.OutboundEntity;
import jakarta.persistence.Column;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link OutboundEntity}
 */
@Data
public class OutboundDto implements Serializable {
    String idx;
    Integer userId;
    String custId;
    String telColumn;
    String memo;
    String orderMemo;
    LocalDateTime scheduledCallbackDate;
    LocalDateTime scheduledConversionDate;

    public void createId () {
        idx = String.format("%010d", userId) + "_" + custId;
    }
}