package com.datamon.datamon2.mapper.repository;

import com.datamon.datamon2.dto.repository.CdbqCodeDto;
import com.datamon.datamon2.entity.CdbqCodeEntity;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface CdbqCodeMapper {
    CdbqCodeEntity toEntity(CdbqCodeDto cdbqCodeDto);

    CdbqCodeDto toDto(CdbqCodeEntity cdbqCodeEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    CdbqCodeEntity partialUpdate(CdbqCodeDto cdbqCodeDto, @MappingTarget CdbqCodeEntity cdbqCodeEntity);
}