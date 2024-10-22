package com.datamon.datamon2.mapper.repository;

import com.datamon.datamon2.dto.repository.AccountApprovalRequestDto;
import com.datamon.datamon2.entity.AccountApprovalRequestEntity;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface AccountApprovalRequestMapper {
    AccountApprovalRequestEntity toEntity(AccountApprovalRequestDto accountApprovalRequestDto);

    AccountApprovalRequestDto toDto(AccountApprovalRequestEntity accountApprovalRequestEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    AccountApprovalRequestEntity partialUpdate(AccountApprovalRequestDto accountApprovalRequestDto, @MappingTarget AccountApprovalRequestEntity accountApprovalRequestEntity);
}