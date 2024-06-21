package com.datamon.datamon2.dto.repository;

import com.datamon.datamon2.entity.CompanyInfomationEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * DTO for {@link CompanyInfomationEntity}
 */
@Data
public class CompanyInfomationDto implements Serializable {
    Integer idx;
    Integer userId;
    String name;
    String corporateNumber;
    String ceo;
}