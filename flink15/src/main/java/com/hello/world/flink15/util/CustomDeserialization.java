package com.hello.world.flink15.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hello.world.flink15.Record;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.streaming.connectors.kafka.KafkaDeserializationSchema;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.io.IOException;

public class CustomDeserialization implements KafkaDeserializationSchema<Record> {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void open(DeserializationSchema.InitializationContext context) throws Exception {
        KafkaDeserializationSchema.super.open(context);

        objectMapper = new ObjectMapper();
    }

    @Override
    public boolean isEndOfStream(Record nextElement) {
        return false;
    }

    @Override
    public Record deserialize(ConsumerRecord<byte[], byte[]> record) {
        try {
            Record readValue = objectMapper.readValue(record.value(), Record.class);
            Long timestamp = TimeUtil.stringToTimestamp(readValue.getTime());
            readValue.setTimeStamp(timestamp);
            return readValue;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public TypeInformation<Record> getProducedType() {
        return TypeInformation.of(Record.class);
    }
}
