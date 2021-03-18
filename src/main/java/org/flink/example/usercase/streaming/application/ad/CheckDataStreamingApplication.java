package org.flink.example.usercase.streaming.application.ad;

import com.alibaba.fastjson.JSONObject;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.flink.example.common.constant.PropertiesConstants;
import org.flink.example.usercase.streaming.util.ExecutionEnvUtil;
import org.flink.example.usercase.streaming.util.KafkaConfigUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.util.ArrayList;

public class CheckDataStreamingApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(CheckDataStreamingApplication.class);

    public static void main(String[] args) throws Exception {
        final ParameterTool parameterTool = ExecutionEnvUtil.createParameterTool(args);
        StreamExecutionEnvironment env = ExecutionEnvUtil.prepare(parameterTool);
        DataStreamSource<String> source = KafkaConfigUtil.buildSource(env);

        source.rebalance()
                .map(new MapFunction<String, Tuple2<Boolean,String>>() {
                    @Override
                    public Tuple2<Boolean,String> map(String eventJsonStr) throws Exception {
                        try {
                            JSONObject json = JSONObject.parseObject(eventJsonStr);
                        } catch (Exception ex) {
                            LOGGER.error(eventJsonStr);
                            return Tuple2.of(false, eventJsonStr);
                        }
                        return Tuple2.of(true, eventJsonStr);
                    }
                }).filter(eventItem -> !eventItem.f0).addSink(KafkaConfigUtil.buildSink(parameterTool));
        env.execute("Streaming check error data to kafka");
    }
}
