package com.datamon.datamon2.mapper.repository;

import com.datamon.datamon2.dto.repository.SmsVerificationDto;
import com.datamon.datamon2.entity.SmsVerificationEntity;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface SmsVerificationMapper {
    SmsVerificationEntity toEntity(SmsVerificationDto smsVerificationDto);

    SmsVerificationDto toDto(SmsVerificationEntity smsVerificationEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    SmsVerificationEntity partialUpdate(SmsVerificationDto smsVerificationDto, @MappingTarget SmsVerificationEntity smsVerificationEntity);
}