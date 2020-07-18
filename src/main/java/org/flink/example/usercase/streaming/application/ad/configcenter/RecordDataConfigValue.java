package org.flink.example.usercase.streaming.application.ad.configcenter;

import java.util.ArrayList;

public class RecordDataConfigValue {
    private String key;
    private ConfigValue cv;
    private ArrayList<String> fields;

    public RecordDataConfigValue(String key, ConfigValue configValue, ArrayList<String> fields) {
        this.key = key;
        this.cv = cv;
        this.fields = fields;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public ConfigValue getCv() {
        return cv;
    }

    public void setCv(ConfigValue cv) {
        this.cv = cv;
    }

    public ArrayList<String> getFields() {
        return fields;
    }

    public void setFields(ArrayList<String> fields) {
        this.fields = fields;
    }
}