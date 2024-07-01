package com.datamon.datamon2.mapper.repository;

import com.datamon.datamon2.dto.repository.MemberInfomationDto;
import com.datamon.datamon2.entity.MemberInfomationEntity;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface MemberInfomationMapper {
    MemberInfomationEntity toEntity(MemberInfomationDto memberInfomationDto);

    MemberInfomationDto toDto(MemberInfomationEntity memberInfomationEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    MemberInfomationEntity partialUpdate(MemberInfomationDto memberInfomationDto, @MappingTarget MemberInfomationEntity memberInfomationEntity);
}