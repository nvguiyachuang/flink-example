package com.hello.world.flink13.java.redis.sink;
 
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.apache.flink.table.data.RowData;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

import java.util.LinkedList;
import java.util.List;

public class MyRedisSink extends RichSinkFunction<RowData> {
 
    private final String host;
    private final int port;
    private final int expire;
    private final String password;

    private ShardedJedisPool pool;

    private Jedis jedis;

    public MyRedisSink(String host, int port, int expire, String password) {
        this.host = host;
        this.port = port;
        this.expire = expire;
        this.password = password;
    }
 
    @Override
    public void open(Configuration parameters) throws Exception {
//        this.jedis = new Jedis(host, port);

        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(100);
        config.setMaxIdle(50);
        config.setMaxWaitMillis(3000);
        config.setTestOnBorrow(true);
        config.setTestOnReturn(true);
        JedisShardInfo jedisShardInfo1 = new JedisShardInfo(this.host, this.port);
        jedisShardInfo1.setPassword(this.password);
        List<JedisShardInfo> list = new LinkedList<>();
        list.add(jedisShardInfo1);
        pool = new ShardedJedisPool(config, list);
        boolean closed = pool.isClosed();
        System.out.println(closed);
    }
 
    @Override
    public void invoke(RowData value, Context context) throws Exception {
        String key = String.valueOf(value.getString(0));
        String sinkVal = String.valueOf(value.getInt(1));
        String result = this.pool.getResource().set(key, sinkVal, "NX", "EX", expire);
        System.out.println(result);
    }
 
    @Override
    public void close() throws Exception {
//        this.jedis.close();
    }
}