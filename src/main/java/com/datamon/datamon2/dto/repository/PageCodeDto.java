package com.datamon.datamon2.dto.repository;

import com.datamon.datamon2.dto.repository.common.DrivenCommonCheckUserDto;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link com.datamon.datamon2.entity.PageCodeEntity}
 */
@Data
public class PageCodeDto extends DrivenCommonCheckUserDto implements Serializable {
    Integer idx;
    Integer codeName;
    String codeFullName;
    String codeValue;
    String codeDescript;

    @Override
    public void create(int userId) {
        super.create(userId);
        codeFullName = "PAGE_" + String.format("%010d", codeName);
    }
}