package com.datamon.datamon2.mapper.repository;

import com.datamon.datamon2.dto.repository.UserLpgeMappingDto;
import com.datamon.datamon2.entity.UserLpgeMappingEntity;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface UserLpgeMappingMapper {
    UserLpgeMappingEntity toEntity(UserLpgeMappingDto userLpgeMappingDto);

    UserLpgeMappingDto toDto(UserLpgeMappingEntity userLpgeMappingEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    UserLpgeMappingEntity partialUpdate(UserLpgeMappingDto userLpgeMappingDto, @MappingTarget UserLpgeMappingEntity userLpgeMappingEntity);
}