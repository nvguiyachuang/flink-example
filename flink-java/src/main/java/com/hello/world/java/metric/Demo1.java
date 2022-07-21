package com.hello.world.java.metric;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.lang.reflect.Field;

public class Demo1 {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
//        env.getConfig().setAutoWatermarkInterval(1000);

        Field field = Class.forName("org.apache.flink.streaming.api.environment.StreamExecutionEnvironment")
                .getDeclaredField("configuration");
        field.setAccessible(true);
        org.apache.flink.configuration.Configuration conf = (Configuration) field.get(env);
        conf.setString("rest.bind-port", "8080");

        env.addSource(new CustSource())
                .map(new MyMapFun())
                .print();

        env.execute();
    }
}
