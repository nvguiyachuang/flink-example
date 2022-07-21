package com.hello.world.java.metric;

import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.util.concurrent.TimeUnit;

public class CustSource implements SourceFunction<String> {
    @Override
    public void run(SourceContext sourceContext) throws Exception {
        for (int i = 0; i < 2000; i++) {
            TimeUnit.SECONDS.sleep(1);
            String t = i + "";
            sourceContext.collect(t);
        }
    }

    @Override
    public void cancel() {

    }
}
