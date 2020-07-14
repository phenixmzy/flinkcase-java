package org.flink.example.usercase.streaming.application.gameplay;

import org.apache.flink.api.common.serialization.SimpleStringEncoder;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.filesystem.StreamingFileSink;
import org.apache.flink.core.fs.Path;
import org.flink.example.usercase.streaming.assigner.sink.JSONEventTimeBucketAssigner;
import org.flink.example.usercase.streaming.util.ExecutionEnvUtil;
import org.flink.example.usercase.streaming.util.KafkaConfigUtil;

public class GamePlayDataCollectorApplication {
    private final static String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static void main(String[] args) throws Exception {
        ParameterTool parameterTool = ExecutionEnvUtil.createParameterTool(args);
        StreamExecutionEnvironment env = ExecutionEnvUtil.prepare(parameterTool);

        final StreamingFileSink sink = StreamingFileSink
                .forRowFormat(new Path(parameterTool.getRequired("hdfs.sink.path")), new SimpleStringEncoder<>("UTF-8"))
                .withBucketAssigner(new JSONEventTimeBucketAssigner("startTime","yyyyMMddHHmm", false))
                .build();

        KafkaConfigUtil.buildSource(env).rebalance().addSink(sink);
        env.execute("GamePlay Data Collector Application");
    }
}
