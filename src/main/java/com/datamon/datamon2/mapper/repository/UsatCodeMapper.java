package com.datamon.datamon2.mapper.repository;

import com.datamon.datamon2.dto.repository.UsatCodeDto;
import com.datamon.datamon2.entity.UsatCodeEntity;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface UsatCodeMapper {
    UsatCodeEntity toEntity(UsatCodeDto usatCodeDto);

    UsatCodeDto toDto(UsatCodeEntity usatCodeEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    UsatCodeEntity partialUpdate(UsatCodeDto usatCodeDto, @MappingTarget UsatCodeEntity usatCodeEntity);
}