package org.flink.example.usercase.streaming.application.ad;

import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.flink.example.common.constant.PropertiesConstants;
import org.flink.example.usercase.streaming.application.configcenter.ConfigCenterManager;
import org.flink.example.usercase.streaming.application.configcenter.ConfigValue;
import org.flink.example.usercase.streaming.util.ExecutionEnvUtil;
import org.flink.example.usercase.streaming.util.KafkaConfigUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ADReportFormatStreamingApplicationCC {
    private static final Logger LOGGER = LoggerFactory.getLogger(ADReportFormatStreamingApplicationCC.class);

    private static List<String> getSourceTopics() {
        Iterator<ConfigValue> cvs = ConfigCenterManager.getConfigValues().values().iterator();
        List<String> topicList = new ArrayList<String>();
        while (cvs.hasNext()) {
            ConfigValue cv = cvs.next();
            topicList.add(cv.getSourceTopic());
        }
        return topicList;
    }

    public static void main(String[] args) throws Exception {
        final ParameterTool parameterTool = ExecutionEnvUtil.createParameterTool(args);
        StreamExecutionEnvironment env = ExecutionEnvUtil.prepare(parameterTool);
        String nameServices = parameterTool.getRequired(PropertiesConstants.NAME_SERVICES_KEY);

        DataStreamSource<String> source = KafkaConfigUtil.buildSource(env, getSourceTopics());

        source.map(new MRichMapFunction(nameServices.split(",")))
                .addSink(KafkaConfigUtil.buildSink(parameterTool));

        env.execute("Streaming name service of config center to kafka");

    }

}