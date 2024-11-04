package com.datamon.datamon2.dto.repository;

import com.datamon.datamon2.dto.repository.common.DrivenCommonUserDto;
import com.datamon.datamon2.entity.DbDuplicateDataProcessingEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

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

    @Override
    public void create(int userId) {
        super.create(userId);
        this.idx = UUID.randomUUID().toString().replace("-", "");
    }
}