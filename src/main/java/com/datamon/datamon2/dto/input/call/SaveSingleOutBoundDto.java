package com.datamon.datamon2.dto.input.call;

import lombok.Data;

@Data
public class SaveSingleOutBoundDto {
    int userId;
    String custId;
    String memo;
    String orderMemo;
}
