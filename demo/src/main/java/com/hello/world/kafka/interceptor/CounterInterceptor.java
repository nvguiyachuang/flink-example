package com.hello.world.kafka.interceptor;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Map;

public class CounterInterceptor implements ProducerInterceptor<String, String> {

    private long successNum = 0L;
    private long errorNum = 0L;

    /**
     * 发送到kafka之前
     */
    @Override
    public ProducerRecord<String, String> onSend(ProducerRecord<String, String> producerRecord) {
        return producerRecord;
    }

    /**
     * 生产者确认后
     */
    @Override
    public void onAcknowledgement(RecordMetadata recordMetadata, Exception e) {
        if (null == e) {
            successNum++;
        } else {
            errorNum++;
        }
    }

    /**
     * This is called when interceptor is closed
     */
    @Override
    public void close() {
        System.out.println("success:::" + successNum);
        System.out.println("error:::" + errorNum);
    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}
