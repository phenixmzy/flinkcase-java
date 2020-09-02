package org.flink.example.usercase.streaming.application.map;

import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.common.state.StateTtlConfig;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.configuration.Configuration;


public class GamePlayMapRFunction extends RichMapFunction {

    @Override
    public Object map(Object o) throws Exception {
        return null;
    }

    @Override
    public void open(Configuration parameters) throws Exception {
//        getRuntimeContext().getMapState()
        StateTtlConfig.Builder stateTTL = StateTtlConfig.newBuilder(Time.seconds(10)).setUpdateType(StateTtlConfig.UpdateType.OnCreateAndWrite);

    }

    @Override
    public void close() throws Exception {
    }
}
