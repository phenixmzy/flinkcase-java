package org.flink.example.usercase.streaming.application.configcenter;

public class ConfigValue {
    private String serviceName;
    private String fields;
    private String sourceTopic;
    private String sinkTopic;

    public ConfigValue(String serviceName, String fields, String sourceTopic, String sinkTopic) {
        this.serviceName = serviceName;
        this.fields = fields;
        this.sourceTopic = sourceTopic;
        this.sinkTopic = sinkTopic;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getFields() {
        return fields;
    }

    public String getSourceTopic() {
        return sourceTopic;
    }

    public String getSinkTopic() {
        return sinkTopic;
    }
}
