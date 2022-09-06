package com.hello.world.flink13.java;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

import java.lang.reflect.Field;

public class Demo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
//        env.getConfig().setAutoWatermarkInterval(1000);

        Field field = Class.forName("org.apache.flink.streaming.api.environment.StreamExecutionEnvironment")
                .getDeclaredField("configuration");
        field.setAccessible(true);
        org.apache.flink.configuration.Configuration conf = (Configuration) field.get(env);
        conf.setString("rest.bind-port", "8080");


        EnvironmentSettings settings = EnvironmentSettings
                .newInstance()
                .inStreamingMode()
                .build();

//        TableEnvironment tEnv = TableEnvironment.create(settings);
        StreamTableEnvironment tEnv = StreamTableEnvironment.create(env, settings);

        tEnv.getConfig().getConfiguration().setString("execution.checkpointing.interval", "60s");

        tEnv.executeSql("CREATE TABLE orders (\n" +
                "order_id INT,\n" +
                "order_date TIMESTAMP(0),\n" +
                "customer_name STRING,\n" +
                "price DECIMAL(10, 5),\n" +
                "product_id INT,\n" +
                "order_status BOOLEAN,\n" +
                "PRIMARY KEY(order_id) NOT ENFORCED\n" +
                ") WITH (\n" +
                "'connector' = 'mysql-cdc',\n" +
                "'hostname' = 'node',\n" +
                "'port' = '3306',\n" +
                "'username' = 'root',\n" +
                "'password' = '000000',\n" +
                "'database-name' = 'mydb',\n" +
                "'table-name' = 'orders')");

        tEnv
                .executeSql("select * from orders")
                .print();
    }
}
