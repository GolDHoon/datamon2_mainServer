package com.datamon.datamon2.mapper.repository;

import com.datamon.datamon2.dto.repository.UstyCodeDto;
import com.datamon.datamon2.entity.UstyCodeEntity;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface UstyCodeMapper {
    UstyCodeEntity toEntity(UstyCodeDto ustyCodeDto);

    UstyCodeDto toDto(UstyCodeEntity ustyCodeEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    UstyCodeEntity partialUpdate(UstyCodeDto ustyCodeDto, @MappingTarget UstyCodeEntity ustyCodeEntity);
}