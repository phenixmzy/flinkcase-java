package org.flink.example.usercase.streaming.application.audit;

import java.util.concurrent.TimeUnit;

import org.apache.flink.api.common.time.Time;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.flink.example.usercase.model.HDFSAuditEvent;
import org.flink.example.usercase.streaming.assigner.HDFSAuditEventAssignerWithPeriodicWatermarks;
import org.flink.example.usercase.streaming.util.ExecutionEnvUtil;
import org.flink.example.usercase.streaming.util.GsonUtil;
import org.flink.example.usercase.streaming.util.KafkaConfigUtil;

import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.SECONDS;

public class HDFSAuditOperationWindowApplication {
    public static void main(String[] args) throws Exception {
        ParameterTool parameterTool = ExecutionEnvUtil.createParameterTool(args);
        StreamExecutionEnvironment env = ExecutionEnvUtil.prepare(parameterTool);
        DataStreamSource<String> source = KafkaConfigUtil.buildSource(env);
        /*source.map(hdfsAuditJson ->{
            HDFSAuditEvent event = GsonUtil.fromJson(hdfsAuditJson, HDFSAuditEvent.class);
            return event;
        }).assignTimestampsAndWatermarks(new HDFSAuditEventAssignerWithPeriodicWatermarks(parameterTool.getLong("window.max.outoforderness")))
        .keyBy(0)
        .timeWindow(Time.of(parameterTool.getLong("window.size"),SECONDS), Time.of(parameterTool.getLong("window.size"), SECONDS))
        ;*/
    }
}
