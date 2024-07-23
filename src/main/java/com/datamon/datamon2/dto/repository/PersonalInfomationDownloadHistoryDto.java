package com.datamon.datamon2.dto.repository;

import com.datamon.datamon2.entity.PersonalInfomationDownloadHistoryEntity;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link PersonalInfomationDownloadHistoryEntity}
 */
@Value
public class PersonalInfomationDownloadHistoryDto implements Serializable {
    Long idx;
    Integer userId;
    String downloadUrl;
    String fileUrl;
    String fileName;
}