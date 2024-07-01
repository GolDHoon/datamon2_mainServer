package com.datamon.datamon2.dto.repository;

import com.datamon.datamon2.entity.CompanyInfomationEntity;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link CompanyInfomationEntity}
 */
@Data
public class CompanyInfomationDto implements Serializable {
    Integer idx;
    Integer userId;
    String name;
    String ceo;
    String corporateNumber;
    String corporateAddress;
    String corporateMail;
    String businessStatus;
    String businessItem;
}