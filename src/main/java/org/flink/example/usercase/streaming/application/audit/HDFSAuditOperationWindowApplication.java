package org.flink.example.usercase.streaming.application.audit;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer011;
import org.flink.example.common.constant.PropertiesConstants;
import org.flink.example.usercase.model.HDFSAuditEvent;
import org.flink.example.usercase.streaming.util.ExecutionEnvUtil;
import org.flink.example.usercase.streaming.util.GsonUtil;
import org.flink.example.usercase.streaming.util.KafkaConfigUtil;

import static java.util.concurrent.TimeUnit.SECONDS;

public class HDFSAuditOperationWindowApplication {
    public static void main(String[] args) throws Exception {
        ParameterTool parameterTool = ExecutionEnvUtil.createParameterTool(args);
        StreamExecutionEnvironment env = ExecutionEnvUtil.prepare(parameterTool);
        DataStreamSource<String> source = KafkaConfigUtil.buildSource(env);
        source.map(new MapFunction<String, Tuple3<String, Long, Integer>>() {

            @Override
            public Tuple3<String, Long, Integer> map(String s) throws Exception {
                HDFSAuditEvent event = GsonUtil.fromJson(s, HDFSAuditEvent.class);
                return Tuple3.of(event.getCmd(), event.getTimeStampMS(),1);
            }
    }).assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor<Tuple3<String,Long,Integer>>(Time.seconds(parameterTool.getLong(PropertiesConstants.FLINK_WINDOW_MAX_OUTOFORDERNESS_MS))) {
            @Override
            public long extractTimestamp(Tuple3<String,Long,Integer> event) {
                return event.f1;
            }
        })
        .keyBy(0)
        .timeWindowAll(Time.of(parameterTool.getLong(PropertiesConstants.FLINK_WINDOW_SIZE),SECONDS),
                Time.of(parameterTool.getLong(PropertiesConstants.FLINK_WINDOW_SLIDE),SECONDS))
                .sum(0).map(item -> item.f0 + " " + item.f2)
                .addSink(KafkaConfigUtil.buildSink(parameterTool));

        env.execute("Window HDFS Audit Operation Count");
    }
}
