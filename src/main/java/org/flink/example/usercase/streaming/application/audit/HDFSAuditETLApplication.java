package org.flink.example.usercase.streaming.application.audit;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer011;
import org.flink.example.common.constant.PropertiesConstants;
import org.flink.example.usercase.model.HDFSAuditEvent;
import org.flink.example.usercase.streaming.util.ExecutionEnvUtil;
import org.flink.example.usercase.streaming.util.GsonUtil;
import org.flink.example.usercase.streaming.util.KafkaConfigUtil;
import org.flink.example.usercase.streaming.util.LogEventsParseUtil;


public class HDFSAuditETLApplication {
    public static void main(String[] args) throws Exception {
        ParameterTool parameterTool = ExecutionEnvUtil.createParameterTool(args);
        StreamExecutionEnvironment env = ExecutionEnvUtil.prepare(parameterTool);
        DataStreamSource<String> source = KafkaConfigUtil.buildSource(env);
        source.map(auditLog -> {
            HDFSAuditEvent event = LogEventsParseUtil.parseHDFSAuditEvent(auditLog);
            return event;
        }).map(event -> GsonUtil.toJson(event))
        .addSink(new FlinkKafkaProducer011(parameterTool.getRequired("kafka.brokers"),
                parameterTool.getRequired(PropertiesConstants.KAFKA_SINK_TOPIC_KEY),  new SimpleStringSchema()));
        env.execute("HDFS Audit ETL Application");
    }


}
