package com.datamon.datamon2.dto.input.user;

import lombok.Data;

import java.util.List;

@Data
public class CopanyListAndCdbtDto {
    String cdbtLowCode;
    List<Integer> idxs;
}
