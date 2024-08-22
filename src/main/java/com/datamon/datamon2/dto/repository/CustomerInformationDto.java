package com.datamon.datamon2.dto.repository;

import com.datamon.datamon2.dto.repository.common.DrivenCommonCheckUserDto;
import com.datamon.datamon2.entity.CustomerInformationEntity;
import lombok.Data;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;

/**
 * DTO for {@link CustomerInformationEntity}
 */
@Data
public class CustomerInformationDto extends DrivenCommonCheckUserDto implements Serializable {
    String  idx;
    String cdbtLowCode;
    String cdbsCode;
    String statusChangeReason;
    String cdbqCode;
    String qualityChangeReason;
    String utmSource;
    String utmMedium;
    String utmCampaign;
    String utmTerm;
    String utmContent;
    String ip;

    public void distribution(){
        cdbsCode = "CDBS_ACPL";
    }
}