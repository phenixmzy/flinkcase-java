package org.flink.example.usercase.streaming.util;

import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class FlinkEvnUtil {
    private final static long ONE_SECONDS = 1000;
    private final static long ONE_MIN = ONE_SECONDS * 60;

    public static long getCheckPointInteravlMin(long timeMin) {
        return timeMin * ONE_MIN;
    }

    public static long getCheckPointTimeOutMin(long timeOutMin) {
        return timeOutMin * ONE_MIN;
    }

    public static StreamExecutionEnvironment getStreamEnvironment(ParameterTool params) {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.getConfig().disableSysoutLogging();
        env.getConfig().setRestartStrategy(RestartStrategies.fixedDelayRestart(4, 10000));

        // create a checkpoint every 5 min
        env.enableCheckpointing(getCheckPointInteravlMin(1));
        env.getCheckpointConfig().setCheckpointTimeout(getCheckPointTimeOutMin(10));
        env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);

        // make parameters available in the web interface
        env.getConfig().setGlobalJobParameters(params);
        return env;
    }
}
