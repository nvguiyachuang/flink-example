package com.hello.world.flink15.st;

import org.apache.flink.client.deployment.ClusterSpecification;
import org.apache.flink.client.deployment.application.ApplicationConfiguration;
import org.apache.flink.client.program.ClusterClient;
import org.apache.flink.client.program.ClusterClientProvider;
import org.apache.flink.configuration.*;
import org.apache.flink.runtime.client.JobStatusMessage;
import org.apache.flink.yarn.YarnClientYarnClusterInformationRetriever;
import org.apache.flink.yarn.YarnClusterDescriptor;
import org.apache.flink.yarn.configuration.YarnConfigOptions;
import org.apache.flink.yarn.configuration.YarnLogConfigUtil;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.yarn.api.records.ApplicationId;
import org.apache.hadoop.yarn.client.api.YarnClient;
import org.apache.hadoop.yarn.conf.YarnConfiguration;

import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

public class YarnAppDemo {
    public static void main(String[] args) throws Exception {
        String flinkConfPath = "/opt/dev/flink/conf";

        Configuration configuration = GlobalConfiguration.loadConfiguration(flinkConfPath);
        configuration.set(DeploymentOptions.TARGET, "yarn-application");
        configuration.set(YarnConfigOptions.APPLICATION_NAME, "yarn-app-name-test");
        configuration.set(YarnConfigOptions.PROVIDED_LIB_DIRS, Collections.singletonList("hdfs:///flink/lib"));

        configuration.setString("fs.hdfs.hadoopconf", "/opt/dev/hadoop/etc/hadoop");
        configuration.setString("pipeline.name", "job-yarnapp");

        String uuid = UUID.randomUUID().toString().replace("-", "");
        if (configuration.contains(CheckpointingOptions.CHECKPOINTS_DIRECTORY)) {
            configuration.set(CheckpointingOptions.CHECKPOINTS_DIRECTORY, configuration.getString(CheckpointingOptions.CHECKPOINTS_DIRECTORY) + "/" + uuid);
        }
        if (configuration.contains(CheckpointingOptions.SAVEPOINT_DIRECTORY)) {
            configuration.set(CheckpointingOptions.SAVEPOINT_DIRECTORY, configuration.getString(CheckpointingOptions.SAVEPOINT_DIRECTORY) + "/" + uuid);
        }

        YarnLogConfigUtil.setLogConfigFileInConfig(configuration, flinkConfPath);

        // yarn client
        YarnConfiguration yarnConfiguration = new YarnConfiguration();
        yarnConfiguration.addResource(new Path(URI.create("/opt/dev/hadoop/etc/hadoop/yarn-site.xml")));
        yarnConfiguration.addResource(new Path(URI.create("/opt/dev/hadoop/etc/hadoop/core-site.xml")));
        yarnConfiguration.addResource(new Path(URI.create("/opt/dev/hadoop/etc/hadoop/hdfs-site.xml")));
        YarnClient yarnClient = YarnClient.createYarnClient();
        yarnClient.init(yarnConfiguration);
        yarnClient.start();

        configuration.set(PipelineOptions.JARS, Collections.singletonList("hdfs:///dlink/jar/dlink-app.jar"));
        String[] userJarParas = ("--id 35 --driver com.mysql.cj.jdbc.Driver --url" +
                " jdbc:mysql://127.0.0.1:3306/dlink?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&useSSL=false&zeroDateTimeBehavior=convertToNull&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true" +
                " --username root --password 1234")
                .split(" ");

        ApplicationConfiguration appConf =
                new ApplicationConfiguration(userJarParas, "com.dlink.app.MainApp");

        YarnClusterDescriptor yarnClusterDescriptor = new YarnClusterDescriptor(
                configuration, yarnConfiguration, yarnClient, YarnClientYarnClusterInformationRetriever.create(yarnClient), true);

        ClusterSpecification.ClusterSpecificationBuilder clusterSpecificationBuilder =
                new ClusterSpecification.ClusterSpecificationBuilder();

        ClusterClientProvider<ApplicationId> clusterClientProvider = yarnClusterDescriptor.deployApplicationCluster(
                clusterSpecificationBuilder.createClusterSpecification(), appConf);

        ClusterClient<ApplicationId> clusterClient = clusterClientProvider.getClusterClient();
        Collection<JobStatusMessage> jobStatusMessages = clusterClient.listJobs().get();
        System.out.println(jobStatusMessages);
    }
}
