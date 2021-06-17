package org.flink.example.usercase.streaming.application.gameplay;

import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.flink.example.usercase.streaming.application.map.GamePlayMapFunction;
import org.flink.example.usercase.streaming.util.ExecutionEnvUtil;
import org.flink.example.usercase.streaming.util.KafkaConfigUtil;

public class GamePlayEventETLStreamingApplication {
    public static void main(String[] args) throws Exception {
        final ParameterTool parameterTool = ExecutionEnvUtil.createParameterTool(args);
        StreamExecutionEnvironment env = ExecutionEnvUtil.prepare(parameterTool);
        DataStreamSource<String> source = KafkaConfigUtil.buildSource(env);
        source.rebalance().addSink(KafkaConfigUtil.buildSink(parameterTool));
        env.execute("Streaming GamePlay Kafka ETL to Kafka");

    }
}
