package org.example.st;

import org.apache.flink.table.api.TableEnvironment;

public class DemoTest {
    public static void main(String[] args) throws Exception {
        String sql = "";
        String sql2 = "";

        Executor executor = new Executor();
        TableEnvironment tEnv = executor.tEnv;

        tEnv.executeSql(sql);
        tEnv.executeSql(sql2);
    }
}
