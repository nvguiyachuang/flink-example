package com.hello.world.flink15.client;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.service.Service;
import org.apache.hadoop.yarn.api.records.ApplicationReport;
import org.apache.hadoop.yarn.client.api.YarnClient;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.apache.hadoop.yarn.exceptions.YarnException;

import java.io.IOException;
import java.net.URI;
import java.util.List;

public class YarnClientDemo {
    public static void main(String[] args) throws IOException, YarnException {
        String path = "/Users/xzh/Documents/yarn-conf/";

        YarnConfiguration yarnConfiguration = new YarnConfiguration();
        yarnConfiguration.addResource(new Path(URI.create(path + "yarn-site.xml")));
        yarnConfiguration.addResource(new Path(URI.create(path + "core-site.xml")));
        yarnConfiguration.addResource(new Path(URI.create(path + "hdfs-site.xml")));
        YarnClient yarnClient = YarnClient.createYarnClient();
        yarnClient.init(yarnConfiguration);
        yarnClient.start();

        // test yarn client
        if (yarnClient.isInState(Service.STATE.STARTED)) {
            System.out.println("test yarn client successful");

            List<ApplicationReport> applications = yarnClient.getApplications();
            System.out.println(applications);
        }
    }
}
