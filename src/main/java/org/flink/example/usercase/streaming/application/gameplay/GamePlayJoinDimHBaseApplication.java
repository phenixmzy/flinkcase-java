package org.flink.example.usercase.streaming.application.gameplay;

import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.AsyncDataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.async.AsyncFunction;
import org.flink.example.usercase.model.GameInfo;
import org.flink.example.usercase.streaming.application.gameplay.join.dim.GameInfoDimHBaseAsyncFunction;
import org.flink.example.usercase.streaming.util.ExecutionEnvUtil;
import org.flink.example.usercase.streaming.util.KafkaConfigUtil;

import java.util.concurrent.TimeUnit;

public class GamePlayJoinDimHBaseApplication {
    public static void main(String[] args) throws Exception{
        AsyncFunction<String, GameInfo> function = new GameInfoDimHBaseAsyncFunction("zk", "zkPort", "hTable");

        ParameterTool parameterTool = ExecutionEnvUtil.createParameterTool(args);
        StreamExecutionEnvironment env =  ExecutionEnvUtil.prepare(parameterTool);
        DataStreamSource<String> dataStream = KafkaConfigUtil.buildSource(env);

        int timeout = Integer.valueOf(parameterTool.getRequired("timeout"));
        AsyncDataStream.orderedWait(dataStream, function, timeout, TimeUnit.MILLISECONDS, 20);
        env.execute("join dim hbase");
    }
}
