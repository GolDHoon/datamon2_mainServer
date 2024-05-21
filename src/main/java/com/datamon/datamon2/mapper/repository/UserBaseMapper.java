package com.datamon.datamon2.mapper.repository;

import com.datamon.datamon2.dto.repository.UserBaseDto;
import com.datamon.datamon2.entity.UserBase;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface UserBaseMapper {
    UserBase toEntity(UserBaseDto userBaseDto);

    UserBaseDto toDto(UserBase userBase);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    UserBase partialUpdate(UserBaseDto userBaseDto, @MappingTarget UserBase userBase);
}