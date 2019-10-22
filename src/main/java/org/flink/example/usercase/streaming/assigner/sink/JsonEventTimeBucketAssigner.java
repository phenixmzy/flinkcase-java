package org.flink.example.usercase.streaming.assigner.sink;

import com.alibaba.fastjson.JSONObject;
import org.apache.flink.api.common.typeutils.base.StringSerializer;
import org.apache.flink.core.io.SimpleVersionedSerializer;
import org.apache.flink.streaming.api.functions.sink.filesystem.BucketAssigner;
import org.flink.example.usercase.streaming.util.DateTimeUtil;

import java.text.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonEventTimeBucketAssigner implements BucketAssigner<String, String> {
    private static Logger logger = LoggerFactory.getLogger(JsonEventTimeBucketAssigner.class);
    private String dateTimeFormat;
    private String filed;

    public JsonEventTimeBucketAssigner() {}

    public JsonEventTimeBucketAssigner(String jsonFiled, String dateTimeFormat) {
        this.dateTimeFormat = dateTimeFormat;
        this.filed = jsonFiled;
    }

    @Override
    public String getBucketId(String eventJsonStr, Context context) {
        try {
            JSONObject json = JSONObject.parseObject(eventJsonStr);
            String parititionValue = DateTimeUtil.getTimeStampStr(json.getInteger(filed) , dateTimeFormat);
            return parititionValue;
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
