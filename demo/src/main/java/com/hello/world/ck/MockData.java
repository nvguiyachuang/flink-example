package com.hello.world.ck;

import java.math.BigDecimal;
import java.util.Random;

public class MockData {

    public static final String[] clusterIdArr =
            {"387-b43a84f69367c2d9dc89616c63c8db81", "387-b43a84f69367c2d9dc89616c63c8db82"};

    public static final String[] instanceIdArr =
            {"b43a84f69367c2d9dc89616c63c8db81", "b43a84f69367c2d9dc89616c63c8db82"};

    public static final String[] metricTypeArr = {"sum", "sum", "sum", "sum", "sum", "sum", "avg"};

    public static final String[] nameArr = {"Sink: Kafka宿.numRecordsOut", "Source: Kafka源.numRecordsOut",
            "Sink: Kafka宿.numRecordsOutPerSecond", "Source: Kafka源.numRecordsOutPerSecond"};

    public static final Integer[] partitionArr = {0, 1, 2, 3, 4, 5, 6, 7};

    public static <T> T getValueByArr(T[] arr) {
        return arr[new Random(new Random().nextInt()).nextInt(arr.length)];
    }

    public static void main(String[] args) throws InterruptedException {
        while (true) {
//            String valueByArr = getValueByArr(clusterIdArr);
//            System.out.println(valueByArr);

            System.out.println(getValueByArr(partitionArr));

            int v = new Random().nextInt(20);
            BigDecimal bigDecimal = BigDecimal.valueOf(v);
            System.out.println(bigDecimal);
        }
    }
}
