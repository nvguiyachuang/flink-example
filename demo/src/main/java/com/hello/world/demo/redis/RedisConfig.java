//package com.hello.world.demo.redis;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.connection.RedisPassword;
//import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
//import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
//import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//import redis.clients.jedis.JedisPoolConfig;
//
//@Configuration
//public class RedisConfig {
//
//    @Value("${spring.redis.host}")
//    private String redisHost;
//
//    @Value("${spring.redis.port}")
//    private String redisPort;
//
//    @Value("${spring.redis.database}")
//    private String redisDb;
//
//    @Value("${spring.redis.password}")
//    private String redisPass;
//
//    @Bean(name = "jedisPoolConfig")
//    public JedisPoolConfig jedisPoolConfig() {
//        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
//        //控制一个pool可分配多少个jedis实例
//        jedisPoolConfig.setMaxTotal(500);
//        //最大空闲数
//        jedisPoolConfig.setMaxIdle(200);
//        //每次释放连接的最大数目，默认是3
//        jedisPoolConfig.setNumTestsPerEvictionRun(1024);
//        //逐出扫描的时间间隔(毫秒) 如果为负数,则不运行逐出线程, 默认-1
//        jedisPoolConfig.setTimeBetweenEvictionRunsMillis(30000);
//        //连接的最小空闲时间 默认1800000毫秒(30分钟)
//        jedisPoolConfig.setMinEvictableIdleTimeMillis(-1);
//        jedisPoolConfig.setSoftMinEvictableIdleTimeMillis(10000);
//        //最大建立连接等待时间。如果超过此时间将接到异常。设为-1表示无限制。
//        jedisPoolConfig.setMaxWaitMillis(1500);
//        jedisPoolConfig.setTestOnBorrow(true);
//        jedisPoolConfig.setTestWhileIdle(true);
//        jedisPoolConfig.setTestOnReturn(false);
//        jedisPoolConfig.setJmxEnabled(true);
//        jedisPoolConfig.setBlockWhenExhausted(false);
//        return jedisPoolConfig;
//    }
//
//    @Bean("connectionFactory")
//    public JedisConnectionFactory connectionFactory() {
//        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
//        redisStandaloneConfiguration.setHostName(redisHost);
//        redisStandaloneConfiguration.setDatabase(Integer.parseInt(redisDb));
//        redisStandaloneConfiguration.setPassword(RedisPassword.of(redisPass));
//        redisStandaloneConfiguration.setPort(Integer.parseInt(redisPort));
//        //获得默认的连接池构造器
//        JedisClientConfiguration.JedisPoolingClientConfigurationBuilder jpcb =
//                (JedisClientConfiguration.JedisPoolingClientConfigurationBuilder) JedisClientConfiguration.builder();
//        //指定jedisPoolConifig来修改默认的连接池构造器（真麻烦，滥用设计模式！）
//        jpcb.poolConfig(jedisPoolConfig());
//        //通过构造器来构造jedis客户端配置
//        JedisClientConfiguration jedisClientConfiguration = jpcb.build();
//        //单机配置 + 客户端配置 = jedis连接工厂
//        return new JedisConnectionFactory(redisStandaloneConfiguration, jedisClientConfiguration);
//    }
//
//    @Bean
//    public RedisTemplate<String, Object> redisTemplate(JedisConnectionFactory connectionFactory) {
//        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
//        redisTemplate.setConnectionFactory(connectionFactory);
//
//        GenericJackson2JsonRedisSerializer jackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
//        StringRedisSerializer serializer = new StringRedisSerializer();
//        redisTemplate.setKeySerializer(serializer);
//        redisTemplate.setHashKeySerializer(serializer);
//        redisTemplate.setValueSerializer(serializer);
//        redisTemplate.setHashValueSerializer(serializer);
//
//        //初始化参数和初始化工作
//        redisTemplate.afterPropertiesSet();
//        return redisTemplate;
//    }
//
//}
