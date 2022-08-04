package org.example.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.streaming.connectors.kafka.KafkaDeserializationSchema;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.example.Record;

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
            Record nameAndMajor = objectMapper.readValue(record.value(), Record.class);
            nameAndMajor.setTimeStamp(record.timestamp());
            return nameAndMajor;
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
