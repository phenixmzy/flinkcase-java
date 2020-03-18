package org.flink.example.usercase.streaming.assigner.window;

import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.windowing.time.Time;
/**
 * bound 延时的时间, 系统会每隔200ms计算一次WM
 * */
public class GamePlayBoundedOutOfOrdernessTimestampExtractor extends BoundedOutOfOrdernessTimestampExtractor<Tuple3<String, Integer, Integer>> {

    public GamePlayBoundedOutOfOrdernessTimestampExtractor(Time bound) {
        super(bound);
    }

    @Override
    public long extractTimestamp(Tuple3<String, Integer, Integer> event) {
        return (long) (event.f1 * 1000L);
    }
}
