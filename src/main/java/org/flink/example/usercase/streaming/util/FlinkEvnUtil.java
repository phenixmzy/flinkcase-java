package org.flink.example.usercase.streaming.util;

public class FlinkEvnUtil {
    private final static long ONE_SECONDS = 1000;
    private final static long ONE_MIN = ONE_SECONDS * 60;

    public static long getCheckPointInteravlMin(long timeMin) {
        return timeMin * ONE_MIN;
    }

    public static long getCheckPointTimeOutMin(long timeOutMin) {
        return timeOutMin * ONE_MIN;
    }
}
