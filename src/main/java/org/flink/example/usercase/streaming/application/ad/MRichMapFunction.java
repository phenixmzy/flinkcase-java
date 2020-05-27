package org.flink.example.usercase.streaming.application.ad;

import com.alibaba.fastjson.JSONObject;
import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigChangeListener;
import com.ctrip.framework.apollo.ConfigService;
import com.ctrip.framework.apollo.model.ConfigChange;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;
import org.flink.example.usercase.streaming.application.configcenter.ConfigCenterManager;
import org.flink.example.usercase.streaming.application.configcenter.ConfigValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;

//https://ci.apache.org/projects/flink/flink-docs-release-1.10/zh/dev/connectors/kafka.html
public class MRichMapFunction extends RichMapFunction<String, String> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MRichMapFunction.class);
    private String[] serviceNames;

    public MRichMapFunction(String[] serviceNames) {
        this.serviceNames = serviceNames;
    }

    private HashMap<String, ArrayList<String>> FIELD_LIST = new HashMap<String, ArrayList<String>>();

    private void reload() {
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
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        //config = (ParameterTool) getRuntimeContext().getExecutionConfig().getGlobalJobParameters();
        reload();
    }

    @Override
    public String map(String eventJsonStr) throws Exception {
        LOGGER.info("GET FIELD_LIST:");
        LOGGER.info(eventJsonStr);
        JSONObject json = JSONObject.parseObject(eventJsonStr);
        return getFormatStr(json);
    }

    private ArrayList<String> getConfigFields(String a_service_config_fields) {
        ArrayList<String> fieldList = new ArrayList<String>();
        String[] fields = a_service_config_fields.split(",");
        for (String field : fields) {
            fieldList.add(field);
        }
        return fieldList;
    }

    private String getFormatStr(JSONObject json) {
        StringBuilder builder = new StringBuilder();
        String docName = json.getString("doc_name");
        ArrayList<String> fieldList = FIELD_LIST.get(docName);
        for (String field : fieldList) {
            Object value = json.get(field);
            if (value == null) {
                builder.append(ConfigCenterManager.NULL_STR).append(ConfigCenterManager.SPLIT_FLAG);
                continue;
            }
            builder.append(value.toString()).append(ConfigCenterManager.SPLIT_FLAG);
        }
        return builder.length() > 0 ? builder.substring(0, builder.length() - ConfigCenterManager.SPLIT_FLAG.length()) : "";
    }
}
