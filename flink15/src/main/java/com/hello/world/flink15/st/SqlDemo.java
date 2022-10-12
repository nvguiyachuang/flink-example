package com.hello.world.flink15.st;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

import java.lang.reflect.Field;
import java.util.HashMap;

public class SqlDemo {
    public static void main(String[] args) throws Exception {
        String sql1 = "CREATE TABLE Orders (\n" +
                "  order_number INT,\n" +
                "  price        DECIMAL(32,2),\n" +
                "  order_time   TIMESTAMP(3)\n" +
                ") WITH (\n" +
                " 'connector' = 'datagen',\n" +
                " 'rows-per-second' = '1',\n" +
                " 'fields.order_number.kind' = 'sequence',\n" +
                " 'fields.order_number.start' = '1',\n" +
                " 'fields.order_number.end' = '1000'\n" +
                ")";

        String sql2 = "CREATE TABLE pt (\n" +
                "ordertotal INT,\n" +
                "numtotal INT\n" +
                ") WITH (\n" +
                "'connector' = 'print'\n" +
                ")";

        String sql3 = "insert into pt select 1 as ordertotal ,sum(order_number)*2 as numtotal from Orders";

        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironment();

        EnvironmentSettings settings = EnvironmentSettings
                .newInstance()
                .inStreamingMode()
                .build();

        StreamTableEnvironment tEnv = StreamTableEnvironment.create(env, settings);

        Field field = Class.forName("org.apache.flink.streaming.api.environment.StreamExecutionEnvironment")
                .getDeclaredField("configuration");
        field.setAccessible(true);
        org.apache.flink.configuration.Configuration conf = (Configuration) field.get(env);
        conf.setString("rest.bind-port", "8080");

        // it takes effort
        tEnv.getConfig().getConfiguration().setString("execution.checkpointing.interval", "40s");
        // it doesn't take effort
        HashMap<String, String> envConf = new HashMap<>();
        envConf.put("execution.checkpointing.interval", "50s");
        Configuration fromMap = Configuration.fromMap(envConf);
        env.getConfig().configure(fromMap, null);

        tEnv.executeSql(sql1);
        tEnv.executeSql(sql2);
        tEnv.executeSql(sql3);
    }
}
