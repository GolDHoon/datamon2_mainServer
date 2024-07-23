package com.datamon.datamon2.dto.repository;

import com.datamon.datamon2.dto.repository.common.DrivenCommonCheckUserDto;
import com.datamon.datamon2.entity.CdbsCodeEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * DTO for {@link CdbsCodeEntity}
 */
@Data
public class CdbsCodeDto extends DrivenCommonCheckUserDto implements Serializable {
    Integer idx;
    String codeName;
    String codeFullName;
    String codeValue;
    String codeDescript;

    @Override
    public void create(int userId) {
        super.create(userId);
        codeFullName = "CDBS_" + codeName;
    }
}