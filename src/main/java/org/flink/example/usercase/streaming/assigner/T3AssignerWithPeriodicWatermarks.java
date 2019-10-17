package org.flink.example.usercase.streaming.assigner;

import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks;
import org.apache.flink.streaming.api.watermark.Watermark;

import javax.annotation.Nullable;

public class T3AssignerWithPeriodicWatermarks implements AssignerWithPeriodicWatermarks<Tuple3<String, Integer, Integer>> {
    // 当前最大时间
    protected long currentMaxTimestamp = Long.MIN_VALUE;

    // 延时设定,表示在maxOutOfOrderness秒内有效,超过maxOutOfOrderness秒的数据被认定为迟到的事件
    protected long maxOutOfOrderness = 0L;

    public T3AssignerWithPeriodicWatermarks(final long maxOutOfOrderness) {
        this.maxOutOfOrderness = maxOutOfOrderness;
    }

    @Nullable
    @Override
    public Watermark getCurrentWatermark() {
        // 根据最大事件时间 - 最大乱序时延长度,最后得到水位线对象Watermark
        return new Watermark(currentMaxTimestamp == Long.MIN_VALUE ? Long.MIN_VALUE : currentMaxTimestamp - maxOutOfOrderness);
    }

    @Override
    public long extractTimestamp(Tuple3<String, Integer, Integer> item, long l) {
        // 获取当前事件时间
        int currentTimeStamp = item.f1;
        // 对比当前事件时间和历史最大时间,将最新的时间赋值给currentMaxTimestamp
        currentMaxTimestamp = Math.max(currentTimeStamp, currentMaxTimestamp);

        // 返回当前事件事件
        return currentTimeStamp;
    }
}
