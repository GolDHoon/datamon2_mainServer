package com.datamon.datamon2.mapper.repository;

import com.datamon.datamon2.dto.repository.DbDuplicateDataProcessingDto;
import com.datamon.datamon2.entity.DbDuplicateDataProcessingEntity;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface DbDuplicateDataProcessingMapper {
    DbDuplicateDataProcessingEntity toEntity(DbDuplicateDataProcessingDto dbDuplicateDataProcessingDto);

    DbDuplicateDataProcessingDto toDto(DbDuplicateDataProcessingEntity dbDuplicateDataProcessingEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    DbDuplicateDataProcessingEntity partialUpdate(DbDuplicateDataProcessingDto dbDuplicateDataProcessingDto, @MappingTarget DbDuplicateDataProcessingEntity dbDuplicateDataProcessingEntity);
}