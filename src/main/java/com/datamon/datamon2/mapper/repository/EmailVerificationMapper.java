package com.datamon.datamon2.mapper.repository;

import com.datamon.datamon2.dto.repository.EmailVerificationDto;
import com.datamon.datamon2.entity.EmailVerificationEntity;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface EmailVerificationMapper {
    EmailVerificationEntity toEntity(EmailVerificationDto emailVerificationDto);

    EmailVerificationDto toDto(EmailVerificationEntity emailVerificationEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    EmailVerificationEntity partialUpdate(EmailVerificationDto emailVerificationDto, @MappingTarget EmailVerificationEntity emailVerificationEntity);
}