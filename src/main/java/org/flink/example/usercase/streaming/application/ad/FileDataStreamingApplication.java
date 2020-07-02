package org.flink.example.usercase.streaming.application.ad;

import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.flink.example.common.constant.PropertiesConstants;
import org.flink.example.usercase.streaming.application.configcenter.ConfigValue;
import org.flink.example.usercase.streaming.application.configcenter.FileConfigCenterManager;
import org.flink.example.usercase.streaming.util.ExecutionEnvUtil;
import org.flink.example.usercase.streaming.util.KafkaConfigUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class FileDataStreamingApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileDataStreamingApplication.class);

    public static void main(String[] args) throws Exception{
        final ParameterTool parameterTool = ExecutionEnvUtil.createParameterTool(args);
        StreamExecutionEnvironment env = ExecutionEnvUtil.prepare(parameterTool);
        String nameServices = parameterTool.getRequired(PropertiesConstants.NAME_SERVICES_KEY);

        DataStreamSource<String> source = KafkaConfigUtil.buildSource(env, getTopics(nameServices));
        source.map(new FileDataRichMapFunction(nameServices.split(",")))
                .filter(rd -> {
                    return rd == null ? false : true;
                })
                .addSink(KafkaConfigUtil.buildSinkRecordDataForEXACTLYONCE(parameterTool));
        env.execute("Collect File Json for chuangliang ad");
    }

    private static ArrayList<String> getTopics(String nameServices) {
        ArrayList<String> topics = new ArrayList<String>();
        FileConfigCenterManager.init();
        //init();
        HashMap<String, ConfigValue> docNameConfigValues = FileConfigCenterManager.getConfigValueByNameSpaces(nameServices.split(","));
        Set<String> docNames = docNameConfigValues.keySet();
        for (String docName : docNames) {
            ConfigValue cv = docNameConfigValues.get(docName);
            topics.add(cv.getConfig("kafka.source.topic"));
        }
        return topics;
    }
}
