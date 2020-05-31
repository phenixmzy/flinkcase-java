package org.flink.example.usercase.streaming.application.ad;

public interface ConfigurationChange {
    public void load();
    public void update(String key, String newValue);
}
