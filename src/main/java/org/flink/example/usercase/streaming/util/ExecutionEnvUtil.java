package org.flink.example.usercase.streaming.util;

import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.flink.example.common.constant.PropertiesConstants;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ExecutionEnvUtil {
    /*
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
    */

    public static final ParameterTool PARAMETER_TOOL = createParameterTool();

    private static ParameterTool createParameterTool() {
        try {
            return ParameterTool
                    .fromPropertiesFile(ExecutionEnvUtil.class.getResourceAsStream(PropertiesConstants.PROPERTIES_FILE_NAME))
                    .mergeWith(ParameterTool.fromSystemProperties())
                    .mergeWith(ParameterTool.fromMap(getenv()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ParameterTool createParameterTool(final String[] args) throws Exception {
//        ParameterTool parameterTool = ParameterTool.fromArgs(args);
        return ParameterTool
                .fromPropertiesFile(ExecutionEnvUtil.class.getResourceAsStream(PropertiesConstants.PROPERTIES_FILE_NAME))
                .mergeWith(ParameterTool.fromArgs(args))
                .mergeWith(ParameterTool.fromSystemProperties())
                .mergeWith(ParameterTool.fromMap(getenv()));
    }

    public static StreamExecutionEnvironment prepare(ParameterTool parameterTool) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(parameterTool.getInt(PropertiesConstants.FLINK_STREAM_PARALLELISM_KEY, 5));
        env.getConfig().disableSysoutLogging();
        env.getConfig().setRestartStrategy(RestartStrategies.fixedDelayRestart(4, 10000));
        if (parameterTool.getBoolean(PropertiesConstants.FLINK_STREAM_CHECKPOINT_ENABLE_KEY, true)) {
            env.enableCheckpointing(parameterTool.getInt(PropertiesConstants.FLINK_STREAM_CHECKPOINT_INTERVAL_KEY, 1000)); // create a checkpoint every 5 seconds
            env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
        }

        env.getConfig().setGlobalJobParameters(parameterTool); // make parameters available in the web interface
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        return env;
    }

    private static Map<String, String> getenv() {
        Map<String, String> map = new HashMap<>();
        for (Map.Entry<String, String> entry : System.getenv().entrySet()) {
            map.put(entry.getKey(), entry.getValue());
        }
        return map;
    }
}
