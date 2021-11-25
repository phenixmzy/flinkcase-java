package org.flink.example.usercase.streaming.application.gameplay;

import akka.remote.WireFormats;
import org.apache.flink.api.common.serialization.SimpleStringEncoder;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.formats.parquet.protobuf.ParquetProtoWriters;
import org.apache.flink.streaming.api.functions.sink.filesystem.StreamingFileSink;
import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.filesystem.rollingpolicies.DefaultRollingPolicy;
import org.apache.flink.util.TimeUtils;
import org.flink.example.usercase.model.GamePlayEvent;
import org.flink.example.usercase.streaming.assigner.sink.JSONEventTimeBucketAssigner;
import org.flink.example.usercase.streaming.util.ExecutionEnvUtil;
import org.flink.example.usercase.streaming.util.GsonUtil;
import org.flink.example.usercase.streaming.util.KafkaConfigUtil;

import java.util.concurrent.TimeUnit;


public class GamePlayEventWriteHDFSApplication {
    public static void main(String[] args) throws Exception {
        ParameterTool parameterTool = ExecutionEnvUtil.createParameterTool(args);
        StreamExecutionEnvironment env = ExecutionEnvUtil.prepare(parameterTool);
        DataStream<String> ds = KafkaConfigUtil.buildSource(env);
        String enCoderFormat = parameterTool.getRequired("encoder.format");
        if ("row.encoder.format".equals(enCoderFormat)) {
            StreamingFileSink streamingFileSink = getRowSink(parameterTool.getRequired("gameplay.output.path"));
            ds.addSink(streamingFileSink);
        } /*else if ("batch.encoder.format".equals(enCoderFormat)) {
            StreamingFileSink streamingFileSink = getBatchSink(parameterTool.getRequired("gameplay.output.path"));
            ds.map(gamePlayJson -> {
                GamePlayEvent gamePlayEvent = GsonUtil.fromJson(gamePlayJson, GamePlayEvent.class);
                return gamePlayEvent;
            }).addSink(streamingFileSink);
        }*/
        env.execute("GameEvent Write to HDFS");
    }

    public static StreamingFileSink getRowSink(String outputPath) throws Exception {
        final StreamingFileSink<String> sink = StreamingFileSink
                .forRowFormat(new Path(outputPath), new SimpleStringEncoder<String>("UTF-8"))
                .withRollingPolicy(
                        DefaultRollingPolicy.builder()
                                .withRolloverInterval(TimeUnit.MINUTES.toMillis(15))
                                .withInactivityInterval(TimeUnit.MINUTES.toMillis(5))
                                .withMaxPartSize(1024 * 1024 * 1024)
                                .build())
                .withBucketAssigner(new JSONEventTimeBucketAssigner<String>("startTime", "yyyyMMddHH", false))
                .build();
        return sink;
    }

    /*public static StreamingFileSink<GamePlayEvent> getBatchSink(String outputPath) throws Exception {
        final StreamingFileSink<GamePlayEvent> sink = StreamingFileSink
                .forBulkFormat(outputPath, ParquetProtoWriters.forType(GamePlayEvent.class))
                .build();
        return sink;
    }*/
}
