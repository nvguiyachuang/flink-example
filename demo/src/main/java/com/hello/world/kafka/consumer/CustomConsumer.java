package com.hello.world.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Collections;
import java.util.Properties;

/**
 * 自动提交offset
 */
public class CustomConsumer {
    public static void main(String[] args) {
        Properties prop = new Properties();
        prop.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "node1:9092");
        prop.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        prop.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        prop.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        // 消费者组id
        prop.put(ConsumerConfig.GROUP_ID_CONFIG, "group1");

        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(prop);
        // 订阅topic
        kafkaConsumer.subscribe(Collections.singletonList("test"));

        while (true) {
            ConsumerRecords<String, String> records = kafkaConsumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                System.out.println("topic:::" + record.topic());
                System.out.println("offset::" + record.offset());
                System.out.println("value:::" + record.value());
            }
        }

    }
}
