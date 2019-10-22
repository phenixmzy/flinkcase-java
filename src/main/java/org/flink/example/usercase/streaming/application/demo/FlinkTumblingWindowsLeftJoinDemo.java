package org.flink.example.usercase.streaming.application.demo;

import org.apache.flink.api.common.functions.CoGroupFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.tuple.Tuple5;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;

import org.flink.example.usercase.streaming.source.StreamDataSourceTable1;
import org.flink.example.usercase.streaming.source.StreamDataSourceTable2;
import org.flink.example.usercase.streaming.util.ExecutionEnvUtil;


public class FlinkTumblingWindowsLeftJoinDemo {
    public static void main(String[] args) throws Exception {
        ParameterTool parameterTool = ExecutionEnvUtil.createParameterTool(args);

        int windowSize = 10;
        long delay = 5100L;

        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        env.setParallelism(1);

        // 设置数据源
        DataStream<Tuple3<String, String, Long>> leftSource = env.addSource(new StreamDataSourceTable1()).name("Demo Source");
        DataStream<Tuple3<String, String, Long>> rightSource = env.addSource(new StreamDataSourceTable2()).name("Demo Source");

        // 设置水位线
        DataStream<Tuple3<String, String, Long>> leftStream = leftSource.assignTimestampsAndWatermarks(
                new BoundedOutOfOrdernessTimestampExtractor<Tuple3<String, String, Long>>(Time.milliseconds(delay)) {
                    @Override
                    public long extractTimestamp(Tuple3<String, String, Long> element) {
                        return element.f2;
                    }
                }
        );

        DataStream<Tuple3<String, String, Long>> rigjhtStream = rightSource.assignTimestampsAndWatermarks(
                new BoundedOutOfOrdernessTimestampExtractor<Tuple3<String, String, Long>>(Time.milliseconds(delay)) {
                    @Override
                    public long extractTimestamp(Tuple3<String, String, Long> element) {
                        return element.f2;
                    }
                }
        );

        // join 操作
        leftStream.coGroup(rigjhtStream)
                .where(new LeftSelectKey()).equalTo(new RightSelectKey())
                .window(TumblingEventTimeWindows.of(Time.seconds(windowSize)))
                .apply(new LeftJoin())
                .writeAsText(parameterTool.getRequired("demo.result.path"));

        env.execute("Left-Join TimeWindow Demo");
    }

    public static class LeftJoin implements CoGroupFunction<Tuple3<String, String, Long>, Tuple3<String, String, Long>, Tuple5<String, String, String, Long, Long>> {
        @Override
        public void coGroup(Iterable<Tuple3<String, String, Long>> leftElements, Iterable<Tuple3<String, String, Long>> rightElements, Collector<Tuple5<String, String, String, Long, Long>> out) {

            for (Tuple3<String, String, Long> leftElem : leftElements) {
                boolean hadElements = false;
                for (Tuple3<String, String, Long> rightElem : rightElements) {
                    out.collect(new Tuple5<>(leftElem.f0, leftElem.f1, rightElem.f1, leftElem.f2, rightElem.f2));
                    hadElements = true;
                }
                if (!hadElements) {
                    out.collect(new Tuple5<>(leftElem.f0, leftElem.f1, "null", leftElem.f2, -1L));
                }
            }
        }
    }

    public static class LeftSelectKey implements KeySelector<Tuple3<String, String, Long>, String> {
        @Override
        public String getKey(Tuple3<String, String, Long> w) {
            return w.f0;
        }
    }

    public static class RightSelectKey implements KeySelector<Tuple3<String, String, Long>, String> {
        @Override
        public String getKey(Tuple3<String, String, Long> w) {
            return w.f0;
        }
    }
}
