package org.example.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class TimeUtil {

    private static final String PATTERN = "yyyy-MM-dd HH:mm:ss";

    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern(PATTERN);

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

}
