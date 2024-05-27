package com.datamon.datamon2.dto.repository;

import com.datamon.datamon2.entity.UserBaseEntity;
import lombok.Data;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link UserBaseEntity}
 */
@Data
public class UserBaseDto implements Serializable {
    Integer idx;
    String userId;
    String userPw;
    String salt;
    String userType;
    Boolean useYn;
    Boolean delYn;
    Instant createDate;
    Integer createId;
    Instant modifyDate;
    Integer modifyId;
    Instant deleteDate;
    Integer deleteId;
}