package org.flink.example.usercase.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.flink.example.usercase.streaming.source.GameBrowseEventSource;
import org.flink.example.usercase.streaming.util.ExecutionEnvUtil;
import org.flink.example.usercase.streaming.util.KafkaConfigUtil;

/**
 * --kafka.sink.topic gameplay  \
 * 	--bootstrap.servers yzj-client-01:9092,yzj-client-02:9092,yzj-client-03:9092 \
 * 	--group.id test-java-gameplay-log-input-gid
 * */
public class GamePlayEventPrint {
    private static final Logger LOGGER = LoggerFactory.getLogger(GamePlayEventPrint.class);
    public static void main(String[] args) throws Exception {
        int recordMaxNum = 1000000;
        int gameIdMaxNum = 100000;
        int userIdMaxNum = 1000000;
        int maxDelay = 100;
        int maxTimeLen = 30000;
        ParameterTool parameterTool = ExecutionEnvUtil.createParameterTool(args);
        StreamExecutionEnvironment env = ExecutionEnvUtil.prepare(parameterTool);
        for(String a : args) {
            LOGGER.info(a);
        }
        env.addSource(new GameBrowseEventSource(recordMaxNum, gameIdMaxNum, userIdMaxNum, maxDelay, maxTimeLen))
                .map(gameplay -> {
            StringBuilder builder = new StringBuilder();
            builder.append(gameplay.getGameId()).append("-").append(gameplay.getUserId()).append("-").append(gameplay.getGameType()).append("-").append(gameplay.getClientVersion());
            return builder.toString();
        }).addSink(KafkaConfigUtil.buildSink(parameterTool));
        env.execute();
    }
}
