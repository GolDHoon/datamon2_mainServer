package com.datamon.datamon2.dto.repository;

import lombok.Data;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.datamon.datamon2.entity.UserBase}
 */
@Data
public class UserBaseDto implements Serializable {
    Integer idx;
    String userId;
    String userPw;
    String salt;
    String userType;
}