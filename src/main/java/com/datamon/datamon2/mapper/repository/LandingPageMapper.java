package com.datamon.datamon2.mapper.repository;

import com.datamon.datamon2.dto.repository.LandingPageDto;
import com.datamon.datamon2.entity.LandingPageEntity;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface LandingPageMapper {
    LandingPageEntity toEntity(LandingPageDto landingPageDto);

    LandingPageDto toDto(LandingPageEntity landingPageEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    LandingPageEntity partialUpdate(LandingPageDto landingPageDto, @MappingTarget LandingPageEntity landingPageEntity);
}