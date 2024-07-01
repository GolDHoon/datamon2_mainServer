package com.datamon.datamon2.mapper.repository;

import com.datamon.datamon2.dto.repository.UserCdbtMappingDto;
import com.datamon.datamon2.entity.UserCdbtMappingEntity;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface UserCdbtMappingMapper {
    UserCdbtMappingEntity toEntity(UserCdbtMappingDto userCdbtMappingDto);

    UserCdbtMappingDto toDto(UserCdbtMappingEntity userCdbtMappingEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    UserCdbtMappingEntity partialUpdate(UserCdbtMappingDto userCdbtMappingDto, @MappingTarget UserCdbtMappingEntity userCdbtMappingEntity);
}