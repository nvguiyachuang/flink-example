package com.hello.world.kafka.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.errors.ProducerFencedException;

import java.util.Properties;

public class CustomProducer2 {
    public static void main(String[] args) {

        Properties props = new Properties();
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
//        props.put("client.id", "ProducerTranscationnalExample");
        props.put("bootstrap.servers", "node:9092");
//        props.put("transactional.id", "test-transactional");
//        props.put("acks", "all");
        KafkaProducer<String, String> producer = new KafkaProducer<>(props);
//        producer.initTransactions();

        try {
            String msg = "matt test";
//            producer.beginTransaction();
            producer.send(new ProducerRecord<>("flink_test_4", msg));
//            producer.commitTransaction();
        } catch (ProducerFencedException e1) {
            e1.printStackTrace();
            producer.close();
        } catch (KafkaException e2) {
            e2.printStackTrace();
            producer.abortTransaction();
        }
        producer.close();
    }
}
