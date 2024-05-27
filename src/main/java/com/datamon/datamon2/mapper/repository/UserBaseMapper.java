package com.datamon.datamon2.mapper.repository;

import com.datamon.datamon2.dto.repository.UserBaseDto;
import com.datamon.datamon2.entity.UserBaseEntity;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface UserBaseMapper {
    UserBaseEntity toEntity(UserBaseDto userBaseDto);

    UserBaseDto toDto(UserBaseEntity userBaseEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    UserBaseEntity partialUpdate(UserBaseDto userBaseDto, @MappingTarget UserBaseEntity userBaseEntity);
}