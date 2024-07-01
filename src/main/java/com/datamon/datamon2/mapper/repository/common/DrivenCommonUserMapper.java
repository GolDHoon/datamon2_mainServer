package com.datamon.datamon2.mapper.repository.common;

import com.datamon.datamon2.dto.repository.common.DrivenCommonUserDto;
import com.datamon.datamon2.entity.common.DrivenCommonUserEntity;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface DrivenCommonUserMapper {
    DrivenCommonUserEntity toEntity(DrivenCommonUserDto drivenCommonUserDto);

    DrivenCommonUserDto toDto(DrivenCommonUserEntity drivenCommonUserEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    DrivenCommonUserEntity partialUpdate(DrivenCommonUserDto drivenCommonUserDto, @MappingTarget DrivenCommonUserEntity drivenCommonUserEntity);
}