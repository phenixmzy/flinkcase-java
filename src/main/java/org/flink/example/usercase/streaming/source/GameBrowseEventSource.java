package org.flink.example.usercase.streaming.source;

import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.flink.example.usercase.model.GameBrowseEvent;
import org.flink.example.usercase.model.factory.GameBrowseEventFactory;

public class GameBrowseEventSource implements SourceFunction<GameBrowseEvent> {
    private boolean isRunning = true;
    private int recordMaxNum;
    private int gameIdMaxNum;
    private int userIdMaxNum;
    private int maxDelay;
    private int maxTimeLen;

    public GameBrowseEventSource(int recordMaxNum, int gameIdMaxNum, int userIdMaxNum, int maxDelay, int maxTimeLen) {
        this.recordMaxNum = recordMaxNum;
        this.gameIdMaxNum = gameIdMaxNum;
        this.userIdMaxNum = userIdMaxNum;
        this.maxDelay = maxDelay;
        this.maxTimeLen = maxTimeLen;
    }

    @Override
    public void run(SourceContext sourceContext) throws Exception {
        while (isRunning) {
            Thread.sleep(50);
            GameBrowseEvent event = GameBrowseEventFactory.build(this.gameIdMaxNum, this.userIdMaxNum, this.maxDelay);
            for(int i = 0; i < recordMaxNum; i++) {
                sourceContext.collect(event);
            }
        }
    }

    @Override
    public void cancel() {
        this.isRunning = false;
    }
}
