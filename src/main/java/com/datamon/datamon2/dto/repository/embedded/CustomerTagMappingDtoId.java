package com.datamon.datamon2.dto.repository.embedded;

import com.datamon.datamon2.entity.embedded.CustomerTagMappingEntityId;
import lombok.Data;

import java.io.Serializable;

/**
 * DTO for {@link CustomerTagMappingEntityId}
 */
@Data
public class CustomerTagMappingDtoId implements Serializable {
    String custId;
    Integer tagId;
}