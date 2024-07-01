package com.datamon.datamon2.dto.repository;

import com.datamon.datamon2.dto.repository.common.DrivenCommonCheckUserDto;
import com.datamon.datamon2.entity.CdbtCodeEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * DTO for {@link CdbtCodeEntity}
 */
@Data
public class CdbtCodeDto extends DrivenCommonCheckUserDto implements Serializable {
    Integer idx;
    String codeName;
    String codeFullName;
    String codeValue;
    String codeDescript;

    @Override
    public void create(int userId) {
        super.create(userId);
        codeFullName = "CDBT_LPGE_" + String.format("%010d", codeName);
    }
}