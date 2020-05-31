package org.flink.example.usercase.streaming.application.ad;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.flink.example.common.constant.PropertiesConstants;
import org.flink.example.usercase.streaming.application.configcenter.ConfigCenterManager;
import org.flink.example.usercase.streaming.application.configcenter.ConfigValue;

import java.util.ArrayList;
import java.util.HashMap;

public class FileDataRichMapFunction extends RichMapFunction<String, RecordData> {
    private String[] serviceNames;
    private HashMap<String, ConfigValue> appConfigs = new HashMap<String, ConfigValue>();
    private HashMap<String, ArrayList<String>> tableFields = new HashMap<String, ArrayList<String>>();

    @Override
    public RecordData map(String eventJsonStr) throws Exception {
        JSONObject json = JSONObject.parseObject(eventJsonStr);
        String docType = json.getString(PropertiesConstants.FILE_JSON_DATA_TABLE_KEY);

        if (appConfigs.containsKey(docType)) {
            String sinkTopic = appConfigs.get(docType).getConfig("kafka.sink.topic");
            return getRecordData(docType, sinkTopic, json);
        }
        return null;
    }

    public FileDataRichMapFunction(String[] serviceNames) {
        this.serviceNames = serviceNames;

    }

    private ArrayList<String> getConfigFields(String config_fields) {
        ArrayList<String> fieldList = new ArrayList<String>();
        String[] fields = config_fields.split(",");
        for (String field : fields) {
            String tableField = field.replace(" ", "");
            fieldList.add(tableField);
        }
        return fieldList;
    }

    private RecordData getRecordData(String fieldsKey, String topic, JSONObject json) {
        StringBuilder builder = new StringBuilder();
        ArrayList<String> fieldList = tableFields.get(fieldsKey);
        for (String field : fieldList) {
            Object value = json.get(field);
            if (value == null) {
                builder.append(ConfigCenterManager.NULL_STR).append(ConfigCenterManager.SPLIT_FLAG);
                continue;
            }
            builder.append(value.toString()).append(ConfigCenterManager.SPLIT_FLAG);
        }

        String outputData =  builder.length() > 0 ? builder.substring(0, builder.length() - ConfigCenterManager.SPLIT_FLAG.length()) : "";
        RecordData rd = new RecordData(topic, outputData);
        return rd;
    }
}
