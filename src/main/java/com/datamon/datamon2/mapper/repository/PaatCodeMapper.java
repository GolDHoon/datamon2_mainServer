package com.datamon.datamon2.mapper.repository;

import com.datamon.datamon2.dto.repository.PaatCodeDto;
import com.datamon.datamon2.entity.PaatCodeEntity;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface PaatCodeMapper {
    PaatCodeEntity toEntity(PaatCodeDto paatCodeDto);

    PaatCodeDto toDto(PaatCodeEntity paatCodeEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    PaatCodeEntity partialUpdate(PaatCodeDto paatCodeDto, @MappingTarget PaatCodeEntity paatCodeEntity);
}