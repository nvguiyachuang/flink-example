package com.hello.world.kafka.consumer;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Collection;
import java.util.Collections;
import java.util.Properties;

/**
 * 自定义提交offset
 */
public class CustomConsumer3 {
    public static void main(String[] args) {
        Properties prop = new Properties();
        prop.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "mac.local:9092");
        prop.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        prop.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        prop.put(ConsumerConfig.GROUP_ID_CONFIG, "group1");

        // 设置手动提交 offset
        prop.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");

        prop.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(prop);
        kafkaConsumer.subscribe(Collections.singletonList("test2"),

                new ConsumerRebalanceListener() {
                    @Override
                    public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
                        // 分区分配之前, 提交旧的offset
                        commit();
                    }

                    @Override
                    public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
                        // 分区分配之后, 读取新的offset
                        readOffset();
                        for (TopicPartition partition : partitions) {
                            System.out.println(partition.topic());
                        }
                    }
                });

        while (true) {
            ConsumerRecords<String, String> records = kafkaConsumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                System.out.println("topic2:::" + record.topic());
                System.out.println("offset2::" + record.offset());
                System.out.println("value2:::" + record.value());
            }

            // 手动异步提交 offset
            kafkaConsumer.commitAsync();
        }

    }

    private static void readOffset() {

    }

    private static void commit() {

    }
}
