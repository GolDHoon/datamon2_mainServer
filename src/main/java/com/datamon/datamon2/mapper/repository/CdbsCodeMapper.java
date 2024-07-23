package com.datamon.datamon2.mapper.repository;

import com.datamon.datamon2.dto.repository.CdbsCodeDto;
import com.datamon.datamon2.entity.CdbsCodeEntity;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface CdbsCodeMapper {
    CdbsCodeEntity toEntity(CdbsCodeDto cdbsCodeDto);

    CdbsCodeDto toDto(CdbsCodeEntity cdbsCodeEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    CdbsCodeEntity partialUpdate(CdbsCodeDto cdbsCodeDto, @MappingTarget CdbsCodeEntity cdbsCodeEntity);
}