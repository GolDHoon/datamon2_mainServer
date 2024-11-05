package com.datamon.datamon2.dto.repository;

import com.datamon.datamon2.dto.repository.common.DrivenCommonUserDto;
import com.datamon.datamon2.entity.EmailVerificationEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link EmailVerificationEntity}
 */
@Data
public class EmailVerificationDto extends DrivenCommonUserDto implements Serializable {
    String idx;
    String verificationCode;

    @Override
    public void create(int userId) {
        super.create(userId);
        this.idx = UUID.randomUUID().toString().replace("-", "");
    }
}