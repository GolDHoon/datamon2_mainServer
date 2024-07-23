package com.datamon.datamon2.dto.repository;

import com.datamon.datamon2.entity.TableIndexEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * DTO for {@link TableIndexEntity}
 */
@Data
public class TableIndexDto implements Serializable {
    Long idx;
    String tableName;
    String optionName;
    Long index;
}