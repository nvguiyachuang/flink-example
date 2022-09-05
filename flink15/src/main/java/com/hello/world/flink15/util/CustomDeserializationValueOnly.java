package com.hello.world.flink15.util;

import com.hello.world.flink15.Record;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;

import java.io.IOException;

public class CustomDeserializationValueOnly implements DeserializationSchema<Record> {

    @Override
    public Record deserialize(byte[] message) throws IOException {
        return null;
    }

    @Override
    public boolean isEndOfStream(Record nextElement) {
        return false;
    }

    @Override
    public TypeInformation<Record> getProducedType() {
        return null;
    }
}
