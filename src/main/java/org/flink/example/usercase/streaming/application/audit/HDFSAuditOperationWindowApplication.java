package org.flink.example.usercase.streaming.application.audit;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer011;
import org.flink.example.common.constant.PropertiesConstants;
import org.flink.example.usercase.model.HDFSAuditEvent;
import org.flink.example.usercase.streaming.assigner.HDFSAuditEventAssignerWithPeriodicWatermarks;
import org.flink.example.usercase.streaming.util.ExecutionEnvUtil;
import org.flink.example.usercase.streaming.util.GsonUtil;
import org.flink.example.usercase.streaming.util.KafkaConfigUtil;

import static java.util.concurrent.TimeUnit.SECONDS;

public class HDFSAuditOperationWindowApplication {
    public static void main(String[] args) throws Exception {
        ParameterTool parameterTool = ExecutionEnvUtil.createParameterTool(args);
        StreamExecutionEnvironment env = ExecutionEnvUtil.prepare(parameterTool);
        DataStreamSource<String> source = KafkaConfigUtil.buildSource(env);
        source.map(hdfsAuditJson ->{
            HDFSAuditEvent event = GsonUtil.fromJson(hdfsAuditJson, HDFSAuditEvent.class);
            return event;
        }).assignTimestampsAndWatermarks(new HDFSAuditEventAssignerWithPeriodicWatermarks(parameterTool.getLong("window.max.outoforderness")))
        .keyBy(0).map(item -> Tuple2.of(item.getCmd(), 1))
        .timeWindowAll(Time.of(parameterTool.getLong("window.size"),SECONDS),
                Time.of(parameterTool.getLong("window.size"), SECONDS))
                .sum(0)
                .addSink(new FlinkKafkaProducer011(parameterTool.getRequired("kafka.brokers"),
                        parameterTool.getRequired(PropertiesConstants.KAFKA_SINK_TOPIC_KEY),
                        new SimpleStringSchema()));

        env.execute("Window HDFS Audit Operation Count");
    }
}
