package com.datamon.datamon2.mapper.repository;

import com.datamon.datamon2.dto.repository.LpgeCodeDto;
import com.datamon.datamon2.entity.LpgeCodeEntity;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface LpgeCodeMapper {
    LpgeCodeEntity toEntity(LpgeCodeDto lpgeCodeDto);

    LpgeCodeDto toDto(LpgeCodeEntity lpgeCodeEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    LpgeCodeEntity partialUpdate(LpgeCodeDto lpgeCodeDto, @MappingTarget LpgeCodeEntity lpgeCodeEntity);
}