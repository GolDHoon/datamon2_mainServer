package com.datamon.datamon2.dto.repository;

import com.datamon.datamon2.dto.repository.common.DrivenCommonCheckUserDto;
import com.datamon.datamon2.entity.UstyCodeEntity;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link UstyCodeEntity}
 */
@Data
public class UstyCodeDto extends DrivenCommonCheckUserDto implements Serializable {
    Integer idx;
    String codeName;
    String codeValue;
    String codeFullName;
    String codeDescript;

    @Override
    public void create(int userId) {
        super.create(userId);
        codeFullName = "USTY_" + codeName;
    }
}