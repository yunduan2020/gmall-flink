package com.tom.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateTimeUtil {
    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String toYMDhms(Date date){
        LocalDateTime localDateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        return formatter.format(localDateTime);
    }

    public static Long toTS(String YMDHms){
        LocalDateTime localDateTime = LocalDateTime.parse(YMDHms, formatter);
        return localDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }
}
