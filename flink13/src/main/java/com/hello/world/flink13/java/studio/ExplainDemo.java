package com.hello.world.flink13.java.studio;

import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.TableEnvironment;

public class ExplainDemo {
    public static void main(String[] args) {
        EnvironmentSettings settings = EnvironmentSettings
                .newInstance()
                .inStreamingMode()
                .build();

        TableEnvironment tEnv = TableEnvironment.create(settings);

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

        String sql = "select * from orders";

//        tEnv.executeSql(sql).print();

        String explain = tEnv.explainSql(sql);
        System.out.println(explain);
    }
}
