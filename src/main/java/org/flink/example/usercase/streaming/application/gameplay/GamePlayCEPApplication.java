package org.flink.example.usercase.streaming.application.gameplay;

import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.SimpleCondition;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.flink.example.usercase.model.GamePlayEvent;
import org.flink.example.usercase.streaming.application.map.GamePlayMapFunction;
import org.flink.example.usercase.streaming.util.ExecutionEnvUtil;
import org.flink.example.usercase.streaming.util.KafkaConfigUtil;

public class GamePlayCEPApplication {
    public static void main(String[] args) throws Exception {
        ParameterTool parameterTool = ExecutionEnvUtil.createParameterTool(args);
        StreamExecutionEnvironment env = ExecutionEnvUtil.prepare(parameterTool);
        DataStreamSource<String> source = KafkaConfigUtil.buildSource(env);
        source.rebalance().map(new GamePlayMapFunction());

        Pattern<GamePlayEvent, GamePlayEvent> gamePlayEvent =
                Pattern.<GamePlayEvent>begin("start").where(new SimpleCondition<GamePlayEvent>() {
                    @Override
                    public boolean filter(GamePlayEvent gamePlayEvent) throws Exception {
                        return false;
                    }
                });
    }
}
