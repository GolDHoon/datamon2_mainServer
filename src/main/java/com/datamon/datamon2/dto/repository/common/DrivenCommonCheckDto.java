package com.datamon.datamon2.dto.repository.common;

import com.datamon.datamon2.entity.common.DrivenCommonCheckEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link DrivenCommonCheckEntity}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DrivenCommonCheckDto implements Serializable {
    Boolean useYn;
    Boolean delYn;

    public void useChoise(boolean use){
        useYn = use;
    }

    public void delChoise(boolean del){
        delYn = del;
    }

    public void initialize(){
        useYn = true;
        delYn = false;
    }
}