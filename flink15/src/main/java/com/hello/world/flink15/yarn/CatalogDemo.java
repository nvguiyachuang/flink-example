package com.hello.world.flink15.yarn;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

public class CatalogDemo {
    public static void main(String[] args) {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironment();

        EnvironmentSettings settings = EnvironmentSettings
                .newInstance()
                .inStreamingMode()
                .build();

        StreamTableEnvironment tEnv = StreamTableEnvironment.create(env, settings);

        tEnv.executeSql("CREATE CATALOG my_catalog WITH(\n" +
                "    'type' = 'jdbc',\n" +
                "    'default-database' = 'zdemo',\n" +
                "    'username' = 'root',\n" +
                "    'password' = '1234',\n" +
                "    'base-url' = 'jdbc:mysql://127.0.0.1:3306'\n" +
                ")");

        tEnv.executeSql("USE CATALOG my_catalog");

        tEnv.executeSql("show catalogs").print();

        /* 需要引入以下依赖

         *    <dependency>
         *             <groupId>org.apache.commons</groupId>
         *             <artifactId>commons-compress</artifactId>
         *             <version>1.21</version>
         *         </dependency>
         */
        tEnv.executeSql("show databases").print();
        tEnv.executeSql("show tables").print();

        tEnv.executeSql("select * from test").print();
    }
}
