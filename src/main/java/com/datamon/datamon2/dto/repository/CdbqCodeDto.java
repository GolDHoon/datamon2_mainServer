package com.datamon.datamon2.dto.repository;

import com.datamon.datamon2.dto.repository.common.DrivenCommonCheckUserDto;
import com.datamon.datamon2.entity.CdbqCodeEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * DTO for {@link CdbqCodeEntity}
 */
@Data
public class CdbqCodeDto extends DrivenCommonCheckUserDto implements Serializable {
    Integer idx;
    String codeName;
    String codeFullName;
    String codeValue;
    String codeDescript;

    @Override
    public void create(int userId) {
        super.create(userId);
        codeFullName = "CDBQ_" + codeName;
    }
}