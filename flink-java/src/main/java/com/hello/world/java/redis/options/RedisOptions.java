package com.hello.world.java.redis.options;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.flink.configuration.ConfigOption;
import org.apache.flink.configuration.ConfigOptions;
import redis.clients.jedis.Protocol;

public class RedisOptions {

    public static final ConfigOption<String> MODE = ConfigOptions
            .key("mode")
            .stringType()
            .defaultValue("single");
    public static final ConfigOption<String> SINGLE_HOST = ConfigOptions
            .key("host")
            .stringType()
            .defaultValue(Protocol.DEFAULT_HOST);
    public static final ConfigOption<Integer> SINGLE_PORT = ConfigOptions
            .key("port")
            .intType()
            .defaultValue(Protocol.DEFAULT_PORT);
    public static final ConfigOption<String> CLUSTER_NODES = ConfigOptions
            .key("cluster.nodes")
            .stringType()
            .noDefaultValue();
    public static final ConfigOption<String> SENTINEL_NODES = ConfigOptions
            .key("sentinel.nodes")
            .stringType()
            .noDefaultValue();
    public static final ConfigOption<String> SENTINEL_MASTER = ConfigOptions
            .key("sentinel.master")
            .stringType()
            .noDefaultValue();
    public static final ConfigOption<String> PASSWORD = ConfigOptions
            .key("password")
            .stringType()
            .noDefaultValue();

    public static final ConfigOption<Integer> EXPIRE = ConfigOptions
            .key("expire")
            .intType()
            .defaultValue(0);

    public static final ConfigOption<String> COMMAND = ConfigOptions
            .key("command")
            .stringType()
            .noDefaultValue();
    public static final ConfigOption<Integer> DB_NUM = ConfigOptions
            .key("db-num")
            .intType()
            .defaultValue(Protocol.DEFAULT_DATABASE);
    public static final ConfigOption<Integer> TTL_SEC = ConfigOptions
            .key("ttl-sec")
            .intType()
            .noDefaultValue();
    public static final ConfigOption<Integer> CONNECTION_TIMEOUT_MS = ConfigOptions
            .key("connection.timeout-ms")
            .intType()
            .defaultValue(Protocol.DEFAULT_TIMEOUT);
    public static final ConfigOption<Integer> CONNECTION_MAX_TOTAL = ConfigOptions
            .key("connection.max-total")
            .intType()
            .defaultValue(GenericObjectPoolConfig.DEFAULT_MAX_TOTAL);
    public static final ConfigOption<Integer> CONNECTION_MAX_IDLE = ConfigOptions
            .key("connection.max-idle")
            .intType()
            .defaultValue(GenericObjectPoolConfig.DEFAULT_MAX_IDLE);
    public static final ConfigOption<Boolean> CONNECTION_TEST_ON_BORROW = ConfigOptions
            .key("connection.test-on-borrow")
            .booleanType()
            .defaultValue(GenericObjectPoolConfig.DEFAULT_TEST_ON_BORROW);
    public static final ConfigOption<Boolean> CONNECTION_TEST_ON_RETURN = ConfigOptions
            .key("connection.test-on-return")
            .booleanType()
            .defaultValue(GenericObjectPoolConfig.DEFAULT_TEST_ON_RETURN);
    public static final ConfigOption<Boolean> CONNECTION_TEST_WHILE_IDLE = ConfigOptions
            .key("connection.test-while-idle")
            .booleanType()
            .defaultValue(GenericObjectPoolConfig.DEFAULT_TEST_WHILE_IDLE);
    public static final ConfigOption<String> LOOKUP_ADDITIONAL_KEY = ConfigOptions
            .key("lookup.additional-key")
            .stringType()
            .noDefaultValue();
    public static final ConfigOption<Integer> LOOKUP_CACHE_MAX_ROWS = ConfigOptions
            .key("lookup.cache.max-rows")
            .intType()
            .defaultValue(-1);
    public static final ConfigOption<Integer> LOOKUP_CACHE_TTL_SEC = ConfigOptions
            .key("lookup.cache.ttl-sec")
            .intType()
            .defaultValue(-1);

}
