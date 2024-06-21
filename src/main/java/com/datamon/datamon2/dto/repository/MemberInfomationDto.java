package com.datamon.datamon2.dto.repository;

import com.datamon.datamon2.entity.MemberInfomationEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * DTO for {@link MemberInfomationEntity}
 */
@Data
public class MemberInfomationDto implements Serializable {
    Integer idx;
    Integer userId;
    Integer companyId;
    String name;
    String role;
    String contactPhone;
    String contactMail;
}