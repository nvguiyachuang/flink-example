package org.example.st;

import org.apache.flink.streaming.api.environment.LocalStreamEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.TableEnvironment;

public class Executor {

    public LocalStreamEnvironment localStreamEnvironment;

    public TableEnvironment tEnv;

    public Executor() {
        EnvironmentSettings settings = EnvironmentSettings
                .newInstance()
                .inStreamingMode()
                .build();

        tEnv = TableEnvironment.create(settings);
    }
}
