package org.flink.example.usercase.streaming.application.ad;

import com.alibaba.fastjson.JSONObject;
import org.flink.example.usercase.streaming.apollo.ConfigurationCenterManagerProxy;
import org.flink.example.usercase.streaming.application.ad.configcenter.ConfigCenterManager;
import org.flink.example.usercase.streaming.application.ad.configcenter.ConfigValue;
import org.flink.example.usercase.streaming.application.ad.configcenter.RecordDataConfigValue;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * NameSpace: collect.canal and collect.file
 * attr:
 * docName.fields
 * docName.kafka.source.topic
 * docName.kafka.sink.topic
 * */
public abstract class ConfigurationService implements ConfigurationChange{
    protected final static String CONFIG_CENTER_DATA_FIELDS_KEY = "fields";
    protected final static String CONFIG_CENTER_KAFKA_SOURCE_TOPIC_KEY = "kafka.source.topic";
    protected final static String CONFIG_CENTER_KAFKA_SINK_TOPIC_KEY = "kafka.sink.topic";
    protected final static String CONFIG_CENTER_FIELDS_COMM_FLAG = ",";

    protected final static String CONFIG_CENTER_NULL_STR = "NULL";
    protected final static String CONFIG_CENTER_SPLIT_FLAG = "\u0001";

    protected HashMap<String, RecordDataConfigValue> docAttrs = new HashMap<String, RecordDataConfigValue>();

    protected String[] recordDataNames;

    private ConfigurationCenterManagerProxy proxy;

    public ConfigurationService(String[] recordDataNames, String configNameSpaces) {
        this.recordDataNames = recordDataNames;
        proxy = new ConfigurationCenterManagerProxy(configNameSpaces);
        proxy.init();
        proxy.register(this);
    }

    protected abstract RecordDataConfigValue getRecordDataConfigValueBy(String recordDataName);

    public void load() {
        for (String recordDataName : recordDataNames) {
            docAttrs.put(recordDataName, getRecordDataConfigValueBy(recordDataName));
        }
    }

    public RecordData getRecordData(String docName, String topic, JSONObject json) {
        StringBuilder builder = new StringBuilder();
        ArrayList<String> fieldList = docAttrs.get(docName).getFields();
        for (String field : fieldList) {
            Object value = json.get(field);
            if (value == null) {
                builder.append(CONFIG_CENTER_NULL_STR).append(CONFIG_CENTER_SPLIT_FLAG);
                continue;
            }
            builder.append(value.toString()).append(CONFIG_CENTER_NULL_STR);
        }
        String outputData =  builder.length() > 0 ? builder.substring(0, builder.length() - ConfigCenterManager.SPLIT_FLAG.length()) : "";
        RecordData rd = new RecordData(topic, outputData);
        return rd;
    }

    protected ArrayList<String> getConfigFields(String config_fields) {
        ArrayList<String> fieldList = new ArrayList<String>();
        String[] fields = config_fields.split(CONFIG_CENTER_FIELDS_COMM_FLAG);
        for (String field : fields) {
            String tableField = field.replace(" ", "");
            fieldList.add(tableField);
        }
        return fieldList;
    }

    public void update(String key, String newValue) {
        for (String recordDataName : recordDataNames) {
            if (key.startsWith(recordDataName)) {
                RecordDataConfigValue rdcv = docAttrs.get(recordDataName);
                if (key.endsWith(CONFIG_CENTER_DATA_FIELDS_KEY)) {
                    rdcv.getCv().putConfigs(CONFIG_CENTER_DATA_FIELDS_KEY, newValue);
                    rdcv.setFields(getConfigFields(newValue));
                } else if (key.endsWith(CONFIG_CENTER_KAFKA_SOURCE_TOPIC_KEY)){
                    rdcv.getCv().putConfigs(CONFIG_CENTER_KAFKA_SOURCE_TOPIC_KEY, newValue);
                } else if (key.endsWith(CONFIG_CENTER_KAFKA_SINK_TOPIC_KEY)) {
                    rdcv.getCv().putConfigs(CONFIG_CENTER_KAFKA_SINK_TOPIC_KEY, newValue);
                }
                break;
            }
        }
    }

    public ArrayList<String> getRecordDataFields(String recordDataName) {
        return docAttrs.get(recordDataName).getFields();
    }

    public ConfigValue getRecordDataConfigValue(String recordDataName) {
        return docAttrs.get(recordDataName).getCv();
    }
}
