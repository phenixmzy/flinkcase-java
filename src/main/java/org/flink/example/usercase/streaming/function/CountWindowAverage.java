package org.flink.example.usercase.streaming.function;

import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.common.state.StateTtlConfig;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.util.Collector;

public class CountWindowAverage extends RichFlatMapFunction<Tuple3<String,Integer, Integer>, Tuple3<String,Integer, Integer> > {

    private transient ValueState<Tuple3<String,Integer, Integer>> sum;

    @Override
    public void flatMap(Tuple3<String, Integer, Integer> in, Collector<Tuple3<String, Integer, Integer>> collector) throws Exception {
        Tuple3<String, Integer, Integer> currentValue = sum.value();
        currentValue.f1 += 1;
        currentValue.f2 += in.f2;
        sum.update(currentValue);

        // if the count reaches 2, emit the average and clear the state
        if (currentValue.f1 >= 2) {
            collector.collect(new Tuple3<String, Integer, Integer>(in.f0, in.f1, currentValue.f2/ currentValue.f1));
            sum.clear();
        }
    }

    public void open(Configuration config) {
        StateTtlConfig ttlConfig = StateTtlConfig.newBuilder(Time.minutes(10))
                .setUpdateType(StateTtlConfig.UpdateType.OnCreateAndWrite)
                .setStateVisibility(StateTtlConfig.StateVisibility.NeverReturnExpired)
                .build();

        //ValueStateDescriptor
        ValueStateDescriptor<Tuple3<String,Integer, Integer>> desc =
                new ValueStateDescriptor<Tuple3<String,Integer, Integer>>("avg",
                        TypeInformation.of(new TypeHint<Tuple3<String, Integer, Integer>>() {}),
                Tuple3.of("", 0, 0));
        desc.enableTimeToLive(ttlConfig);
        sum = getRuntimeContext().getState(desc);
    }
}
