package org.flink.example.usercase.streaming.application.gameplay;

import org.apache.flink.api.common.functions.JoinFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.JoinedStreams;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.flink.example.common.constant.PropertiesConstants;
import org.flink.example.usercase.model.GameBrowseEvent;
import org.flink.example.usercase.model.GamePlayEvent;
import org.flink.example.usercase.streaming.source.GameBrowseEventSource;
import org.flink.example.usercase.streaming.source.GamePlayEventSource;
import org.flink.example.usercase.streaming.util.ExecutionEnvUtil;
import org.flink.example.usercase.streaming.util.KafkaConfigUtil;

public class GamePlayJoinWindowApplication {

    private static DataStream<Tuple4<String, String, Integer, Integer>> getGamePlayStream(StreamExecutionEnvironment env, ParameterTool parameterTool) {
        int recordMaxNum = Integer.valueOf(parameterTool.getRequired("gameplay.record.max.num"));
        int gameIdMaxNum = Integer.valueOf(parameterTool.getRequired("gameplay.gid.max.num"));
        int userIdMaxNum = Integer.valueOf(parameterTool.getRequired("gameplay.uid.max.num"));
        int maxDelay = Integer.valueOf(parameterTool.getRequired("gameplay.delay.max.num"));
        int maxTimeLen = Integer.valueOf(parameterTool.getRequired("gameplay.timelen.max.num"));

        DataStream<Tuple4<String, String, Integer, Integer>> gamePlayStream  = env.addSource(new GamePlayEventSource(recordMaxNum, gameIdMaxNum, userIdMaxNum, maxDelay, maxTimeLen)).map(new MapFunction<GamePlayEvent, Tuple4<String, String, Integer, Integer>>() {

            @Override
            public Tuple4<String, String, Integer, Integer> map(GamePlayEvent gamePlayEvent) throws Exception {
                return Tuple4.of(gamePlayEvent.getGameId(), gamePlayEvent.getUserId(), gamePlayEvent.getStartTime(), 1);
            }
        }).assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor<Tuple4<String,String, Integer, Integer>>(Time.seconds(parameterTool.getLong(PropertiesConstants.FLINK_WINDOW_MAX_OUTOFORDERNESS_MS))) {
            @Override
            public long extractTimestamp(Tuple4<String, String, Integer, Integer> event) {
                return (long) (event.f2 * 1000L);
            }
        });

        return gamePlayStream;
    }

    private static DataStream<Tuple4<String, String, Integer, Integer>> getBrowseStream(StreamExecutionEnvironment env, ParameterTool parameterTool) {
        int recordMaxNum = Integer.valueOf(parameterTool.getRequired("browse.record.max.num"));
        int gameIdMaxNum = Integer.valueOf(parameterTool.getRequired("browse.gid.max.num"));
        int userIdMaxNum = Integer.valueOf(parameterTool.getRequired("browse.uid.max.num"));
        int maxDelay = Integer.valueOf(parameterTool.getRequired("browse.delay.max.num"));
        int maxTimeLen = Integer.valueOf(parameterTool.getRequired("browse.timelen.max.num"));

        DataStream<Tuple4<String, String, Integer, Integer>> gameBrowseStream  = env.addSource(new GameBrowseEventSource(recordMaxNum, gameIdMaxNum, userIdMaxNum, maxDelay, maxTimeLen)).map(new MapFunction<GameBrowseEvent, Tuple4<String, String, Integer, Integer>>() {

            @Override
            public Tuple4<String, String, Integer, Integer> map(GameBrowseEvent gameBrowseEvent) throws Exception {
                return Tuple4.of(gameBrowseEvent.getGameId(), gameBrowseEvent.getUserId(), gameBrowseEvent.getBrowseTime(), 1);
            }
        }).assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor<Tuple4<String,String, Integer, Integer>>(Time.seconds(parameterTool.getLong(PropertiesConstants.FLINK_WINDOW_MAX_OUTOFORDERNESS_MS))) {
            @Override
            public long extractTimestamp(Tuple4<String, String, Integer, Integer> event) {
                return (long) (event.f2 * 1000L);
            }
        });

        return gameBrowseStream;
    }

    private static class NameKeySelector implements KeySelector<Tuple4<String, String, Integer, Integer>, String> {
        @Override
        public String getKey(Tuple4<String, String, Integer, Integer> value) {
            return value.f0+"_"+value.f1;
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println("Hello GamePlayJoinWindow");
        ParameterTool parameterTool = ExecutionEnvUtil.createParameterTool(args);
        StreamExecutionEnvironment env = ExecutionEnvUtil.prepare(parameterTool);

        DataStream<Tuple4<String, String, Integer, Integer>> gamePlayStream =  getGamePlayStream(env, parameterTool);
        DataStream<Tuple4<String, String, Integer, Integer>> browseStream = getBrowseStream(env, parameterTool);
        JoinedStreams<Tuple4<String, String, Integer, Integer>, Tuple4<String, String, Integer, Integer>> joinStream = gamePlayStream.join(browseStream);
        DataStream<Tuple3<String, String, Integer>> joinWindow = joinStream
                .where(new NameKeySelector())
                .equalTo(new NameKeySelector())
                .window(TumblingEventTimeWindows.of(Time.seconds(Integer.valueOf(parameterTool.getRequired(PropertiesConstants.FLINK_WINDOW_SIZE)))))
                .apply(new JoinFunction<Tuple4<String,String, Integer, Integer>, Tuple4<String, String, Integer,Integer>, Tuple3<String, String, Integer>>() {
                    @Override
                    public Tuple3<String, String, Integer> join(Tuple4<String, String, Integer, Integer> jn1, Tuple4<String, String, Integer, Integer> jn2) throws Exception {
                        return Tuple3.of(jn1.f0, jn1.f1, jn1.f3);
                    }
                });
        joinWindow.keyBy(0).sum(2).map(item -> item.f0 + " " + item.f2)
                .addSink(KafkaConfigUtil.buildSink(parameterTool));
        env.execute("GamePlay Join GameBrower with Window");
    }
}
