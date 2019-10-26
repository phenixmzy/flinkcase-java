package org.flink.example.usercase.streaming.application.cep;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.flink.api.common.serialization.SimpleStringEncoder;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.cep.CEP;
import org.apache.flink.cep.PatternSelectFunction;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.IterativeCondition;
import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.filesystem.StreamingFileSink;
import org.apache.flink.streaming.api.functions.sink.filesystem.bucketassigners.DateTimeBucketAssigner;
import org.apache.flink.streaming.api.windowing.time.Time;

import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer011;
import org.flink.example.common.constant.PropertiesConstants;
import org.flink.example.usercase.model.GamePlayEvent;
import org.flink.example.usercase.model.LoginEvent;
import org.flink.example.usercase.streaming.assigner.sink.JSONEventTimeBucketAssigner;
import org.flink.example.usercase.streaming.source.LoginEventSource;
import org.flink.example.usercase.streaming.util.ExecutionEnvUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlinkLoginFail {
    private static final Logger LOGGER = LoggerFactory.getLogger(FlinkLoginFail.class);

    public static void main(String[] args) throws Exception {
        ParameterTool parameterTool = ExecutionEnvUtil.createParameterTool(args);
        StreamExecutionEnvironment env = ExecutionEnvUtil.prepare(parameterTool, TimeCharacteristic.ProcessingTime);

        // 在3秒 内重复登录了三次, 则产生告警
        Pattern<LoginEvent, LoginEvent> loginFailPattern = Pattern.<LoginEvent>
                begin("first")
                .where(new IterativeCondition<LoginEvent>() {
                    @Override
                    public boolean filter(LoginEvent loginEvent, Context context) throws Exception {
                        LOGGER.info("first: {}" , loginEvent);
                        return loginEvent.getType().equals("fail");
                    }
                })
                .next("second")
                .where(new IterativeCondition<LoginEvent>() {
                    @Override
                    public boolean filter(LoginEvent loginEvent, Context context) throws Exception {
                        LOGGER.info("second: {}" , loginEvent);
                        return loginEvent.getType().equals("fail");
                    }
                })
                .next("three")
                .where(new IterativeCondition<LoginEvent>() {
                    @Override
                    public boolean filter(LoginEvent loginEvent, Context context) throws Exception {
                        LOGGER.info("three: {}" , loginEvent);
                        return loginEvent.getType().equals("fail");
                    }
                })
                .within(Time.seconds(3));

        DataStream<LoginEvent> loginEventStream = env.addSource(new LoginEventSource(Integer.valueOf(parameterTool.getRequired("source.record.max"))));

        // 根据用户id分组，以便可以锁定用户IP，cep模式匹配
        PatternStream<LoginEvent> patternStream = CEP.pattern(
                loginEventStream.keyBy(LoginEvent::getUserId),
                loginFailPattern);

        // 获取重复登录三次失败的用户信息
        DataStream<String> loginFailDataStream = patternStream.select(
                new PatternSelectFunction<LoginEvent, String>() {
                    @Override
                    public String select(Map<String, List<LoginEvent>> pattern) throws Exception {
                        List<LoginEvent> second = pattern.get("three");
                        StringBuilder builder = new StringBuilder("alter:[");
                        builder.append(second.get(0).getUserId() ).append(",")
                                .append(second.get(0).getIp()).append(",")
                                .append(second.get(0).getType()).append("]");
                        return builder.toString();
                    }
                }
        );

        // 发送告警用户
        loginFailDataStream.addSink(new FlinkKafkaProducer011(parameterTool.getRequired("kafka.brokers"),
                parameterTool.getRequired(PropertiesConstants.KAFKA_SINK_TOPIC_KEY),
                new SimpleStringSchema()));
        env.execute("CEP Login Fail");
    }
}

