package org.flink.example.usercase.streaming.application.configcenter;

import java.util.HashMap;

public class ConfigCenterManager {
    public final static String AD_CONFIG_FIELDS = "ad_id, ad_name, ad_channel";
    public final static String APP_CONFIG_FIELDS = "app_id, app_name, app_size";

    public final static String NULL_STR = "NULL";
    public final static String SPLIT_FLAG = "\u0001";

    public static HashMap<String, ConfigValue> CONFIG_VALUES = new HashMap<String, ConfigValue>();

    public final static String AD_SERVICE_NAME_KEY = "cl.ad";
    public final static String APP_SERVICE_NAME_KEY = "cl.app";

    static {
        init();
    }

    public static void init() {
        ConfigValue adConfigValue = new ConfigValue("CANAL_AD", AD_CONFIG_FIELDS, "ad-source", "ad-sink");
        ConfigValue appConfigValue = new ConfigValue("CANAL_APP", APP_CONFIG_FIELDS, "app-source", "app-sink");
        CONFIG_VALUES.put(AD_SERVICE_NAME_KEY, adConfigValue);
        CONFIG_VALUES.put(APP_SERVICE_NAME_KEY, appConfigValue);
    }

    public static HashMap<String, ConfigValue> getConfigValues() {
        return CONFIG_VALUES;
    }
}
