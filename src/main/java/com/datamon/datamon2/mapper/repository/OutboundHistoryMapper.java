package com.datamon.datamon2.mapper.repository;

import com.datamon.datamon2.dto.repository.OutboundHistoryDto;
import com.datamon.datamon2.entity.OutboundHistoryEntity;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface OutboundHistoryMapper {
    OutboundHistoryEntity toEntity(OutboundHistoryDto outboundHistoryDto);

    OutboundHistoryDto toDto(OutboundHistoryEntity outboundHistoryEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    OutboundHistoryEntity partialUpdate(OutboundHistoryDto outboundHistoryDto, @MappingTarget OutboundHistoryEntity outboundHistoryEntity);
}