package com.datamon.datamon2.dto.repository;

import com.datamon.datamon2.entity.UserCdbtMappingEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * DTO for {@link UserCdbtMappingEntity}
 */
@Data
public class UserCdbtMappingDto implements Serializable {
    Long idx;
    Integer userId;
    String cdbtLowCode;
    String cdbtCode;
}