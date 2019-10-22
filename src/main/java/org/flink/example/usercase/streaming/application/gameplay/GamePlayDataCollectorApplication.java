package org.flink.example.usercase.streaming.application.gameplay;

import org.apache.flink.api.common.serialization.SimpleStringEncoder;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.filesystem.StreamingFileSink;
import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.functions.sink.filesystem.bucketassigners.DateTimeBucketAssigner;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer011;
import org.flink.example.common.constant.PropertiesConstants;
import org.flink.example.usercase.model.GamePlayEvent;
import org.flink.example.usercase.streaming.util.DateTimeUtil;
import org.flink.example.usercase.streaming.util.ExecutionEnvUtil;
import org.flink.example.usercase.streaming.util.GsonUtil;
import org.flink.example.usercase.streaming.util.KafkaConfigUtil;
import com.alibaba.fastjson.JSONObject;

public class GamePlayDataCollectorApplication {
    private final static String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static void main(String[] args) throws Exception {
        ParameterTool parameterTool = ExecutionEnvUtil.createParameterTool(args);
        StreamExecutionEnvironment env = ExecutionEnvUtil.prepare(parameterTool);

        final StreamingFileSink sink = StreamingFileSink
                .forRowFormat(new Path(parameterTool.getRequired("hdfs.sink.path")), new SimpleStringEncoder<>("UTF-8"))
                .withBucketAssigner(new DateTimeBucketAssigner("yyyyMMddHHmm"))
                .build();

        DataStreamSource<String> source = KafkaConfigUtil.buildSource(env);
        source
        .map(gamePlayJson -> {
            StringBuilder builder = new StringBuilder();
            GamePlayEvent event = GsonUtil.fromJson(gamePlayJson, GamePlayEvent.class);
            builder.append(gamePlayJson)
                    .append("[").append(DateTimeUtil.getTimeStampStr(event.getStartTime() * 1000L, TIME_FORMAT)).append("]")
                    .append("[").append(DateTimeUtil.getTimeStampStr(event.getLeaveTime() * 1000L, TIME_FORMAT)).append("]");
            return builder.toString();
        })
                .addSink(sink);
     /*   .addSink(new FlinkKafkaProducer011(parameterTool.getRequired("kafka.brokers"),
                parameterTool.getRequired(PropertiesConstants.KAFKA_SINK_TOPIC_KEY),
                new SimpleStringSchema()));*/
        // .addSink(sink);
        env.execute("GamePlay Data Collector Application");
    }
}
