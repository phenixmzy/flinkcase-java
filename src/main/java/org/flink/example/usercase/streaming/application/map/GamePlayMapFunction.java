package org.flink.example.usercase.streaming.application.map;

import org.apache.flink.api.common.functions.MapFunction;
import org.flink.example.usercase.model.GamePlayEvent;
import org.flink.example.usercase.streaming.util.GsonUtil;

public class GamePlayMapFunction implements MapFunction<String, GamePlayEvent> {
    @Override
    public GamePlayEvent map(String gamePlayJson) throws Exception {
        GamePlayEvent gamePlayEvent = GsonUtil.fromJson(gamePlayJson, GamePlayEvent.class);
        return gamePlayEvent;
    }
}
