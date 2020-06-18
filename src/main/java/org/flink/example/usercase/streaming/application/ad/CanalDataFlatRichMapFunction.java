package org.flink.example.usercase.streaming.application.ad;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.util.Collector;
import org.flink.example.common.constant.PropertiesConstants;
import org.flink.example.usercase.streaming.application.configcenter.ConfigCenterManager;
import org.flink.example.usercase.streaming.application.configcenter.ConfigValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;

public class CanalDataFlatRichMapFunction extends RichFlatMapFunction<String, RecordData> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CanalDataFlatRichMapFunction.class);
    private String[] serviceNames;

    private HashMap<String, ConfigValue> appConfigs = new HashMap<String, ConfigValue>();
    private HashMap<String, ArrayList<String>> tableFields = new HashMap<String, ArrayList<String>>();

    public CanalDataFlatRichMapFunction(String[] serviceNames) {
        this.serviceNames = serviceNames;
    }

    private void reloadConfig() {
        /*
        Config config = ConfigService.getAppConfig();
        for (String sn : this.serviceNames) {
            ConfigValue cv = ConfigCenterManager.getConfigValues().get(sn);
            FIELD_LIST.put(sn, getConfigFields(cv.getFields()));
        }

        config.addChangeListener(new ConfigChangeListener() {

            @Override
            public void onChange(ConfigChangeEvent configChangeEvent) {
                for (String key : configChangeEvent.changedKeys()) {
                    ConfigChange change = configChangeEvent.getChange(key);
                    change.getNewValue();
                }
            }
        });
        */
    }

    private void reload() {
        ConfigCenterManager.init();
        //ConfigCenterManager.init();
        for (String ns : serviceNames) {
            String table = ConfigCenterManager.getConfigValues(ns + ".table");
            String fields = ConfigCenterManager.getConfigValues(ns + ".fields");
            String sinkTopic = ConfigCenterManager.getConfigValues(ns + ".kafka.sink.topic");
            LOGGER.info("table:{}, fields:{}, sinkTopic:{}", table, fields, sinkTopic);
            System.out.println("table:" + table + ", fields:" + fields + ", sinkTopic:" + sinkTopic);

            ConfigValue cv = new ConfigValue(table);
            cv.putConfigs("kafka.sink.topic", sinkTopic);
            cv.putConfigs("table.fields", fields);
            appConfigs.put(table, cv);
            tableFields.put(table, getConfigFields(fields));
        }
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        //config = (ParameterTool) getRuntimeContext().getExecutionConfig().getGlobalJobParameters();
        reload();
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

        String outputData = builder.length() > 0 ? builder.substring(0, builder.length() - ConfigCenterManager.SPLIT_FLAG.length()) : "";
        RecordData rd = new RecordData(topic, outputData);
        return rd;
    }

    @Override
    public void flatMap(String eventJsonStr, Collector<RecordData> collector) throws Exception {
        LOGGER.info(eventJsonStr);
        JSONObject json = JSONObject.parseObject(eventJsonStr);
        String table = json.getString(PropertiesConstants.CANAL_JSON_DATA_TABLE_KEY);
        for (String k : appConfigs.keySet()) {
            ConfigValue cv = appConfigs.get(k);
            LOGGER.info("ConfigCenter appConfigs ac_key:{}", k);
            for (String kk : cv.getConfigs().keySet())
                LOGGER.info("ConfigCenter appConfigs ac_key:{}, attrs[{}:{}]", k, kk, cv.getConfig(kk));
        }
        LOGGER.info("Stream data of table:{},", table);

        if (appConfigs.containsKey(table)) {
            String sinkTopic = appConfigs.get(table).getConfig("kafka.sink.topic");

            JSONArray datasJson = json.getJSONArray("data");
            if (datasJson == null) {
                LOGGER.warn("warn data, the data is null.: {}", eventJsonStr);
                return;
            }
            try {
                for (int i = 0, size = datasJson.size(); i < size; i++) {
                    JSONObject dataJson = datasJson.getJSONObject(i);
                    collector.collect(getRecordData(table, sinkTopic, dataJson));
                    LOGGER.info("collector table:{}, sinkTopic:{}", table, sinkTopic);
                }
            } catch (Exception e) {
                LOGGER.error("error data: {}", eventJsonStr);
            }
            LOGGER.info("collector table:{}, sinkTopic:{}, recordCount:{}", table, sinkTopic, datasJson.size());
        }
    }
}
