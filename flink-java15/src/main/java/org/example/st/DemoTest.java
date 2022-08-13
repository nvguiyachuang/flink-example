package org.example.st;

import org.apache.flink.streaming.api.environment.LocalStreamEnvironment;

public class DemoTest {

    private static LocalStreamEnvironment localStreamEnvironment ;

    public static void main(String[] args) {
        localStreamEnvironment = new LocalStreamEnvironment(null);

    }
}
