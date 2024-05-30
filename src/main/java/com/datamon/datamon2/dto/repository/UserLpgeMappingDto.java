package com.datamon.datamon2.dto.repository;

import com.datamon.datamon2.entity.UserLpgeMappingEntity;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link UserLpgeMappingEntity}
 */
@Data
public class UserLpgeMappingDto implements Serializable {
    String lpgeCode;
    Integer userId;
}