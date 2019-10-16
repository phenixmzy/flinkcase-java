package org.flink.example.usercase.streaming.assigner;

import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks;
import org.apache.flink.streaming.api.watermark.Watermark;

public abstract class AbstractAssignerWithPeriodicWatermarks<T> implements AssignerWithPeriodicWatermarks {
    protected long currentMaxtTimestamp = 0L;
    protected long maxOutOfOrderness = 0L;

    public AbstractAssignerWithPeriodicWatermarks(long maxOutOfOrderness) {
        this.maxOutOfOrderness = maxOutOfOrderness;
    }

    public Watermark getCurrentWatermark() {
        return new Watermark(currentMaxtTimestamp - maxOutOfOrderness);
    }
}
