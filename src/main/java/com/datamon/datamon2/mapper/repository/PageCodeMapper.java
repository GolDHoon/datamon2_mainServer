package com.datamon.datamon2.mapper.repository;

import com.datamon.datamon2.dto.repository.PageCodeDto;
import com.datamon.datamon2.entity.PageCodeEntity;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface PageCodeMapper {
    PageCodeEntity toEntity(PageCodeDto pageCodeDto);

    PageCodeDto toDto(PageCodeEntity pageCodeEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    PageCodeEntity partialUpdate(PageCodeDto pageCodeDto, @MappingTarget PageCodeEntity pageCodeEntity);
}