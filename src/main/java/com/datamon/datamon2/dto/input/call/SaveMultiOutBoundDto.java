package com.datamon.datamon2.dto.input.call;

import lombok.Data;

import java.util.List;

@Data
public class SaveMultiOutBoundDto {
    int userId;
    List<String> custId;
    String memo;
    String orderMemo;
}
