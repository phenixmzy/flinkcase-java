package org.flink.example.usercase.streaming.util;

import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.api.java.utils.ParameterTool;
//import org.apache.flink.runtime.state.filesystem.FsStateBackend;
import org.apache.flink.contrib.streaming.state.EmbeddedRocksDBStateBackend;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.flink.example.common.constant.PropertiesConstants;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * flink-conf.yaml
 * #配置checkpoint保存个数,减轻存储压力
 * state.checkpoints.num-retained: 2
 *
 * #提升作业恢复速度
 * state.backend.local-recovery: true
 * */
public class ExecutionEnvUtil {
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
        return prepare(parameterTool, TimeCharacteristic.EventTime);
    }

    public static StreamExecutionEnvironment prepare(ParameterTool parameterTool, TimeCharacteristic timeCharacteristic) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(parameterTool.getInt(PropertiesConstants.FLINK_STREAM_PARALLELISM_KEY, 5));


        /**
         * 重启策略配置:
         * ** failureRateRestart,
         * ** noRestart(),
         * ** fixedDelayRestart,
         * ** env.enableCheckpointing
         * */
        //env.enableCheckpointing(parameterTool.getInt(PropertiesConstants.FLINK_STREAM_CHECKPOINT_INTERVAL_KEY, PropertiesConstants.DEFAULT_FLINK_STREAM_CHECKPOINT_INTERVAL_VALUE)); // create a checkpoint every 180 seconds
        //env.setRestartStrategy(RestartStrategies.noRestart());
        //env.setRestartStrategy(RestartStrategies.failureRateRestart(3, Time.minutes(5), Time.seconds(10)));
        env.setRestartStrategy(RestartStrategies.fixedDelayRestart(parameterTool.getInt(PropertiesConstants.FLINK_STREAM_FIXED_DELAY_RESTART_KEY, PropertiesConstants.DEFAULT_FLINK_STREAM_FIXED_DELAY_RESTART_VAL), 10000));
        if (parameterTool.getBoolean(PropertiesConstants.FLINK_STREAM_CHECKPOINT_ENABLE_KEY, true)) {
            env.enableCheckpointing(parameterTool.getInt(PropertiesConstants.FLINK_STREAM_CHECKPOINT_INTERVAL_KEY, PropertiesConstants.DEFAULT_FLINK_STREAM_CHECKPOINT_INTERVAL_VALUE)); // create a checkpoint every 180 seconds
            env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
            env.getCheckpointConfig().enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
            env.setStateBackend(new EmbeddedRocksDBStateBackend());

            /*if (parameterTool.getProperties().contains(PropertiesConstants.FLINK_STATE_BACKEND_DIR_KEY)) {
                env.setStateBackend(new FsStateBackend(parameterTool.getRequired(PropertiesConstants.FLINK_STATE_BACKEND_DIR_KEY)));
            }*/

            env.getCheckpointConfig().setCheckpointTimeout(parameterTool.getInt(PropertiesConstants.FLINK_STREAM_CHECKPOINT_TIMEOUT_MS_KEY, PropertiesConstants.DEFAULT_FLINK_STREAM_CHECKPOINT_INTERVAL_VALUE));
            env.getCheckpointConfig().setTolerableCheckpointFailureNumber(parameterTool.getInt(PropertiesConstants.FLINK_TOLERABLE_CHECKPOINT_FAILURE_NUMBER_KEY,PropertiesConstants.DEFAULT_FLINK_TOLERABLE_CHECKPOINT_FAILURE_NUMBER_VAL));
        }

        //env.getCheckpointConfig().enableUnalignedCheckpoints(true);
        env.getConfig().setGlobalJobParameters(parameterTool); // make parameters available in the web interface
        env.setStreamTimeCharacteristic(timeCharacteristic);
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
