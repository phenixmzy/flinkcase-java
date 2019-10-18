package org.flink.example.usercase.streaming.application.gameplay;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.tuple.Tuple5;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer011;
import org.flink.example.common.constant.PropertiesConstants;
import org.flink.example.usercase.model.GamePlayEvent;
import org.flink.example.usercase.streaming.util.ExecutionEnvUtil;
import org.flink.example.usercase.streaming.util.GsonUtil;
import org.flink.example.usercase.streaming.util.KafkaConfigUtil;

import static java.util.concurrent.TimeUnit.SECONDS;

public class GamePlayWindowApplication {
    public static void main(String[] args) throws Exception {
        ParameterTool parameterTool = ExecutionEnvUtil.createParameterTool(args);
        StreamExecutionEnvironment env = ExecutionEnvUtil.prepare(parameterTool);
        DataStreamSource<String> source = KafkaConfigUtil.buildSource(env);
        source.map(new MapFunction<String, Tuple3<String,Integer,Integer>>() {
                       @Override
                       public Tuple3<String,Integer,Integer> map(String gamePlayJson) throws Exception {
                           GamePlayEvent gamePlayEvent = GsonUtil.fromJson(gamePlayJson, GamePlayEvent.class);
                           return Tuple3.of(gamePlayEvent.getGameId(), gamePlayEvent.getLeaveTime() ,1);
                       }
                   }
        ).assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor<Tuple3<String,Integer,Integer>>(Time.seconds(parameterTool.getLong(PropertiesConstants.FLINK_WINDOW_MAX_OUTOFORDERNESS))) {
            @Override
            public long extractTimestamp(Tuple3<String,Integer,Integer> event) {
                return (long)(event.f1 * 1000L);
            }
        })
                .keyBy(0)
                .timeWindow(Time.of(parameterTool.getLong(PropertiesConstants.FLINK_WINDOW_SIZE),SECONDS))
        .sum(2).map(item -> {
            StringBuilder builder = new StringBuilder();
            builder.append(item.f0).append(" ").append(item.f2);
            return builder.toString();
        })
        .addSink(new FlinkKafkaProducer011(parameterTool.getRequired(PropertiesConstants.KAFKA_BROKERS_KEY),
                parameterTool.getRequired(PropertiesConstants.KAFKA_SINK_TOPIC_KEY),
                new SimpleStringSchema()));
        env.execute("Window GamePlayCount ");
    }
}
