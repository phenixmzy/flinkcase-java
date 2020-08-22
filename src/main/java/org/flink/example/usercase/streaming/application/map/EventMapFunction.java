package org.flink.example.usercase.streaming.application.map;

import org.apache.flink.api.common.functions.MapFunction;
import org.flink.example.usercase.model.Event;
import org.flink.example.usercase.streaming.util.GsonUtil;

public class EventMapFunction implements MapFunction<String, Event> {
    @Override
    public Event map(String eventJson) throws Exception {
        Event event = GsonUtil.fromJson(eventJson, Event.class);
        System.out.println(eventJson);
        return event;
    }
}
