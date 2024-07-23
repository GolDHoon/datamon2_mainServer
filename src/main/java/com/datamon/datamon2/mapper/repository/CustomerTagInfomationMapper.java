package com.datamon.datamon2.mapper.repository;

import com.datamon.datamon2.dto.repository.CustomerTagInfomationDto;
import com.datamon.datamon2.entity.CustomerTagInfomationEntity;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface CustomerTagInfomationMapper {
    CustomerTagInfomationEntity toEntity(CustomerTagInfomationDto customerTagInfomationDto);

    CustomerTagInfomationDto toDto(CustomerTagInfomationEntity customerTagInfomationEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    CustomerTagInfomationEntity partialUpdate(CustomerTagInfomationDto customerTagInfomationDto, @MappingTarget CustomerTagInfomationEntity customerTagInfomationEntity);
}