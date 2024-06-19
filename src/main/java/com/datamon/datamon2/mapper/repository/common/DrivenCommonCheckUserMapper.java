package com.datamon.datamon2.mapper.repository.common;

import com.datamon.datamon2.dto.repository.common.DrivenCommonCheckUserDto;
import com.datamon.datamon2.entity.common.DrivenCommonCheckUserEntity;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface DrivenCommonCheckUserMapper {
    DrivenCommonCheckUserEntity toEntity(DrivenCommonCheckUserDto drivenCommonCheckUserDto);

    DrivenCommonCheckUserDto toDto(DrivenCommonCheckUserEntity drivenCommonCheckUserEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    DrivenCommonCheckUserEntity partialUpdate(DrivenCommonCheckUserDto drivenCommonCheckUserDto, @MappingTarget DrivenCommonCheckUserEntity drivenCommonCheckUserEntity);
}