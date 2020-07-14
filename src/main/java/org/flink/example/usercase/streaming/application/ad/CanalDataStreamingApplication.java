package org.flink.example.usercase.streaming.application.ad;

import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.flink.example.common.constant.PropertiesConstants;
import org.flink.example.usercase.streaming.util.ExecutionEnvUtil;
import org.flink.example.usercase.streaming.util.KafkaConfigUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CanalDataStreamingApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(CanalDataStreamingApplication.class);

    public static void main(String[] args) throws Exception {
        final ParameterTool parameterTool = ExecutionEnvUtil.createParameterTool(args);
        StreamExecutionEnvironment env = ExecutionEnvUtil.prepare(parameterTool);
        String nameServices = parameterTool.getRequired(PropertiesConstants.NAME_SERVICES_KEY);
        LOGGER.info("nameService:{}",nameServices);
        DataStreamSource<String> source = KafkaConfigUtil.buildSource(env);

        source.flatMap(new CanalDataFlatRichMapFunction(nameServices.split(",")))
                .addSink(KafkaConfigUtil.buildSinkRecordDataForEXACTLYONCE(parameterTool));

        env.execute("Streaming name service of config center to kafka");

    }

}