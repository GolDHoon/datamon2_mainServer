package com.datamon.datamon2.dto.repository;

import com.datamon.datamon2.dto.repository.common.DrivenCommonCheckUserDto;
import com.datamon.datamon2.entity.UsatCodeEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * DTO for {@link UsatCodeEntity}
 */
@Data
public class UsatCodeDto extends DrivenCommonCheckUserDto implements Serializable {
    Integer idx;
    Integer codeName;
    String codeFullName;
    String codeValue;
    String codeDescript;

    @Override
    public void create(int userId) {
        super.create(userId);
        codeFullName = "AUTH_USAT" + String.format("%010d", codeName);
    }
}