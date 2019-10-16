package org.flink.example.usercase.model;

public class HDFSAuditEvent {
    private long timeStampMS;
    private String allowed;
    private String ugi;
    private String ip;
    private String cmd;
    private String src;
    private String dst;

    public long getTimeStampMS() {
        return timeStampMS;
    }

    public void setTimeStampMS(long timeStampMS) {
        this.timeStampMS = timeStampMS;
    }

    public String getAllowed() {
        return allowed;
    }

    public void setAllowed(String allowed) {
        this.allowed = allowed;
    }

    public String getUgi() {
        return ugi;
    }

    public void setUgi(String ugi) {
        this.ugi = ugi;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getDst() {
        return dst;
    }

    public void setDst(String dst) {
        this.dst = dst;
    }
}
