package com.datamon.datamon2.mapper.repository;

import com.datamon.datamon2.dto.repository.CdbtCodeDto;
import com.datamon.datamon2.entity.CdbtCodeEntity;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface CdbtCodeMapper {
    CdbtCodeEntity toEntity(CdbtCodeDto cdbtCodeDto);

    CdbtCodeDto toDto(CdbtCodeEntity cdbtCodeEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    CdbtCodeEntity partialUpdate(CdbtCodeDto cdbtCodeDto, @MappingTarget CdbtCodeEntity cdbtCodeEntity);
}