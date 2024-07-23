package com.datamon.datamon2.mapper.repository;

import com.datamon.datamon2.dto.repository.LandingPageInfomationDto;
import com.datamon.datamon2.entity.LandingPageInfomationEntity;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface LandingPageInfomationMapper {
    LandingPageInfomationEntity toEntity(LandingPageInfomationDto landingPageInfomationDto);

    LandingPageInfomationDto toDto(LandingPageInfomationEntity landingPageInfomationEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    LandingPageInfomationEntity partialUpdate(LandingPageInfomationDto landingPageInfomationDto, @MappingTarget LandingPageInfomationEntity landingPageInfomationEntity);
}