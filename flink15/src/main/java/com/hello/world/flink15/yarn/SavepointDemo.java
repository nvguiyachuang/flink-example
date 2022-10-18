package com.hello.world.flink15.yarn;

import org.apache.flink.api.common.JobID;
import org.apache.flink.client.deployment.ClusterRetrieveException;
import org.apache.flink.client.program.ClusterClient;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.DeploymentOptions;
import org.apache.flink.configuration.GlobalConfiguration;
import org.apache.flink.core.execution.SavepointFormatType;
import org.apache.flink.yarn.YarnClientYarnClusterInformationRetriever;
import org.apache.flink.yarn.YarnClusterClientFactory;
import org.apache.flink.yarn.YarnClusterDescriptor;
import org.apache.flink.yarn.configuration.YarnConfigOptions;
import org.apache.flink.yarn.configuration.YarnLogConfigUtil;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.yarn.api.records.ApplicationId;
import org.apache.hadoop.yarn.client.api.YarnClient;
import org.apache.hadoop.yarn.conf.YarnConfiguration;

import java.net.URI;
import java.util.Collections;

public class SavepointDemo {
    public static Configuration configuration;
    public static YarnConfiguration yarnConfiguration = new YarnConfiguration();
    public static YarnClient yarnClient = YarnClient.createYarnClient();

    public static final String APP_ID = "";
    public static final String JOB_ID = "";

    public static void main(String[] args) throws ClusterRetrieveException {
        initYarn();

        YarnClusterClientFactory clusterClientFactory = new YarnClusterClientFactory();
        configuration.set(YarnConfigOptions.APPLICATION_ID, APP_ID);
        ApplicationId applicationId = clusterClientFactory.getClusterId(configuration);

        YarnClusterDescriptor clusterDescriptor = new YarnClusterDescriptor(configuration, yarnConfiguration,
                yarnClient, YarnClientYarnClusterInformationRetriever.create(yarnClient), true);

        ClusterClient<ApplicationId> clusterClient = clusterDescriptor.retrieve(
                applicationId).getClusterClient();

        clusterClient.triggerSavepoint(JobID.fromHexString(JOB_ID), "savePointDir", SavepointFormatType.DEFAULT);
//        clusterClient.cancelWithSavepoint(JobID.fromHexString(JOB_ID), "savePointDir", SavepointFormatType.DEFAULT);
//        clusterClient.stopWithSavepoint(JobID.fromHexString(JOB_ID), true, "savePointDir", SavepointFormatType.DEFAULT);
    }

    private static void initYarn() {
        String flinkConfPath = "/opt/dev/flink/conf";

        configuration = GlobalConfiguration.loadConfiguration(flinkConfPath);
        configuration.set(DeploymentOptions.TARGET, "yarn-application");
        configuration.set(YarnConfigOptions.APPLICATION_NAME, "yarn-app-name-test");
        configuration.set(YarnConfigOptions.PROVIDED_LIB_DIRS, Collections.singletonList("hdfs:///flink/lib"));

        configuration.setString("fs.hdfs.hadoopconf", "/opt/dev/hadoop/etc/hadoop");
        configuration.setString("pipeline.name", "pipeline-name-test");

//        String uuid = UUID.randomUUID().toString().replace("-", "");
//        System.out.println(uuid);
//        if (configuration.contains(CheckpointingOptions.CHECKPOINTS_DIRECTORY)) {
//            configuration.set(CheckpointingOptions.CHECKPOINTS_DIRECTORY, configuration.getString(CheckpointingOptions.CHECKPOINTS_DIRECTORY) + "/" + uuid);
//        }
//        if (configuration.contains(CheckpointingOptions.SAVEPOINT_DIRECTORY)) {
//            configuration.set(CheckpointingOptions.SAVEPOINT_DIRECTORY, configuration.getString(CheckpointingOptions.SAVEPOINT_DIRECTORY) + "/" + uuid);
//        }

        YarnLogConfigUtil.setLogConfigFileInConfig(configuration, flinkConfPath);

        // yarn client
        yarnConfiguration.addResource(new Path(URI.create("/opt/dev/hadoop/etc/hadoop/yarn-site.xml")));
        yarnConfiguration.addResource(new Path(URI.create("/opt/dev/hadoop/etc/hadoop/core-site.xml")));
        yarnConfiguration.addResource(new Path(URI.create("/opt/dev/hadoop/etc/hadoop/hdfs-site.xml")));
        yarnClient.init(yarnConfiguration);
        yarnClient.start();
    }
}
