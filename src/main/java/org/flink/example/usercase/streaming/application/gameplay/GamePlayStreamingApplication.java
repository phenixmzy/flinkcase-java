package org.flink.example.usercase.streaming.application.gameplay;

import org.apache.flink.api.common.state.StateTtlConfig;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;
import org.flink.example.usercase.streaming.application.map.GamePlayMapFunction;

import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import org.flink.example.usercase.streaming.util.ExecutionEnvUtil;
import org.flink.example.usercase.streaming.util.KafkaConfigUtil;

public class GamePlayStreamingApplication {
    public static void main(String[] args) throws Exception {
        final ParameterTool parameterTool = ExecutionEnvUtil.createParameterTool(args);
        StreamExecutionEnvironment env = ExecutionEnvUtil.prepare(parameterTool);
        DataStreamSource<String> source = KafkaConfigUtil.buildSource(env);
        source.rebalance().map(new GamePlayMapFunction())
                .map(gamePlay -> {
                    String gameId = gamePlay.getGameId();
                    return gameId;
                }).keyBy(new KeySelector<String, Object>() {

            @Override
            public Object getKey(String s) throws Exception {
                return s;
            }
        })
                .addSink(KafkaConfigUtil.buildSink(parameterTool));
        env.execute("Streaming GamePlay Kafka to Kafka");

    }
}
