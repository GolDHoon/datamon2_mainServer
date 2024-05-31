package com.datamon.datamon2.mapper.repository.embeddable;

import com.datamon.datamon2.dto.repository.embeddable.UserLpgeMappingEntityIdDto;
import com.datamon.datamon2.entity.embeddable.UserLpgeMappingEntityId;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface UserLpgeMappingEntityIdMapper {
    UserLpgeMappingEntityId toEntity(UserLpgeMappingEntityIdDto userLpgeMappingEntityIdDto);

    UserLpgeMappingEntityIdDto toDto(UserLpgeMappingEntityId userLpgeMappingEntityId);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    UserLpgeMappingEntityId partialUpdate(UserLpgeMappingEntityIdDto userLpgeMappingEntityIdDto, @MappingTarget UserLpgeMappingEntityId userLpgeMappingEntityId);
}