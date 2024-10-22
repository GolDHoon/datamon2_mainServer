package com.datamon.datamon2.dto.repository;

import com.datamon.datamon2.dto.repository.common.DrivenCommonCheckUserDto;
import com.datamon.datamon2.dto.repository.common.DrivenCommonUserDto;
import com.datamon.datamon2.entity.DbDuplicateDataProcessingEntity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

/**
 * DTO for {@link DbDuplicateDataProcessingEntity}
 */
@Data
public class DbDuplicateDataProcessingDto extends DrivenCommonUserDto implements Serializable {
    String idx;
    String dbType;
    String dbCode;
    Integer keyGroupNo;
    String key;
    Boolean preprocessingYn;
    Boolean postprocessingYn;
}