package com.datamon.datamon2.util;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class InstantUtil {
    public Instant getNow(){
        Instant now = Instant.now();
        ZoneId korea = ZoneId.of("Asia/Seoul");
        ZonedDateTime koreaZonedDateTime = now.atZone(korea);

        return koreaZonedDateTime.toInstant();
    }
}
