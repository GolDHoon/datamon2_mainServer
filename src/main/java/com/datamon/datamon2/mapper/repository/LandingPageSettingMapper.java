package com.datamon.datamon2.mapper.repository;

import com.datamon.datamon2.dto.repository.LandingPageSettingDto;
import com.datamon.datamon2.entity.LandingPageSettingEntity;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface LandingPageSettingMapper {
    LandingPageSettingEntity toEntity(LandingPageSettingDto landingPageSettingDto);

    LandingPageSettingDto toDto(LandingPageSettingEntity landingPageSettingEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    LandingPageSettingEntity partialUpdate(LandingPageSettingDto landingPageSettingDto, @MappingTarget LandingPageSettingEntity landingPageSettingEntity);
}