package com.datamon.datamon2.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    DateTimeFormatter dateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public String LocalDateTimeToDateTimeStr(LocalDateTime date){
        return dateTimeFormatter.format(date);
    }

    public String LocalDateTimeToDateStr(LocalDateTime date){
        return dateTime.format(date);
    }
}
