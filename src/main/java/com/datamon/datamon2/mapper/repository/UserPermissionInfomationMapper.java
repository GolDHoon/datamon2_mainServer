package com.datamon.datamon2.mapper.repository;

import com.datamon.datamon2.dto.repository.UserPermissionInfomationDto;
import com.datamon.datamon2.entity.UserPermissionInfomationEntity;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface UserPermissionInfomationMapper {
    UserPermissionInfomationEntity toEntity(UserPermissionInfomationDto userPermissionInfomationDto);

    UserPermissionInfomationDto toDto(UserPermissionInfomationEntity userPermissionInfomationEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    UserPermissionInfomationEntity partialUpdate(UserPermissionInfomationDto userPermissionInfomationDto, @MappingTarget UserPermissionInfomationEntity userPermissionInfomationEntity);
}