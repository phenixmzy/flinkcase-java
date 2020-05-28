package org.flink.example.usercase.streaming.application.gameplay;

import org.flink.example.usercase.streaming.application.map.GamePlayMapFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;

import org.flink.example.common.constant.PropertiesConstants;
import org.flink.example.usercase.model.GamePlayEvent;
import org.flink.example.usercase.streaming.util.ExecutionEnvUtil;
import org.flink.example.usercase.streaming.util.KafkaConfigUtil;
import org.flink.example.usercase.streaming.util.GsonUtil;

public class GamePlayStreamingApplication {
    //private static Logger logger = LoggerFactory.getLogger(GamePlayStreamingApplication.class);

    public static void main(String[] args) throws Exception{
        final ParameterTool parameterTool = ExecutionEnvUtil.createParameterTool(args);
        StreamExecutionEnvironment env = ExecutionEnvUtil.prepare(parameterTool);
        DataStreamSource<String> source = KafkaConfigUtil.buildSource(env);
        source.map(new GamePlayMapFunction())
                .map(gamePlay -> {
                    String gameId = gamePlay.getGameId();
                    return gameId;
                })
                .addSink(KafkaConfigUtil.buildSink(parameterTool));
        env.execute("Streaming GamePlay Kafka to Kafka");

    }
}
