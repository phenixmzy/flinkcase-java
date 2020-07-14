package org.flink.example.usercase.streaming.application.gameplay;

/*
import org.apache.flink.api.common.functions.JoinFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.JoinedStreams;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.java.StreamTableEnvironment;
import org.flink.example.common.constant.PropertiesConstants;
import org.flink.example.usercase.model.GameBrowseEvent;
import org.flink.example.usercase.model.GamePlayEvent;
import org.flink.example.usercase.streaming.source.GameBrowseEventSource;
import org.flink.example.usercase.streaming.source.GamePlayEventSource;
import org.flink.example.usercase.streaming.util.ExecutionEnvUtil;
import org.flink.example.usercase.streaming.util.KafkaConfigUtil;
*/

public class GamePlayCCApplication {

   /* public static void main(String[] args) throws Exception {
        System.out.println("Hello Apollo Config Center");
        ParameterTool parameterTool = ExecutionEnvUtil.createParameterTool(args);
        StreamExecutionEnvironment env = ExecutionEnvUtil.prepare(parameterTool);
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);
        *//*
        int recordMaxNum = Integer.valueOf(parameterTool.getRequired("gameplay.record.max.num"));
        int gameIdMaxNum = Integer.valueOf(parameterTool.getRequired("gameplay.gid.max.num"));
        int userIdMaxNum = Integer.valueOf(parameterTool.getRequired("gameplay.uid.max.num"));
        int maxDelay = Integer.valueOf(parameterTool.getRequired("gameplay.delay.max.num"));
        int maxTimeLen = Integer.valueOf(parameterTool.getRequired("gameplay.timelen.max.num"));
        *//*
        int recordMaxNum = 100;
        int gameIdMaxNum = 100000;
        int userIdMaxNum = 1000000000;
        int maxDelay = 300;
        int maxTimeLen = 300;
        DataStream<GamePlayEvent> source = env.addSource(new GamePlayEventSource(recordMaxNum, gameIdMaxNum, userIdMaxNum, maxDelay, maxTimeLen));
        Table gamePlayTable = tableEnv.fromDataStream(source);
        Table result =tableEnv.sqlQuery("select game_id, sum(timeLen) from " + gamePlayTable + " where timeLen > 10 group by game_id");
        tableEnv.toRetractStream(result, GamePlayEvent.class).print();

        env.execute("GamePlay Flink SQL");
    }*/
}
