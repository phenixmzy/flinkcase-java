package org.flink.example.usercase.streaming.application.configcenter;

import java.util.HashMap;

public class ConfigCenterManager {
    public final static String AD_CONFIG_FIELDS = "ad_id,ad_name,ad_channel";
    public final static String APP_CONFIG_FIELDS = "app_id,app_name,app_size";

    public final static String NULL_STR = "NULL";
    public final static String SPLIT_FLAG = "\u0001";

    public static HashMap<String, String> CONFIG_VALUES = new HashMap<String, String>();

    static {
        init();
    }

    public static void init() {
        CONFIG_VALUES.put("canal.cl.ad","canal.cl.ad.table,canal.cl.ad.fields,canal.cl.ad.kafka.sink.topic");
        CONFIG_VALUES.put("canal.cl.ad.table","ad");
        CONFIG_VALUES.put("canal.cl.ad.fields",AD_CONFIG_FIELDS);
        CONFIG_VALUES.put("canal.cl.ad.kafka.sink.topic", "ad-sink");

        CONFIG_VALUES.put("canal.cl.app","canal.cl.app.table,canal.cl.app.fields,canal.cl.app.kafka.sink.topic");
        CONFIG_VALUES.put("canal.cl.app.table","app");
        CONFIG_VALUES.put("canal.cl.app.fields", APP_CONFIG_FIELDS);
        CONFIG_VALUES.put("canal.cl.app.kafka.sink.topic", "app-sink");
    }

    public static String getConfigValues(String key) {
        return CONFIG_VALUES.get(key);
    }
}
