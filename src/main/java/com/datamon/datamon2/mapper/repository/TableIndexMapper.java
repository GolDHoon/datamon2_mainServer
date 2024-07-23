package com.datamon.datamon2.mapper.repository;

import com.datamon.datamon2.dto.repository.TableIndexDto;
import com.datamon.datamon2.entity.TableIndexEntity;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface TableIndexMapper {
    TableIndexEntity toEntity(TableIndexDto tableIndexDto);

    TableIndexDto toDto(TableIndexEntity tableIndexEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    TableIndexEntity partialUpdate(TableIndexDto tableIndexDto, @MappingTarget TableIndexEntity tableIndexEntity);
}