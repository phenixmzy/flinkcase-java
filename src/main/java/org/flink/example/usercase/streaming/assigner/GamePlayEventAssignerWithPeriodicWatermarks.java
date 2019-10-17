package org.flink.example.usercase.streaming.assigner;

import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks;
import org.apache.flink.streaming.api.watermark.Watermark;
import org.flink.example.usercase.model.GamePlayEvent;

import javax.annotation.Nullable;

public class GamePlayEventAssignerWithPeriodicWatermarks implements AssignerWithPeriodicWatermarks<GamePlayEvent> {
    protected long currentTimestamp = Long.MIN_VALUE;
    protected long maxOutOfOrderness = 0L;

    public GamePlayEventAssignerWithPeriodicWatermarks(final long maxOutOfOrderness) {
        this.maxOutOfOrderness = maxOutOfOrderness;
    }

    @Nullable
    @Override
    public Watermark getCurrentWatermark() {
        return new Watermark(currentTimestamp == Long.MIN_VALUE ? Long.MIN_VALUE : currentTimestamp - maxOutOfOrderness);
    }

    @Override
    public long extractTimestamp(GamePlayEvent gamePlayEvent, long timeStamp) {
        currentTimestamp = Math.max(gamePlayEvent.getLeaveTime(), currentTimestamp);
        return currentTimestamp;
    }
}
