package com.hello.world.flink15.yarn;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

/**
 * apply to standalone, yarn-session, k8s-session
 */
public class SessionDemo {
    public static void main(String[] args) {
        StreamExecutionEnvironment env = StreamExecutionEnvironment
                .createRemoteEnvironment("192.168.110.62", 8080, null,null);

        EnvironmentSettings settings = EnvironmentSettings
                .newInstance()
                .inStreamingMode()
                .build();

        StreamTableEnvironment tEnv = StreamTableEnvironment.create(env, settings);

        // sql
        String sql1 = "CREATE TABLE Orders (\n" +
                "  order_number INT,\n" +
                "  price        DECIMAL(32,2),\n" +
                "  order_time   TIMESTAMP(3)\n" +
                ") WITH (\n" +
                " 'connector' = 'datagen',\n" +
                " 'rows-per-second' = '1',\n" +
                " 'fields.order_number.kind' = 'sequence',\n" +
                " 'fields.order_number.start' = '1',\n" +
                " 'fields.order_number.end' = '100000'\n" +
                ")";

        String sql2 = "CREATE TABLE pt (\n" +
                "ordertotal INT,\n" +
                "numtotal INT\n" +
                ") WITH (\n" +
                "'connector' = 'print'\n" +
                ")";

        String sql3 = "insert into pt select 1 as ordertotal ,sum(order_number)*2 as numtotal from Orders";

        tEnv.executeSql(sql1);
        tEnv.executeSql(sql2);
        tEnv.executeSql(sql3);
    }
}
