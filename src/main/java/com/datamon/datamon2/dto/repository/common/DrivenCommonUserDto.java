package com.datamon.datamon2.dto.repository.common;

import com.datamon.datamon2.entity.common.DrivenCommonUserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * DTO for {@link DrivenCommonUserEntity}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DrivenCommonUserDto implements Serializable {
    LocalDateTime createDate;
    Integer createId;
    LocalDateTime modifyDate;
    Integer modifyId;
    LocalDateTime deleteDate;
    Integer deleteId;

    public void create(int userId){
        createId = userId;
        modifyId = userId;
        createDate = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        modifyDate = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
    }

    public void modify(int userId){
        modifyId = userId;
        modifyDate = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
    }

    public void delete(int userId){
        deleteId = userId;
        deleteDate = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
    }
}