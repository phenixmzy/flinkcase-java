package org.flink.example.usercase.streaming.application.gameplay;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringEncoder;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.filesystem.StreamingFileSink;
import org.apache.flink.streaming.api.functions.sink.filesystem.bucketassigners.DateTimeBucketAssigner;
import org.flink.example.usercase.model.GamePlayEvent;
import org.flink.example.usercase.streaming.assigner.sink.JSONEventTimeBucketAssigner;
import org.flink.example.usercase.streaming.util.ExecutionEnvUtil;
import org.flink.example.usercase.streaming.util.GsonUtil;
import org.flink.example.usercase.streaming.util.KafkaConfigUtil;

import org.apache.flink.formats.parquet.avro.ParquetAvroWriters;

public class GamePlayDataCollectorParquetApplication {
    private final static String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static void main(String[] args) throws Exception {
        ParameterTool parameterTool = ExecutionEnvUtil.createParameterTool(args);
        StreamExecutionEnvironment env = ExecutionEnvUtil.prepare(parameterTool);

    final StreamingFileSink sink = StreamingFileSink
                .forBulkFormat(new Path(parameterTool.getRequired("hdfs.sink.path")), ParquetAvroWriters.forReflectRecord(GamePlayEvent.class))
                .withBucketAssigner(new DateTimeBucketAssigner("yyyyMMddHHmm"))
                .build();

        KafkaConfigUtil.buildSource(env)
        .map(new MapFunction<String, GamePlayEvent>() {
            @Override
            public GamePlayEvent map(String jsonStr) throws Exception {
                return GsonUtil.fromJson(jsonStr, GamePlayEvent.class);
            }
        })
      .addSink(sink);
        env.execute("GamePlay Data Collector Application");
    }
}
