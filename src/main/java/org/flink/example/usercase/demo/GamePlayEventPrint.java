package org.flink.example.usercase.demo;

import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.flink.example.usercase.streaming.source.GameBrowseEventSource;
import org.flink.example.usercase.streaming.util.ExecutionEnvUtil;

public class GamePlayEventPrint {
    public static void main(String[] args) throws Exception {
        int recordMaxNum = 1000000;
        int gameIdMaxNum = 100000;
        int userIdMaxNum = 1000000;
        int maxDelay = 100;
        int maxTimeLen = 30000;
        ParameterTool parameterTool = ExecutionEnvUtil.createParameterTool(args);
        StreamExecutionEnvironment env = ExecutionEnvUtil.prepare(parameterTool);

        env.addSource(new GameBrowseEventSource(recordMaxNum, gameIdMaxNum, userIdMaxNum, maxDelay, maxTimeLen)).map(gameplay -> {
            StringBuilder builder = new StringBuilder();
            builder.append(gameplay.getGameId()).append("-").append(gameplay.getUserId()).append("-").append(gameplay.getGameType()).append("-").append(gameplay.getClientVersion());
            return builder.toString();
        }).print();
        env.execute();
    }
}
