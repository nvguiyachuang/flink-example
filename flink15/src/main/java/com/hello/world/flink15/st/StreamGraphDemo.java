package com.hello.world.flink15.st;

import org.apache.flink.api.dag.Transformation;
import org.apache.flink.client.deployment.ClusterDeploymentException;
import org.apache.flink.client.deployment.ClusterSpecification;
import org.apache.flink.client.program.ClusterClient;
import org.apache.flink.client.program.ClusterClientProvider;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.GlobalConfiguration;
import org.apache.flink.runtime.jobgraph.JobGraph;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.graph.StreamGraph;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.table.api.internal.TableEnvironmentImpl;
import org.apache.flink.table.delegation.Parser;
import org.apache.flink.table.delegation.Planner;
import org.apache.flink.table.operations.ModifyOperation;
import org.apache.flink.table.operations.Operation;
import org.apache.flink.yarn.YarnClientYarnClusterInformationRetriever;
import org.apache.flink.yarn.YarnClusterDescriptor;
import org.apache.flink.yarn.configuration.YarnConfigOptions;
import org.apache.flink.yarn.configuration.YarnLogConfigUtil;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.yarn.api.records.ApplicationId;
import org.apache.hadoop.yarn.client.api.YarnClient;
import org.apache.hadoop.yarn.conf.YarnConfiguration;

import java.net.URI;
import java.util.*;

/**
 * streamGraph and jobGraph
 */
public class StreamGraphDemo {
    public static void main(String[] args) throws Exception {
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
                " 'fields.order_number.end' = '1000'\n" +
                ")";

        String sql2 = "CREATE TABLE pt (\n" +
                "ordertotal INT,\n" +
                "numtotal INT\n" +
                ") WITH (\n" +
                "'connector' = 'print'\n" +
                ")";

        String sql3 = "insert into pt select 1 as ordertotal ,sum(order_number)*2 as numtotal from Orders";

        // env config
        HashMap<String, String> envConf = new HashMap<>();
        envConf.put("execution.checkpointing.interval", "70s");
        envConf.put("table.exec.resource.default-parallelism", "2");

        submit(sql1,sql2, sql3,envConf);
    }

    private static void submit(String sql1, String sql2, String sql3, HashMap<String, String> envConf)
            throws ClusterDeploymentException {

//        Configuration fromMap = Configuration.fromMap(conf);
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironment();

        EnvironmentSettings settings = EnvironmentSettings
                .newInstance()
                .inStreamingMode()
                .build();

        StreamTableEnvironment tEnv = StreamTableEnvironment.create(env, settings);

        // env conf
        Configuration fromMap = Configuration.fromMap(envConf);
        env.getConfig().configure(fromMap, null);

        // tEnv conf
        Configuration customConf = tEnv.getConfig().getConfiguration();
        for (Map.Entry<String, String> entry : envConf.entrySet()) {
            customConf.setString(entry.getKey(), entry.getValue());
        }

        tEnv.executeSql(sql1);
        tEnv.executeSql(sql2);

        // get streamGraph
        TableEnvironmentImpl env2 = (TableEnvironmentImpl) tEnv;
        Planner planner = env2.getPlanner();
        Parser parser = planner.getParser();
        List<Operation> operations = parser.parse(sql3);
        System.out.println(operations);

        Operation operation = operations.get(0);
        List<ModifyOperation> modifyOperations = new ArrayList<>();
        modifyOperations.add((ModifyOperation) operation);

        List<Transformation<?>> trans = planner.translate(modifyOperations);

        for (Transformation<?> tran : trans) {
            env.addOperator(tran);
        }
        StreamGraph streamGraph = env.getStreamGraph();
        JobGraph jobGraph = streamGraph.getJobGraph();

        System.out.println(jobGraph);

        Configuration configuration = GlobalConfiguration.loadConfiguration("/opt/dev/flink/conf");
        configuration.set(YarnConfigOptions.PROVIDED_LIB_DIRS, Collections.singletonList("hdfs:///flink/lib"));
        YarnLogConfigUtil.setLogConfigFileInConfig(configuration, "/opt/dev/flink/conf");

        // add flink conf
        configuration.addAll(fromMap);
        configuration.addAll(customConf);

        YarnConfiguration yarnConfiguration = new YarnConfiguration();
        yarnConfiguration.addResource(new Path(URI.create("/opt/dev/hadoop/etc/hadoop/yarn-site.xml")));
        yarnConfiguration.addResource(new Path(URI.create("/opt/dev/hadoop/etc/hadoop/core-site.xml")));
        yarnConfiguration.addResource(new Path(URI.create("/opt/dev/hadoop/etc/hadoop/hdfs-site.xml")));
        YarnClient yarnClient = YarnClient.createYarnClient();
        yarnClient.init(yarnConfiguration);
        yarnClient.start();

        ClusterSpecification clusterSpecification = new ClusterSpecification.ClusterSpecificationBuilder().createClusterSpecification();
        YarnClusterDescriptor yarnClusterDescriptor = new YarnClusterDescriptor(configuration, yarnConfiguration, yarnClient,
                YarnClientYarnClusterInformationRetriever.create(yarnClient), true);
        ClusterClientProvider<ApplicationId> applicationIdClusterClientProvider = yarnClusterDescriptor
                .deployJobCluster(clusterSpecification, jobGraph, true);

        ClusterClient<ApplicationId> clusterClient = applicationIdClusterClientProvider.getClusterClient();
        System.out.println(clusterClient);
    }
}
