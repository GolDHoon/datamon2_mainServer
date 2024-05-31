package com.datamon.datamon2.dto.repository.embeddable;

import com.datamon.datamon2.entity.embeddable.UserLpgeMappingEntityId;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link UserLpgeMappingEntityId}
 */
@Value
public class UserLpgeMappingEntityIdDto implements Serializable {
    String lpgeCode;
    Integer userId;
}