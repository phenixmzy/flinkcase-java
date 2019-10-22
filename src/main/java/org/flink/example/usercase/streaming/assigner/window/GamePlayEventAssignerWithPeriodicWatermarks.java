package org.flink.example.usercase.streaming.assigner.window;

import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks;
import org.apache.flink.streaming.api.watermark.Watermark;
import org.flink.example.usercase.model.GamePlayEvent;

import javax.annotation.Nullable;

public class GamePlayEventAssignerWithPeriodicWatermarks implements AssignerWithPeriodicWatermarks<GamePlayEvent> {
    // 当前最大时间
    protected long currentMaxTimestamp = Long.MIN_VALUE;

    // 延时设定,表示在maxOutOfOrderness秒内有效,超过maxOutOfOrderness秒的数据被认定为迟到的事件
    protected long maxOutOfOrderness = 0L;

    public GamePlayEventAssignerWithPeriodicWatermarks(final long maxOutOfOrderness) {
        this.maxOutOfOrderness = maxOutOfOrderness;
    }

    /**
     * 定义水位线的生成
     * @return
     */
    @Nullable
    @Override
    public Watermark getCurrentWatermark() {
        // 根据最大事件时间 - 最大乱序时延长度,最后得到水位线对象Watermark
        return new Watermark(currentMaxTimestamp == Long.MIN_VALUE ? Long.MIN_VALUE : currentMaxTimestamp - maxOutOfOrderness);
    }

    /**
     * 定义抽取TimeStamp的逻辑
     * @param gamePlayEvent
     * @param previousEventTimeStamp
     * @return
     */
    @Override
    public long extractTimestamp(GamePlayEvent gamePlayEvent, long previousEventTimeStamp) {
        // 获取当前事件时间
        int currentTimeStamp = gamePlayEvent.getLeaveTime();
        // 对比当前事件时间和历史最大时间,将最新的时间赋值给currentMaxTimestamp
        currentMaxTimestamp = Math.max(currentTimeStamp, currentMaxTimestamp);

        // 返回当前事件事件
        return currentTimeStamp;
    }
}
