package com.hello.world.flink15.util;

import java.util.Map;

public class SystemEnvDemo {
    public static void main(String[] args) {
        System.out.println(System.getenv("hello"));
        System.out.println(System.getenv("hello2"));

        Map<String, String> getenv = System.getenv();
        for (Map.Entry<String, String> entry : getenv.entrySet()) {
            System.out.println(entry.getKey() + "---" + entry.getValue());
        }
    }
}
