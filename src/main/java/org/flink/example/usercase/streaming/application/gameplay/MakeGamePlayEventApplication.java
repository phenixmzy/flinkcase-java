package org.flink.example.usercase.streaming.application.gameplay;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.flink.example.common.constant.PropertiesConstants;
import org.flink.example.usercase.model.GamePlayEvent;
import org.flink.example.usercase.streaming.application.map.GamePlayMapFunction;
import org.flink.example.usercase.streaming.source.GamePlayEventSource;
import org.flink.example.usercase.streaming.util.ExecutionEnvUtil;
import org.flink.example.usercase.streaming.util.GsonUtil;
import org.flink.example.usercase.streaming.util.KafkaConfigUtil;

public class MakeGamePlayEventApplication {

    private static DataStream<GamePlayEvent> getGamePlayEventStream(StreamExecutionEnvironment env, ParameterTool parameterTool) {
        int recordMaxNum = Integer.valueOf(parameterTool.getRequired("gameplay.record.max.num"));
        int gameIdMaxNum = Integer.valueOf(parameterTool.getRequired("gameplay.gid.max.num"));
        int userIdMaxNum = Integer.valueOf(parameterTool.getRequired("gameplay.uid.max.num"));
        int maxDelay = Integer.valueOf(parameterTool.getRequired("gameplay.delay.max.num"));
        int maxTimeLen = Integer.valueOf(parameterTool.getRequired("gameplay.timelen.max.num"));

        DataStream<GamePlayEvent> gamePlayStream  = env.addSource(new GamePlayEventSource(recordMaxNum, gameIdMaxNum, userIdMaxNum, maxDelay, maxTimeLen));

        return gamePlayStream;
    }

    public static void main(String[] args) throws Exception {
        final ParameterTool parameterTool = ExecutionEnvUtil.createParameterTool(args);
        StreamExecutionEnvironment env = ExecutionEnvUtil.prepare(parameterTool);
        getGamePlayEventStream(env,parameterTool)
                .map(gamePlay -> {
                    return GsonUtil.toJson(gamePlay);
                })
                .addSink(KafkaConfigUtil.buildSink(parameterTool));
        env.execute("Make GamePlay Event Test Data");
    }
}
