package com.hello.world.flink15;

import org.apache.flink.table.data.TimestampData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZoneId;

public class LogDemo {
    private static final Logger log = LoggerFactory.getLogger(LogDemo.class);

    public static void main(String[] args) {
        log.debug("rgwegwegwegwegwe");
        log.info("rgwegwegwegwegwe");
        log.error("rgwegwegwegwegwe");

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
