package org.flink.example.usercase.streaming.application.stateapp;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer011;
import org.flink.example.common.constant.PropertiesConstants;
import org.flink.example.usercase.model.CPUState;
import org.flink.example.usercase.streaming.function.CPUAlertProcessFunction;
import org.flink.example.usercase.streaming.util.ExecutionEnvUtil;
import org.flink.example.usercase.streaming.util.KafkaConfigUtil;

public class CPUAlertApp {
    public static void main(String[] args) throws Exception {
        ParameterTool parameterTool = ExecutionEnvUtil.createParameterTool(args);
        StreamExecutionEnvironment env = ExecutionEnvUtil.prepare(parameterTool);
        DataStreamSource<String> source = KafkaConfigUtil.buildSource(env);

        FlinkKafkaProducer011 kafkaSink = new FlinkKafkaProducer011(parameterTool.getRequired(PropertiesConstants.KAFKA_BROKERS_KEY),
                parameterTool.getRequired(PropertiesConstants.KAFKA_SINK_TOPIC_KEY),
                new SimpleStringSchema());

        DataStream<CPUState> dataStream = source.filter(item -> item.trim().length() >0 ).map(new MapFunction<String, CPUState>() {
            @Override
            public CPUState map(String s) throws Exception {
                String[] pair = s.split(":");
                String host = pair[0];
                Float cpuUsed = Float.valueOf(pair[1]);
                Long timeStamp = Long.valueOf(pair[2]);
                CPUState cpuState = new CPUState(host, cpuUsed, timeStamp);
                return cpuState;
            }
        }).assignTimestampsAndWatermarks(
                new BoundedOutOfOrdernessTimestampExtractor<CPUState>(Time.seconds(10)) {

            @Override
            public long extractTimestamp(CPUState cpuState) {
                return cpuState.getTimeStampMS();
            }
        });

        dataStream.keyBy("host")
                .process(new CPUAlertProcessFunction())
                .addSink(kafkaSink);

        env.execute("CPU Used State Test");
    }
}

/*
.map(item ->{
        StringBuilder builder = new StringBuilder();
        builder.append(item.getHost()).append(" ").append(item.getUsed()).append(" ").append(item.getTimeStampMS());
        return builder.toString();
        })*/
