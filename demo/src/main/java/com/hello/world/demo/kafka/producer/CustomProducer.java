package com.hello.world.demo.kafka.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class CustomProducer {
    public static void main(String[] args) {

        Properties prop = new Properties();
        prop.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "node:9092");
        prop.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        prop.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        prop.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        // 等待时间，默认1毫秒，与上一个条件共同控制是否发送一批数据
        prop.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        // 重试次数
        prop.put(ProducerConfig.RETRIES_CONFIG, 3);
        // 发送ack级别
        prop.put(ProducerConfig.ACKS_CONFIG, "all");
        // 开启事务
        prop.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "unique-id");

        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(prop);

        // 初始化，开启事务
        kafkaProducer.initTransactions();
        kafkaProducer.beginTransaction();

//        for (int i = 0; i < 10; i++) {
        try {
            kafkaProducer.send(new ProducerRecord<>("flink_test_4", "message-"));
        } catch (Exception e) {
            // abortTransaction
            kafkaProducer.abortTransaction();
        }

//            Future<RecordMetadata> result = kafkaProducer.send(
//                    // 发送数据，key确定分区
//                    new ProducerRecord<>("test2", "message-" + i),
//                    // callback，recordMetadata: 消息的一些信息， ex: 异常
//                    (recordMetadata, ex) -> {
//                        if (null == ex) {
//                            long offset = recordMetadata.offset();
//                            int partition = recordMetadata.partition();
//                            String topic = recordMetadata.topic();
//                            System.out.println("success" + topic + partition + offset);
//                        } else {
//                            System.out.println(ex.getMessage());
//                        }
//                    });

            // ********** 同步
            // RecordMetadata recordMetadata = result.get();
            // System.out.println(recordMetadata.offset());
//        }

        // 提交事务
        kafkaProducer.commitTransaction();

        kafkaProducer.close();
    }
}
