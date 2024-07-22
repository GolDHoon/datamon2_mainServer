package com.datamon.datamon2.dto.repository.common;

import com.datamon.datamon2.entity.common.DrivenCommonCheckUserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * DTO for {@link DrivenCommonCheckUserEntity}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DrivenCommonCheckUserDto implements Serializable {
    Boolean useYn;
    Boolean delYn;
    LocalDateTime createDate;
    Integer createId;
    LocalDateTime modifyDate;
    Integer modifyId;
    LocalDateTime deleteDate;
    Integer deleteId;

    public void useChoise(boolean use){
        useYn = use;
    }

    public void delChoise(boolean del){
        delYn = del;
    }

    public void create(int userId){
        createId = userId;
        modifyId = userId;
        createDate = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        modifyDate = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        useYn = true;
        delYn = false;
    }

    public void modify(int userId){
        modifyId = userId;
        modifyDate = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
    }

    public void delete(int userId){
        deleteId = userId;
        delYn = true;
        deleteDate = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
    }
}