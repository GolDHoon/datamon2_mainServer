package com.datamon.datamon2.mapper.repository;

import com.datamon.datamon2.dto.repository.LandingPageBlockedKeywordDto;
import com.datamon.datamon2.entity.LandingPageBlockedKeywordEntity;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface LandingPageBlockedKeywordMapper {
    LandingPageBlockedKeywordEntity toEntity(LandingPageBlockedKeywordDto landingPageBlockedKeywordDto);

    LandingPageBlockedKeywordDto toDto(LandingPageBlockedKeywordEntity landingPageBlockedKeywordEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    LandingPageBlockedKeywordEntity partialUpdate(LandingPageBlockedKeywordDto landingPageBlockedKeywordDto, @MappingTarget LandingPageBlockedKeywordEntity landingPageBlockedKeywordEntity);
}