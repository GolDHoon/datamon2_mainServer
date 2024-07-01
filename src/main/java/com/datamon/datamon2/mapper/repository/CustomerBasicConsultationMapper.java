package com.datamon.datamon2.mapper.repository;

import com.datamon.datamon2.dto.repository.CustomerBasicConsultationCheckDto;
import com.datamon.datamon2.entity.CustomerBasicConsultationEntity;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface CustomerBasicConsultationMapper {
    CustomerBasicConsultationEntity toEntity(CustomerBasicConsultationCheckDto customerBasicConsultationDto);

    CustomerBasicConsultationCheckDto toDto(CustomerBasicConsultationEntity customerBasicConsultationEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    CustomerBasicConsultationEntity partialUpdate(CustomerBasicConsultationCheckDto customerBasicConsultationDto, @MappingTarget CustomerBasicConsultationEntity customerBasicConsultationEntity);
}