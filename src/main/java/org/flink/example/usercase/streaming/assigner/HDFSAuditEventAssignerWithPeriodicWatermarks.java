package org.flink.example.usercase.streaming.assigner;

import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks;
import org.apache.flink.streaming.api.watermark.Watermark;
import org.flink.example.usercase.model.HDFSAuditEvent;

import javax.annotation.Nullable;

public class HDFSAuditEventAssignerWithPeriodicWatermarks implements AssignerWithPeriodicWatermarks<HDFSAuditEvent> {
    protected long currentTimestamp = Long.MIN_VALUE;
    protected long maxOutOfOrderness = 0L;

    public HDFSAuditEventAssignerWithPeriodicWatermarks(final long maxOutOfOrderness) {
        this.maxOutOfOrderness = maxOutOfOrderness * 1000L;
    }

    @Nullable
    @Override
    public Watermark getCurrentWatermark() {
        return new Watermark(currentTimestamp - maxOutOfOrderness);
    }

    @Override
    public long extractTimestamp(HDFSAuditEvent hdfsAuditEvent, long timeStamp) {
        currentTimestamp = Math.max(hdfsAuditEvent.getTimeStampMS(), timeStamp);
        return currentTimestamp;
    }
}
