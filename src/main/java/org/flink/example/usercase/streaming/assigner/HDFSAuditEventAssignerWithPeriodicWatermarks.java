package org.flink.example.usercase.streaming.assigner;

import org.flink.example.usercase.model.HDFSAuditEvent;

public class HDFSAuditEventAssignerWithPeriodicWatermarks extends AbstractAssignerWithPeriodicWatermarks {

    public HDFSAuditEventAssignerWithPeriodicWatermarks(long maxOutOfOrderness) {
        super(maxOutOfOrderness);
    }

    @Override
    public long extractTimestamp(Object o, long l) {
        HDFSAuditEvent event = (HDFSAuditEvent)o;
        currentMaxtTimestamp = Math.max(event.getTimeStampMS(), currentMaxtTimestamp);
        return event.getTimeStampMS();
    }
}
