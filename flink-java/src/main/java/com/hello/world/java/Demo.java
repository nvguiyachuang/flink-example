package com.hello.world.java;

import org.apache.flink.api.common.typeinfo.BasicTypeInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.ManagementFactory;

public class Demo {
    private static final Logger log = LoggerFactory.getLogger(Demo.class);

    public static void main(String[] args) {
        log.debug("rgwegwegwegwegwe");
        log.info("rgwegwegwegwegwe");
        log.error("rgwegwegwegwegwe");

        BasicTypeInfo<String> stringTypeInfo = BasicTypeInfo.STRING_TYPE_INFO;

        String appId = System.getenv("_APP_ID");
        System.out.println(appId);

        String identity = ManagementFactory.getRuntimeMXBean().getName();
        String pid = identity.split("@")[0];
        System.out.println(identity);
        System.out.println(pid);
    }
}
