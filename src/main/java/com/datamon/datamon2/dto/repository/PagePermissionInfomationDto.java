package com.datamon.datamon2.dto.repository;

import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link com.datamon.datamon2.entity.PagePermissionInfomationEntity}
 */
@Data
public class PagePermissionInfomationDto implements Serializable {
    Long idx;
    Integer userId;
    String pageCode;
    String paatCode;
    Boolean useYn;
    Boolean delYn;
    Instant createDate;
    Integer create;
    Instant modifyDate;
    Integer modify;
    Instant deleteDate;
    Integer deleteId;
}