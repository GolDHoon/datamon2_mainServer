package com.datamon.datamon2.mapper.repository;

import com.datamon.datamon2.dto.repository.CompanyInfomationDto;
import com.datamon.datamon2.entity.CompanyInfomationEntity;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface CompanyInfomationMapper {
    CompanyInfomationEntity toEntity(CompanyInfomationDto companyInfomationDto);

    CompanyInfomationDto toDto(CompanyInfomationEntity companyInfomationEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    CompanyInfomationEntity partialUpdate(CompanyInfomationDto companyInfomationDto, @MappingTarget CompanyInfomationEntity companyInfomationEntity);
}