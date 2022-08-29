package org.example;

import org.apache.flink.table.data.TimestampData;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

public class Demo {
    public static void main(String[] args) {
        long milliseconds = 1661416458978L;
        TimestampData timestampData = TimestampData.fromEpochMillis(milliseconds);
        long millisecond = timestampData.getMillisecond();
        System.out.println(millisecond);
    }

    private static Long epochMills(ZoneId shiftTimeZone, String timestampStr) {
//        LocalDateTime localDateTime = LocalDateTime.from(1661416458978L);
//        ZoneOffset zoneOffset = ZoneOffset.MAX;
//        return localDateTime.toInstant(zoneOffset).toEpochMilli();
        return null;
    }

}
