package com.hello.world.java.redis.sink;

import org.apache.flink.configuration.ReadableConfig;
import org.apache.flink.table.connector.ChangelogMode;
import org.apache.flink.table.connector.sink.DynamicTableSink;
import org.apache.flink.table.connector.sink.SinkFunctionProvider;
import org.apache.flink.types.RowKind;

import static com.hello.world.java.redis.options.RedisOptions.*;

public class RedisDynamicTableSink implements DynamicTableSink {
    private final ReadableConfig options;

    public RedisDynamicTableSink(ReadableConfig options) {
        this.options = options;
    }

    @Override
    public ChangelogMode getChangelogMode(ChangelogMode requestedMode) {
        return ChangelogMode
                .newBuilder()
                .addContainedKind(RowKind.INSERT)
                .addContainedKind(RowKind.UPDATE_BEFORE)
                .addContainedKind(RowKind.UPDATE_AFTER)
                .build();
    }

    @Override
    public SinkRuntimeProvider getSinkRuntimeProvider(Context context) {
        String host = options.get(SINGLE_HOST);
        Integer port = options.get(SINGLE_PORT);
        Integer expire = options.get(EXPIRE);
        String password = options.get(PASSWORD);
        MyRedisSink myRedisSink = new MyRedisSink(host, port, expire, password);
        return SinkFunctionProvider.of(myRedisSink);
    }

    @Override
    public DynamicTableSink copy() {
        return new RedisDynamicTableSink(this.options);
    }

    @Override
    public String asSummaryString() {
        return "xxx";
    }
}
