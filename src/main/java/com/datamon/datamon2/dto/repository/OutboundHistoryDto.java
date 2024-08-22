package com.datamon.datamon2.dto.repository;

import com.datamon.datamon2.dto.repository.common.DrivenCommonCheckUserDto;
import com.datamon.datamon2.entity.OutboundHistoryEntity;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link OutboundHistoryEntity}
 */
@Data
public class OutboundHistoryDto extends DrivenCommonCheckUserDto implements Serializable {
    String idx;
    String originalIdx;
    int sort;
    String saveReason;
    Integer userId;
    String custId;
    String telColumn;
    String memo;
    String orderMemo;
    LocalDateTime scheduledCallbackDate;
    LocalDateTime scheduledConversionDate;
    String cdbsCode;
    String statusChangeReason;
    String cdbqCode;
    String qualityChangeReason;

    public void createId () {
        idx = custId + "_" + String.format("%010d", sort);
    }

    public void setHistory (OutboundDto outboundDto) {
        originalIdx = outboundDto.getIdx();
        userId = outboundDto.getUserId();
        custId = outboundDto.getCustId();
        telColumn = outboundDto.getTelColumn();
        memo = outboundDto.getMemo();
        orderMemo = outboundDto.getOrderMemo();
        scheduledCallbackDate = outboundDto.getScheduledCallbackDate();
        scheduledConversionDate = outboundDto.getScheduledConversionDate();
    }

    public void setCustDbStatusInfo (CustomerInformationDto custDbStatusInfo){
        cdbsCode = custDbStatusInfo.getCdbsCode();
        statusChangeReason = custDbStatusInfo.getStatusChangeReason();
        cdbqCode = custDbStatusInfo.getCdbqCode();
        qualityChangeReason = custDbStatusInfo.getQualityChangeReason();
    }
}