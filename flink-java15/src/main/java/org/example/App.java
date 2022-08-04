package org.example;

import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.state.StateTtlConfig;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.connector.kafka.source.reader.deserializer.KafkaRecordDeserializationSchema;
import org.apache.flink.runtime.state.hashmap.HashMapStateBackend;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.triggers.ContinuousEventTimeTrigger;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.example.util.CustomDeserialization;
import org.example.util.TimeUtil;

import java.lang.reflect.Field;
import java.time.Duration;

public class App {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

//        System.out.println(env.getStreamTimeCharacteristic());

        env.setParallelism(4);
        env.setBufferTimeout(100);
        env.enableCheckpointing(3000L);

        HashMapStateBackend backend = new HashMapStateBackend();
        env.setStateBackend(backend);

        Field field = Class.forName("org.apache.flink.streaming.api.environment.StreamExecutionEnvironment")
                .getDeclaredField("configuration");
        field.setAccessible(true);
        org.apache.flink.configuration.Configuration conf = (Configuration) field.get(env);
        conf.setString("rest.bind-port", "8080");

        KafkaSource<Record> kafkaSource = KafkaSource
                .<Record>builder()
                .setBootstrapServers("node1:19092")
                .setGroupId("test-group")
                .setTopics("test")
                .setStartingOffsets(OffsetsInitializer.earliest())
                .setDeserializer(KafkaRecordDeserializationSchema.of(new CustomDeserialization()))
//                .setDeserializer(KafkaRecordDeserializationSchema.valueOnly(new CustomDeserializationValueOnly()))
                .setProperty("partition.discovery.interval.ms", "10000")
                .build();

        WatermarkStrategy<Record> watermarkStrategy = WatermarkStrategy
                .<Record>forBoundedOutOfOrderness(Duration.ofSeconds(10))
                .withTimestampAssigner(new SerializableTimestampAssigner<Record>() {
                    @Override
                    public long extractTimestamp(Record element, long recordTimestamp) {
                        return element.getTimeStamp();
                    }
                })
                // 下游算子不需要等待这条数据流产生的Watermark，而取其他上游激活状态的Watermark，来决定是否需要触发窗口计算。
                // WatermarkStrategy.withIdleness(Duration.ofMillis(5))
                // 上面代码设置超时时间5毫秒，超过这个时间，没有生成Watermark，将流状态设置空闲，
                // 当下次有新的Watermark生成并发送到下游时，重新设置为活跃。
                .withIdleness(Duration.ofMillis(400));

        env
                .fromSource(kafkaSource, watermarkStrategy, "testSourceName")
//                .fromCollection(Arrays.asList("a", "c", "b"), TypeInformation.of(String.class))
//                .fromSequence(1L, 99999999999999999L)
                .keyBy(Record::getName)
                .window(TumblingEventTimeWindows.of(Time.seconds(5)))
                .trigger(ContinuousEventTimeTrigger.of(Time.seconds(2)))
                .process(new ProcessWindowFunction<Record, String, String, TimeWindow>() {

                    private ValueState<Integer> valueState;

                    @Override
                    public void open(Configuration parameters) throws Exception {
                        ValueStateDescriptor<Integer> stateDescriptor =
                                new ValueStateDescriptor<>("testState", Integer.class);

                        StateTtlConfig ttlConfig = StateTtlConfig
                                .newBuilder(org.apache.flink.api.common.time.Time.seconds(1))
                                .setUpdateType(StateTtlConfig.UpdateType.OnCreateAndWrite)
                                .setStateVisibility(StateTtlConfig.StateVisibility.NeverReturnExpired)
                                .build();

                        stateDescriptor.enableTimeToLive(ttlConfig);

                        valueState = getRuntimeContext().getState(stateDescriptor);
                    }

                    @Override
                    public void process(String key,
                                        ProcessWindowFunction<Record, String, String, TimeWindow>.Context context,
                                        Iterable<Record> elements,
                                        Collector<String> out) throws Exception {
//                        Integer value = valueState.value();
//                        if (null == value) value = 0;

                        Integer value = 0;

                        for (Record element : elements) {
                            value += element.getScore();
                        }
//                        valueState.update(value);
                        long start = context.window().getStart();
                        out.collect(TimeUtil.timestampToString(start) + "--key--" + key + "--" + value);
                    }
                })
                .print();

        env.execute();
    }
}
