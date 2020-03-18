package org.flink.example.usercase.streaming.application.gameplay;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer011;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;
import org.flink.example.common.constant.PropertiesConstants;
import org.flink.example.usercase.model.GamePlayEvent;
import org.flink.example.usercase.streaming.util.ExecutionEnvUtil;
import org.flink.example.usercase.streaming.util.GsonUtil;
import org.flink.example.usercase.streaming.util.KafkaConfigUtil;

import java.util.ArrayList;
import java.util.List;

enum GAMETYPE {
    EXT("exe"), FLASH("flash"), ONLINE("online"), WEB("web");
    private String gameType;
    private GAMETYPE(String gameType) {
        this.gameType = gameType;
    }

    public String getGameType() { return gameType; }
}

public class GamePlaySplitTypeApplication {

    private static String getTag(GamePlayEvent gamePlayEvent) {
        String tag = null;
        switch (GAMETYPE.valueOf(gamePlayEvent.getGameType())) {
            case EXT:
                tag = GAMETYPE.EXT.getGameType();
                break;
            case ONLINE:
                tag = GAMETYPE.ONLINE.getGameType();
            case WEB:
                tag = GAMETYPE.WEB.getGameType();
            case FLASH:
                tag = GAMETYPE.FLASH.getGameType();
        }
        return tag;
    }

    public static void main(String[] args) throws Exception {
        ParameterTool parameterTool = ExecutionEnvUtil.createParameterTool(args);
        StreamExecutionEnvironment env = ExecutionEnvUtil.prepare(parameterTool);
        DataStreamSource<String> source = KafkaConfigUtil.buildSource(env);

        final OutputTag<GamePlayEvent> exeOutputTag = new OutputTag<>("exe", TypeInformation.of(GamePlayEvent.class));
        final OutputTag<GamePlayEvent> flashOutputTag = new OutputTag<>("flash", TypeInformation.of(GamePlayEvent.class));
        final OutputTag<GamePlayEvent> webOutputTag = new OutputTag<>("web", TypeInformation.of(GamePlayEvent.class));
        final OutputTag<GamePlayEvent> onlineOutputTag = new OutputTag<>("online", TypeInformation.of(GamePlayEvent.class));



        source.map(new MapFunction<String, GamePlayEvent>() {

            @Override
            public GamePlayEvent map(String gamePlayJson) throws Exception {
                GamePlayEvent gamePlayEvent = GsonUtil.fromJson(gamePlayJson, GamePlayEvent.class);
                return gamePlayEvent;
            }
        }).keyBy(new KeySelector<GamePlayEvent, String>() {

            @Override
            public String getKey(GamePlayEvent gamePlayEvent) throws Exception {
                return getTag(gamePlayEvent);
            }
        }).process(new  ProcessFunction<GamePlayEvent, GamePlayEvent>() {

            @Override
            public void processElement(GamePlayEvent gamePlayEvent, Context context, Collector<GamePlayEvent> collector) throws Exception {
                OutputTag<GamePlayEvent> outputTag = null;
                switch (GAMETYPE.valueOf(gamePlayEvent.getGameType())) {
                    case EXT:
                        outputTag = exeOutputTag;
                        break;
                    case ONLINE:
                        outputTag = onlineOutputTag;
                    case WEB:
                        outputTag = webOutputTag;
                    case FLASH:
                        outputTag = flashOutputTag;
                }
                context.output(outputTag, gamePlayEvent);
            }
        }).map(gamePlayEvent -> gamePlayEvent.toString())
                .getSideOutput(exeOutputTag).addSink(
                new FlinkKafkaProducer011(parameterTool.getRequired(PropertiesConstants.KAFKA_BROKERS_KEY),
                parameterTool.getRequired(PropertiesConstants.KAFKA_SINK_TOPIC_KEY),
                new SimpleStringSchema()));
    }
}
