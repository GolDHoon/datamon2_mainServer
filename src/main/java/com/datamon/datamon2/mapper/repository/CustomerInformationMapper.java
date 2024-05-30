package com.datamon.datamon2.mapper.repository;

import com.datamon.datamon2.dto.repository.CustomerInformationDto;
import com.datamon.datamon2.entity.CustomerInformationEntity;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface CustomerInformationMapper {
    CustomerInformationEntity toEntity(CustomerInformationDto customerInformationDto);

    CustomerInformationDto toDto(CustomerInformationEntity customerInformationEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    CustomerInformationEntity partialUpdate(CustomerInformationDto customerInformationDto, @MappingTarget CustomerInformationEntity customerInformationEntity);
}