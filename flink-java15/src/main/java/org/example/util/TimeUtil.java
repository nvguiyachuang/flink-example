package org.example.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class TimeUtil {

    private static final String PATTERN = "yyyy-MM-dd HH:mm:ss";

    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern(PATTERN, Locale.CHINA);

    /**
     * timestamp to format str
     */
    public static String timestampToString(Long timestamp) {
        if (null == timestamp) return null;

        LocalDateTime localDateTime = Instant
                .ofEpochMilli(timestamp)
                .atZone(ZoneOffset.ofHours(8))
                .toLocalDateTime();

        return localDateTime.format(dtf);
    }

    public static Long stringToTimestamp(String str){
        LocalDateTime parse = LocalDateTime.parse(str, dtf);
        return parse.toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
    }

    public static void main(String[] args) {
        System.out.println(stringToTimestamp("2020-01-01 00:00:00"));
    }
}
