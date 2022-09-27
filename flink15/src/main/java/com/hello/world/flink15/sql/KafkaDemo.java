package com.hello.world.flink15.sql;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

public class KafkaDemo {
    public static void main(String[] args) {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(2);

        EnvironmentSettings settings = EnvironmentSettings
                .newInstance()
                .inStreamingMode()
                .build();

        StreamTableEnvironment tEnv = StreamTableEnvironment.create(env, settings);

        tEnv.executeSql("CREATE TABLE source (\n" +
                "  `user_id` String,\n" +
                "  `item_id` String\n" +
                ") WITH (\n" +
                "  'connector' = 'kafka',\n" +
                "  'topic' = 'test',\n" +
                "  'properties.bootstrap.servers' = 'localhost:9092',\n" +
                "  'properties.group.id' = 'testGroup',\n" +
                "  'scan.startup.mode' = 'earliest-offset',\n" +
                "  'format' = 'csv',\n" +
                "  'csv.ignore-parse-errors' = 'true'\n" +
                ");");

        tEnv.executeSql("CREATE TABLE sink (\n" +
                "  `user_id` String,\n" +
                "  `item_id` String\n" +
                ") WITH (\n" +
                "  'connector' = 'kafka',\n" +
                "  'topic' = 'test1',\n" +
                "  'properties.bootstrap.servers' = 'localhost:9092',\n" +
                "  'format' = 'csv',\n" +
                "  'csv.ignore-parse-errors' = 'true'\n" +
                ");");

//        tEnv.executeSql("insert into sink select * from source");

        tEnv.executeSql("select * from source").print();
    }
}
