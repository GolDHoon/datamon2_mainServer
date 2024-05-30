package com.datamon.datamon2.mapper.repository;

import com.datamon.datamon2.dto.repository.LandingPageBlockedIpDto;
import com.datamon.datamon2.entity.LandingPageBlockedIpEntity;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface LandingPageBlockedIpMapper {
    LandingPageBlockedIpEntity toEntity(LandingPageBlockedIpDto landingPageBlockedIpDto);

    LandingPageBlockedIpDto toDto(LandingPageBlockedIpEntity landingPageBlockedIpEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    LandingPageBlockedIpEntity partialUpdate(LandingPageBlockedIpDto landingPageBlockedIpDto, @MappingTarget LandingPageBlockedIpEntity landingPageBlockedIpEntity);
}