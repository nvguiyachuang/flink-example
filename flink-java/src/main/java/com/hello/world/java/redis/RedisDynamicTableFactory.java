package com.hello.world.java.redis;

import com.hello.world.java.redis.sink.RedisDynamicTableSink;
import org.apache.flink.configuration.ConfigOption;
import org.apache.flink.configuration.ReadableConfig;
import org.apache.flink.table.connector.sink.DynamicTableSink;
import org.apache.flink.table.connector.source.DynamicTableSource;
import org.apache.flink.table.factories.DynamicTableSinkFactory;
import org.apache.flink.table.factories.DynamicTableSourceFactory;
import org.apache.flink.table.factories.FactoryUtil;

import java.util.HashSet;
import java.util.Set;

import static com.hello.world.java.redis.options.RedisOptions.*;

public class RedisDynamicTableFactory implements DynamicTableSinkFactory, DynamicTableSourceFactory {

    @Override
    public String factoryIdentifier() {
        return "redis";
    }

    @Override
    public Set<ConfigOption<?>> requiredOptions() {
        final Set<ConfigOption<?>> options = new HashSet<>();
        options.add(SINGLE_HOST);
        options.add(SINGLE_PORT);
        return options;
    }

    @Override
    public Set<ConfigOption<?>> optionalOptions() {
        final Set<ConfigOption<?>> options = new HashSet<>();
//        options.add(SINGLE_HOST);
//        options.add(SINGLE_PORT);
//        options.add(LOOKUP_CACHE_TTL_SEC);
//        options.add(DB_NUM);
        options.add(PASSWORD);
        options.add(EXPIRE);
        return options;
    }

    @Override
    public DynamicTableSource createDynamicTableSource(Context context) {
        return null;
    }

    @Override
    public DynamicTableSink createDynamicTableSink(Context context) {
        final FactoryUtil.TableFactoryHelper helper = FactoryUtil.createTableFactoryHelper(this, context);
        helper.validate();
        ReadableConfig options = helper.getOptions();
        return new RedisDynamicTableSink(options);
    }
}
