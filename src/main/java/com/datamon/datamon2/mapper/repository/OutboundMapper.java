package com.datamon.datamon2.mapper.repository;

import com.datamon.datamon2.dto.repository.OutboundDto;
import com.datamon.datamon2.entity.OutboundEntity;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface OutboundMapper {
    OutboundEntity toEntity(OutboundDto outboundDto);

    OutboundDto toDto(OutboundEntity outboundEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    OutboundEntity partialUpdate(OutboundDto outboundDto, @MappingTarget OutboundEntity outboundEntity);
}