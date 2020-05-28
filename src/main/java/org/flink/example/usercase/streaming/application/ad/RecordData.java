package org.flink.example.usercase.streaming.application.ad;

public class RecordData {
    private String topic;
    private String data;

    public RecordData(String topic, String data) {
        this.topic = topic;
        this.data = data;
    }

    public String getTopic() {
        return topic;
    }

    public String getData() {
        return data;
    }

}
