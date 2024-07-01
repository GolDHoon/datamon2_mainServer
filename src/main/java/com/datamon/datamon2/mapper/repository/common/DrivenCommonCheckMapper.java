package com.datamon.datamon2.mapper.repository.common;

import com.datamon.datamon2.dto.repository.common.DrivenCommonCheckDto;
import com.datamon.datamon2.entity.common.DrivenCommonCheckEntity;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface DrivenCommonCheckMapper {
    DrivenCommonCheckEntity toEntity(DrivenCommonCheckDto drivenCommonCheckDto);

    DrivenCommonCheckDto toDto(DrivenCommonCheckEntity drivenCommonCheckEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    DrivenCommonCheckEntity partialUpdate(DrivenCommonCheckDto drivenCommonCheckDto, @MappingTarget DrivenCommonCheckEntity drivenCommonCheckEntity);
}