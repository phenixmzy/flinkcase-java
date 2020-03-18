package org.flink.example.usercase.model;

public class CPUState {
    private String host;
    private float used;
    private long timeStampMS;

    public CPUState() {}

    public CPUState(String host, float used, long timeStampMS) {
        this.host = host;
        this.used = used;
        this.timeStampMS = timeStampMS;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public float getUsed() {
        return used;
    }

    public void setUsed(float used) {
        this.used = used;
    }

    public long getTimeStampMS() {
        return timeStampMS;
    }

    public void setTimeStampMS(long timeStampMS) {
        this.timeStampMS = timeStampMS;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{host=").append(host)
                .append(", used=").append(used)
                .append(", timeStampMS=").append(timeStampMS)
                .append("}");
        return builder.toString();
    }
}
