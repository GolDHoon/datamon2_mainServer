package com.datamon.datamon2.dto.repository;

import com.datamon.datamon2.dto.repository.common.DrivenCommonUserDto;
import com.datamon.datamon2.entity.SmsVerificationEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link SmsVerificationEntity}
 */
@Data
public class SmsVerificationDto extends DrivenCommonUserDto implements Serializable {
    String idx;
    String verificationCode;

    @Override
    public void create(int userId) {
        super.create(userId);
        this.idx = UUID.randomUUID().toString().replace("-", "");
    }
}