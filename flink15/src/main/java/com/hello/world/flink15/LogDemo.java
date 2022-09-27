package com.hello.world.flink15;

import org.apache.flink.table.data.TimestampData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

}
