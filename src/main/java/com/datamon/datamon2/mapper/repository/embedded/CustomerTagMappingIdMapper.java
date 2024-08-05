package com.datamon.datamon2.mapper.repository.embedded;

import com.datamon.datamon2.dto.repository.embedded.CustomerTagMappingDtoId;
import com.datamon.datamon2.entity.embedded.CustomerTagMappingEntityId;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface CustomerTagMappingIdMapper {
    CustomerTagMappingEntityId toEntity(CustomerTagMappingDtoId customerTagMappingDtoId);

    CustomerTagMappingDtoId toDto(CustomerTagMappingEntityId customerTagMappingEntityId);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    CustomerTagMappingEntityId partialUpdate(CustomerTagMappingDtoId customerTagMappingDtoId, @MappingTarget CustomerTagMappingEntityId customerTagMappingEntityId);
}