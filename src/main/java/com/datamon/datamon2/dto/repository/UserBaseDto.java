package com.datamon.datamon2.dto.repository;

import com.datamon.datamon2.dto.repository.common.DrivenCommonCheckUserDto;
import com.datamon.datamon2.entity.UserBaseEntity;
import lombok.Data;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link UserBaseEntity}
 */
@Data
public class UserBaseDto extends DrivenCommonCheckUserDto implements Serializable {
    Integer idx;
    String userId;
    String userPw;
    String salt;
    String userType;
}