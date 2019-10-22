package org.flink.example.usercase.streaming.assigner.sink;

import org.apache.flink.core.io.SimpleVersionedSerializer;
import org.apache.flink.streaming.api.functions.sink.filesystem.BucketAssigner;
import org.flink.example.usercase.model.GamePlayEvent;
import org.flink.example.usercase.streaming.util.DateTimeUtil;

import java.text.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GamePlayDateTimeBucketAssigner implements BucketAssigner<GamePlayEvent, String> {
    private static Logger logger = LoggerFactory.getLogger(GamePlayDateTimeBucketAssigner.class);
    private String dateTimeFormat;

    public GamePlayDateTimeBucketAssigner(String dateTimeFormat) {
        this.dateTimeFormat = dateTimeFormat;
    }

    @Override
    public String getBucketId(GamePlayEvent gamePlayEvent, Context context) {
        try {
            return DateTimeUtil.getTimeStampStr(gamePlayEvent.getLeaveTime() * 1000L , dateTimeFormat);
        } catch (ParseException e) {
            logger.error(e.getMessage());
        }
        return "error-bucket-id";
    }

    @Override
    public SimpleVersionedSerializer<String> getSerializer() {
        return null;
    }
}
