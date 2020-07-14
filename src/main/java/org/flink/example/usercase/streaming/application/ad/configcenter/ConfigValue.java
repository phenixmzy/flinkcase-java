package org.flink.example.usercase.streaming.application.ad.configcenter;

import java.util.HashMap;

public class ConfigValue {
    private String key;
    private HashMap<String,String> configs = new HashMap<String,String>();

    public ConfigValue(String key) {
        this.key = key;
    }

    public String getKey() { return key; }

    public HashMap<String, String> getConfigs() {
        return configs;
    }

    public void putConfigs(String attrKey, String attrValue) {
        configs.put(attrKey, attrValue);
    }

    public String getConfig(String attr) {
        return configs.get(attr);
    }
}
