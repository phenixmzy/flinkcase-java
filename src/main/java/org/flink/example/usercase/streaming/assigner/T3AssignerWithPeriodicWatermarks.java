package org.flink.example.usercase.streaming.assigner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks;
import org.apache.flink.streaming.api.watermark.Watermark;

import javax.annotation.Nullable;

public class T3AssignerWithPeriodicWatermarks implements AssignerWithPeriodicWatermarks<Tuple3<String, Integer, Integer>> {
    private static Logger logger = LoggerFactory.getLogger(T3AssignerWithPeriodicWatermarks.class);

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

        long currentWatermark = currentMaxTimestamp - maxOutOfOrderness;
        logger.info("currentWatermark=" + currentWatermark);
        return new Watermark(currentMaxTimestamp == Long.MIN_VALUE ? Long.MIN_VALUE : currentWatermark);
    }

    @Override
    public long extractTimestamp(Tuple3<String, Integer, Integer> item, long l) {
        // 获取当前事件时间(item.f1), 对比当前事件时间和历史最大时间,将最新的时间赋值给currentMaxTimestamp
        //currentMaxTimestamp = Math.max(currentEventTimeStamp, currentMaxTimestamp);
        logger.info("start extractTimestamp()  currentMaxTimestamp=" + currentMaxTimestamp);
        if (item.f1 > currentMaxTimestamp) {
            currentMaxTimestamp = item.f1;
        }
        logger.info("end extractTimestamp() item.f1= " + item.f1 + " currentMaxTimestamp=" + currentMaxTimestamp);
        // 返回最大事件事件
        return currentMaxTimestamp;
    }
}
