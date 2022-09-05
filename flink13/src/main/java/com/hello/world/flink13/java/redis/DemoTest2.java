package com.hello.world.flink13.java.redis;

import org.apache.flink.shaded.guava18.com.google.common.cache.Cache;
import org.apache.flink.shaded.guava18.com.google.common.cache.CacheBuilder;
import org.apache.flink.table.factories.Factory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.TimeUnit;

public class DemoTest2 {
    public static void main(String[] args) {
        ServiceLoader<Factory> factories = ServiceLoader.load(Factory.class);
        for (Factory factory : factories) {
            if ("redis".equals(factory.factoryIdentifier()))
                System.out.println(factory.factoryIdentifier());
        }

        Cache<String, String> cache = CacheBuilder.newBuilder()
                .expireAfterWrite(10, TimeUnit.SECONDS)
                .maximumSize(10)
                .build();

        cache.put("hello", "com");

        String result = cache.getIfPresent("hello");
        System.out.println(result);

        System.out.println(cache.getIfPresent("test"));

//        System.out.println(cache.getAllPresent());

        Map<String, String> srcFiles =
                new HashMap<String, String>() {
                    {
                        put("local1.txt", "Local text Content1");
                        put("local2.txt", "Local text Content2");
                    }
                };
        System.out.println(srcFiles);

        ArrayList<Integer> list = new ArrayList<Integer>() {{
            for (int i = 0; i < 10; i++) {
                add(i);
            }
        }};

        Map<Object, Object> map = new HashMap<Object, Object>(){
            {
                put("", "");
            }
        };
    }
}
