package org.flink.example.usercase.streaming.source;

import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.flink.example.usercase.model.LoginEvent;

import java.util.List;
import java.util.Arrays;

public class LoginEventSource  implements SourceFunction<LoginEvent> {
    public static final List<LoginEvent> EVENT_LIST = Arrays.asList(
            new LoginEvent("小明", "192.168.0.1", "fail"),
                new LoginEvent("小明", "192.168.0.2", "fail"),
                new LoginEvent("小王", "192.168.10,11", "fail"),
                new LoginEvent("小王", "192.168.10,12", "fail"),
                new LoginEvent("小明", "192.168.0.3", "fail"),
                new LoginEvent("小明", "192.168.0.4", "fail"),
                new LoginEvent("小王", "192.168.10,10", "success"));
    private boolean isRunning = true;
    private int recordMaxNum;

    public LoginEventSource(int recordMaxNum) {
        this.recordMaxNum = recordMaxNum;
    }

    @Override
    public void run(SourceContext<LoginEvent> sourceContext) throws Exception {
        while (isRunning) {
            Thread.sleep(50);
            for(int i = 0; i < recordMaxNum; i++) {
                for(LoginEvent event : EVENT_LIST) {
                    sourceContext.collect(event);
                }
            }
        }
    }

    @Override
    public void cancel() {
        this.isRunning = false;
    }
}
