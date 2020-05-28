package org.flink.example.usercase.streaming.application.ad;

import com.alibaba.fastjson.JSONObject;
import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigChangeListener;
import com.ctrip.framework.apollo.ConfigService;
import com.ctrip.framework.apollo.model.ConfigChange;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;
import org.flink.example.common.constant.PropertiesConstants;
import org.flink.example.usercase.streaming.application.configcenter.ConfigCenterManager;
import org.flink.example.usercase.streaming.application.configcenter.ConfigValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class MRichMapFunction extends RichMapFunction<String, RecordData> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MRichMapFunction.class);
    private String[] serviceNames;

    private HashMap<String, ConfigValue> configValues = new HashMap<String, ConfigValue>();
    private HashMap<String, ArrayList<String>> fields = new HashMap<String, ArrayList<String>>();

    public MRichMapFunction(String[] serviceNames) {
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
        configValues.putAll(ConfigCenterManager.getConfigValues());
        Set<String> keySet = configValues.keySet();
        for (String key : keySet) {
            fields.put(key, getConfigFields(configValues.get(key).getFields()));
        }
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        //config = (ParameterTool) getRuntimeContext().getExecutionConfig().getGlobalJobParameters();
        reload();
    }

    @Override
    public RecordData map(String eventJsonStr) throws Exception {
        JSONObject json = JSONObject.parseObject(eventJsonStr);
        String fieldsKey = json.getString(PropertiesConstants.CANAL_JSON_DATA_TABLE_KEY);
        return getRecordData(json, fieldsKey);
    }

    private ArrayList<String> getConfigFields(String config_fields) {
        ArrayList<String> fieldList = new ArrayList<String>();
        String[] fields = config_fields.split(",");
        for (String field : fields) {
            fieldList.add(field);
        }
        return fieldList;
    }

    private RecordData getRecordData(JSONObject json, String fieldsKey) {
        StringBuilder builder = new StringBuilder();
        ArrayList<String> fieldList = fields.get(fieldsKey);
        for (String field : fieldList) {
            Object value = json.get(field);
            if (value == null) {
                builder.append(ConfigCenterManager.NULL_STR).append(ConfigCenterManager.SPLIT_FLAG);
                continue;
            }
            builder.append(value.toString()).append(ConfigCenterManager.SPLIT_FLAG);
        }

        String outputData =  builder.length() > 0 ? builder.substring(0, builder.length() - ConfigCenterManager.SPLIT_FLAG.length()) : "";
        RecordData rd = new RecordData(fieldsKey, outputData);
        return rd;
    }
}
