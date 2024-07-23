package com.datamon.datamon2.dto.repository;

import com.datamon.datamon2.entity.OutboundEntity;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link OutboundEntity}
 */
@Value
public class OutboundDto implements Serializable {
    String idx;
    Integer userId;
    String custId;
    String telColumn;
    String memo;
}