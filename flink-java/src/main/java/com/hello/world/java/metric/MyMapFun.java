package com.hello.world.java.metric;

import org.apache.flink.api.common.functions.RichMapFunction;

public class MyMapFun extends RichMapFunction<String, String> {
    @Override
    public String map(String s) throws Exception {
        return s;
    }
}
