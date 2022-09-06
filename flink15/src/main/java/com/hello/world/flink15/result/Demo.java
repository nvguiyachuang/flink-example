package com.hello.world.flink15.result;

import org.apache.flink.core.execution.JobClient;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.TableResult;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.types.Row;
import org.apache.flink.util.CloseableIterator;

import java.util.List;
import java.util.Optional;

/**
 * TableResult
 * 可以获取到select语句的 data,changeData
 */
public class Demo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        EnvironmentSettings settings = EnvironmentSettings
                .newInstance()
                .inStreamingMode()
                .build();

//        TableEnvironment tEnv = TableEnvironment.create(settings);
        StreamTableEnvironment tEnv = StreamTableEnvironment.create(env, settings);

        tEnv.executeSql("CREATE TABLE Orders (\n" +
                "  order_number INT,\n" +
                "  price        DECIMAL(32,2),\n" +
                "  order_time   TIMESTAMP(3)\n" +
                ") WITH (\n" +
                " 'connector' = 'datagen',\n" +
                " 'rows-per-second' = '1',\n" +
                " 'fields.order_number.kind' = 'sequence',\n" +
                " 'fields.order_number.start' = '1',\n" +
                " 'fields.order_number.end' = '100'\n" +
                ")");

        TableResult tableResult = tEnv.executeSql("select * from Orders");

        List<String> columnNames = tableResult.getResolvedSchema().getColumnNames();
        System.out.println("columnNames: " + columnNames);

        Optional<JobClient> jobClient = tableResult.getJobClient();
        if (jobClient.isPresent()) {
            // jobId
            String jobId = jobClient.get().getJobID().toHexString();
            System.out.println("jobId: " + jobId);

            int maxNum = 0;

            CloseableIterator<Row> collect = tableResult.collect();
            while (collect.hasNext()) {
                maxNum++;
                if (maxNum > 10) {
                    // shut down job
                    jobClient.get().cancel();
                    break;
                }

                Row next = collect.next();
                System.out.println(next);
            }

            collect.close();
        }

    }
}
