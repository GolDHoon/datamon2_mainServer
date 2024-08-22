package com.datamon.datamon2.dto.input.call;

import lombok.Data;

@Data
public class UpdateOutboundDto {
    String idx;
    String mode;
    String memo;
    String callBackDate;
    String conversionDate;
    String cdbsCode;
    String statusChangeReason;
    String cdbqCode;
    String qualityChangeReason;
}
