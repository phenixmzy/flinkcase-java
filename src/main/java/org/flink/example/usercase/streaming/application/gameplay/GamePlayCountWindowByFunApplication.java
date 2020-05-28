package org.flink.example.usercase.streaming.application.gameplay;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.flink.example.common.constant.PropertiesConstants;
import org.flink.example.usercase.model.GamePlayEvent;
import org.flink.example.usercase.streaming.function.CountWindowAverage;
import org.flink.example.usercase.streaming.util.ExecutionEnvUtil;
import org.flink.example.usercase.streaming.util.GsonUtil;
import org.flink.example.usercase.streaming.util.KafkaConfigUtil;

public class GamePlayCountWindowByFunApplication {
    public static void main(String[] args) throws Exception {
        ParameterTool parameterTool = ExecutionEnvUtil.createParameterTool(args);
        StreamExecutionEnvironment env = ExecutionEnvUtil.prepare(parameterTool);
        DataStreamSource<String> source = KafkaConfigUtil.buildSource(env);
        source.map(gamePlayJson -> GsonUtil.fromJson(gamePlayJson, GamePlayEvent.class))
                .map(new MapFunction<GamePlayEvent, Tuple3<String,Integer, Integer>>() {
                    @Override
                    public Tuple3<String, Integer, Integer> map(GamePlayEvent value) throws Exception {
                        return Tuple3.of(value.getGameId(), 1, value.getTimeLen());
                    }
                })
                .keyBy(0)
                .flatMap(new CountWindowAverage())
                .map(item -> item.f0+"-"+item.f1+"-"+item.f2)
                .addSink(KafkaConfigUtil.buildSink(parameterTool))
        ;
        env.execute("Gameplay Count Window By Fun State");
    }
}
