package com.datamon.datamon2.mapper.repository;

import com.datamon.datamon2.dto.repository.PagePermissionInfomationDto;
import com.datamon.datamon2.entity.PagePermissionInfomationEntity;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface PagePermissionInfomationMapper {
    PagePermissionInfomationEntity toEntity(PagePermissionInfomationDto pagePermissionInfomationDto);

    PagePermissionInfomationDto toDto(PagePermissionInfomationEntity pagePermissionInfomationEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    PagePermissionInfomationEntity partialUpdate(PagePermissionInfomationDto pagePermissionInfomationDto, @MappingTarget PagePermissionInfomationEntity pagePermissionInfomationEntity);
}