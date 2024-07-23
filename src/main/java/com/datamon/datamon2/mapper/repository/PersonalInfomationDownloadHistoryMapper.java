package com.datamon.datamon2.mapper.repository;

import com.datamon.datamon2.dto.repository.PersonalInfomationDownloadHistoryDto;
import com.datamon.datamon2.entity.PersonalInfomationDownloadHistoryEntity;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface PersonalInfomationDownloadHistoryMapper {
    PersonalInfomationDownloadHistoryEntity toEntity(PersonalInfomationDownloadHistoryDto personalInfomationDownloadHistoryDto);

    PersonalInfomationDownloadHistoryDto toDto(PersonalInfomationDownloadHistoryEntity personalInfomationDownloadHistoryEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    PersonalInfomationDownloadHistoryEntity partialUpdate(PersonalInfomationDownloadHistoryDto personalInfomationDownloadHistoryDto, @MappingTarget PersonalInfomationDownloadHistoryEntity personalInfomationDownloadHistoryEntity);
}