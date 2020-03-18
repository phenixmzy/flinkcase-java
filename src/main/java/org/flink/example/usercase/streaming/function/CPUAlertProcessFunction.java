package org.flink.example.usercase.streaming.function;

import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;
import org.flink.example.usercase.model.CPUState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class CPUValueState {
    public float lastCpuUsed;
    public long timerTimeStamp;
    public boolean timerIsActive;
}

public class CPUAlertProcessFunction extends KeyedProcessFunction<Tuple, CPUState, String> {
    private static final Logger LOG = LoggerFactory.getLogger(CPUAlertProcessFunction.class);
    private ValueState<Float> lastCpuUsedState;
    private ValueState<Long> currTimer ;
    private ValueState<Boolean> timerIsActive;
    //private ValueState<CPUValueState> lastState;

    @Override
    public void open(Configuration parameters) throws Exception {
        lastCpuUsedState = this.getRuntimeContext().getState(new ValueStateDescriptor<>("cpuUsedState", Float.class));
        currTimer = this.getRuntimeContext().getState(new ValueStateDescriptor<>("currTimer", Long.class));
        //timerIsActive = this.getRuntimeContext().getState(new ValueStateDescriptor<Boolean>("timer-is-active", Boolean.class));

        //lastState = this.getRuntimeContext().getState(new ValueStateDescriptor<CPUValueState>("cpu-state", CPUValueState.class));
    }

    @Override
    public void processElement(CPUState value, Context context, Collector<String> collector) throws Exception {


        // 获取上次使用率
        Float preCpuUsedValue = lastCpuUsedState.value();

        // 更新到目前使用率
        lastCpuUsedState.update(value.getUsed());

        // 获取上次定时器时间
        Long currStateTS = currTimer.value();


        /*
        CPUValueState tmpCPUValueState = lastState.value();
        if (tmpCPUValueState == null) {
            CPUValueState currCPUValueState = new CPUValueState();
            currCPUValueState.timerTimeStamp = 0L;
            currCPUValueState.lastCpuUsed = value.getUsed();
            currCPUValueState.timerIsActive = false;
            lastState.update(currCPUValueState);
        } else {
            boolean timerIsActive = tmpCPUValueState.timerIsActive;
            if (tmpCPUValueState.lastCpuUsed < value.getUsed()) {
                long timerTs = context.timerService().currentProcessingTime() + 10000L;
                if (!timerIsActive) {
                    context.timerService().registerProcessingTimeTimer(timerTs);
                }
                tmpCPUValueState.lastCpuUsed = value.getUsed();
                tmpCPUValueState.timerTimeStamp = timerTs;
                tmpCPUValueState.timerIsActive = true;
                lastState.update(tmpCPUValueState);
            } else {
                if (timerIsActive) {
                    context.timerService().deleteProcessingTimeTimer(tmpCPUValueState.timerTimeStamp);
                }
                tmpCPUValueState.lastCpuUsed = value.getUsed();
                tmpCPUValueState.timerTimeStamp = 0L;
                tmpCPUValueState.timerIsActive = false;
                lastState.update(tmpCPUValueState);
            }
        }
        */


        String flag = "----------------------------------------------";
        String rowEnd = "\n";

        StringBuilder builder = new StringBuilder(flag);
        builder.append(rowEnd);
        builder.append(value.toString()).append(rowEnd);


        if ((preCpuUsedValue == null && currStateTS == null) ||
                (preCpuUsedValue !=null && value.getUsed() > preCpuUsedValue.floatValue())) {
            long timerTs = context.timerService().currentProcessingTime() + 10000L;
            context.timerService().registerProcessingTimeTimer(timerTs);
            currTimer.update(timerTs);
            builder.append("REGISTER timerTs AND UPDATE currTimer").append(rowEnd);
        } else if ( preCpuUsedValue.floatValue() > value.getUsed()) { // 如有第一条数据是残留,或使用率下降,删除定时器
            builder.append("deleteProcessingTimeTimer check ");
            builder.append(rowEnd);
            try {
                printCheck(preCpuUsedValue, builder);
                builder.append(rowEnd);

                context.timerService().deleteProcessingTimeTimer(currStateTS.longValue());
                currTimer.clear();
                builder.append(" deleteProcessingTimeTimer done!").append(rowEnd);
            } catch (Exception e) {
                builder.append("Execution:").append(e).append(rowEnd);
            }
        } else {
            builder.append("else ");
            printCheck(preCpuUsedValue, builder);
            builder.append(rowEnd);
        }

        System.out.println(builder.toString());

    }

    @Override
    public void onTimer(long timestamp, OnTimerContext ctx, Collector<String> out) throws Exception {
        out.collect(ctx.getCurrentKey() + " 使用率连续上升");
        //currTimer.clear();
    }

    private void printCheck(Float preCpuUsedValue, StringBuilder builder) {
        if (preCpuUsedValue != null) {
            builder.append("[preCpuUsedValue=").append(preCpuUsedValue.floatValue()).append(" ] ");
        } else {
            builder.append("[preCpuUsedValue= null ] ");
        }

    }

}
