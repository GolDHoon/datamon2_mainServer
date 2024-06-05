package com.datamon.datamon2.util;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class InstantUtil {
    public Instant getNow(){
        return ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toInstant();
    }
}
