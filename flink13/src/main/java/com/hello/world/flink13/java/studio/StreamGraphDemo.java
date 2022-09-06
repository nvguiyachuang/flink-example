package com.hello.world.flink13.java.studio;

import org.apache.flink.api.dag.Transformation;
import org.apache.flink.configuration.PipelineOptions;
import org.apache.flink.runtime.jobgraph.JobGraph;
import org.apache.flink.runtime.jobgraph.jsonplan.JsonPlanGenerator;
import org.apache.flink.runtime.rest.messages.JobPlanInfo;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.graph.StreamGraph;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.TableConfig;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.table.api.internal.TableEnvironmentImpl;
import org.apache.flink.table.delegation.Parser;
import org.apache.flink.table.delegation.Planner;
import org.apache.flink.table.operations.ModifyOperation;
import org.apache.flink.table.operations.Operation;
import org.apache.flink.table.planner.utils.ExecutorUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * streamGraph and jobGraph
 */
public class StreamGraphDemo {
    public static void main(String[] args) {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        EnvironmentSettings settings = EnvironmentSettings
                .newInstance()
                .inStreamingMode()
                .build();

        StreamTableEnvironment tEnv = StreamTableEnvironment.create(env, settings);

        TableConfig tableConfig = tEnv.getConfig();
        tableConfig.getConfiguration().setString("execution.checkpointing.interval", "60s");

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

        String printSql = "CREATE TABLE print_table (\n" +
                "order_id INT,\n" +
                "order_date TIMESTAMP(0),\n" +
                "customer_name STRING,\n" +
                "price DECIMAL(10, 5),\n" +
                "product_id INT,\n" +
                "order_status BOOLEAN\n" +
                ") WITH (\n" +
                "  'connector' = 'print'\n" +
                ")";
        tEnv.executeSql(printSql);

        String sql = "insert into print_table select * from orders";

        // get streamGraph
        TableEnvironmentImpl env2 = (TableEnvironmentImpl) tEnv;
        Planner planner = env2.getPlanner();
        Parser parser = planner.getParser();
        List<Operation> operations = parser.parse(sql);
        System.out.println(operations);

        Operation operation = operations.get(0);
        List<ModifyOperation> modifyOperations = new ArrayList<>();
        modifyOperations.add((ModifyOperation) operation);

        List<Transformation<?>> trans = planner.translate(modifyOperations);

        // streamGraph
        StreamGraph streamGraph = ExecutorUtils.generateStreamGraph(env, trans);

        if (tableConfig.getConfiguration().containsKey(PipelineOptions.NAME.key())) {
            streamGraph.setJobName(tableConfig.getConfiguration().getString(PipelineOptions.NAME));
        }

        System.out.println(streamGraph);

        // jobGraph
        JobGraph jobGraph = streamGraph.getJobGraph();
        String generatePlan = JsonPlanGenerator.generatePlan(jobGraph);
        JobPlanInfo jobPlanInfo = new JobPlanInfo(generatePlan);
        String jsonPlan = jobPlanInfo.getJsonPlan();
        /*
{
    "jid": "a812f1eb6bf0c1f341f0bc35819e642b",
    "name": "Flink Streaming Job",
    "nodes": [
        {
            "id": "cbc357ccb763df2852fee8c4fc7d55f2",
            "parallelism": 8,
            "operator": "",
            "operator_strategy": "",
            "description": "Source: TableSourceScan(
            table=[[default_catalog, default_database, orders]],
            fields=[order_id, order_date, customer_name, price, product_id, order_status]) ->
            Sink: Sink(table=[default_catalog.default_database.print_table],
            fields=[order_id, order_date, customer_name, price, product_id, order_status])",
            "optimizer_properties": { }
        }
    ]
}
         */
        System.out.println(jsonPlan);
    }
}
