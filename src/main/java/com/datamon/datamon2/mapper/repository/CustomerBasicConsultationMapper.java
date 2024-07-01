package com.datamon.datamon2.mapper.repository;

import com.datamon.datamon2.dto.repository.CustomerBasicConsultationDto;
import com.datamon.datamon2.entity.CustomerBasicConsultationEntity;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface CustomerBasicConsultationMapper {
    CustomerBasicConsultationEntity toEntity(CustomerBasicConsultationDto customerBasicConsultationDto);

    CustomerBasicConsultationDto toDto(CustomerBasicConsultationEntity customerBasicConsultationEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    CustomerBasicConsultationEntity partialUpdate(CustomerBasicConsultationDto customerBasicConsultationDto, @MappingTarget CustomerBasicConsultationEntity customerBasicConsultationEntity);
}