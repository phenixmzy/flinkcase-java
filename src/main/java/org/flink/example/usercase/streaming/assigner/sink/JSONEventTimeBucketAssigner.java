package org.flink.example.usercase.streaming.assigner.sink;

import com.alibaba.fastjson.JSONObject;
import org.apache.flink.core.io.SimpleVersionedSerializer;
import org.apache.flink.streaming.api.functions.sink.filesystem.BucketAssigner;
import org.apache.flink.streaming.api.functions.sink.filesystem.bucketassigners.SimpleVersionedStringSerializer;
import org.flink.example.usercase.streaming.util.DateTimeUtil;

import java.text.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JSONEventTimeBucketAssigner<IN> implements BucketAssigner<String, String> {
    private static Logger logger = LoggerFactory.getLogger(JSONEventTimeBucketAssigner.class);
    private String dateTimeFormat;
    private String filed;
    private boolean isMS = false;

    public JSONEventTimeBucketAssigner(String jsonFiled, String dateTimeFormat, boolean isMS) {
        this.dateTimeFormat = dateTimeFormat;
        this.filed = jsonFiled;
        this.isMS = isMS;
    }

    @Override
    public String getBucketId(String eventJsonStr, Context context) {
        try {
            JSONObject json = JSONObject.parseObject(eventJsonStr);
            long eventTime = isMS ? json.getLong(filed) : json.getLong(filed) * DateTimeUtil.SECONDS;
            String parititionValue = DateTimeUtil.getTimeStampStr(eventTime , dateTimeFormat);
            return parititionValue;
        } catch (ParseException e) {
            logger.error(e.getMessage());
        }
        return "error-bucket-id";
    }

    @Override
    public SimpleVersionedSerializer<String> getSerializer() {
        return SimpleVersionedStringSerializer.INSTANCE;
    }
}
