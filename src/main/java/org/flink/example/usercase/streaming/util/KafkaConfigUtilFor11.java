package org.flink.example.usercase.streaming.util;

//import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011;
import org.apache.flink.api.common.io.ratelimiting.FlinkConnectorRateLimiter;
import org.apache.flink.api.common.io.ratelimiting.GuavaFlinkConnectorRateLimiter;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.internals.KafkaTopicPartition;
import org.flink.example.common.constant.PropertiesConstants;

import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 *
 *
 * 		<dependency>
 * 			<groupId>org.apache.flink</groupId>
 * 			<artifactId>flink-connector-filesystem_${scala.binary.version}</artifactId>
 * 			<version>${flink.version}</version>
 * 			<scope>provided</scope>
 * 		</dependency>
 * 		<dependency>
 * 			<groupId>org.apache.flink</groupId>
 * 			<artifactId>flink-connector-kafka-0.11_${scala.binary.version}</artifactId>
 * 			<version>${flink.version}</version>
 * 			<scope>provided</scope>
 * 		</dependency>
 *
 * 		Kafka Connector的支持
 * 		<dependency>
 * 			<groupId>org.apache.flink</groupId>
 * 			<artifactId>flink-sql-connector-kafkakafka-0.11_${scala.binary.version}</artifactId>
 * 			<version>${flink.version}</version>
 * 			<scope>provided</scope>
 * 		</dependency>
 *
 *
 * */

public class KafkaConfigUtilFor11 {
    /*public static DataStreamSource<String> buildSource011(StreamExecutionEnvironment env) {
        ParameterTool parameter = (ParameterTool) env.getConfig().getGlobalJobParameters();
        String topic = parameter.getRequired(PropertiesConstants.KAFKA_SOURCE_TOPIC_KEY);
        Long offsetTime = parameter.getLong(PropertiesConstants.KAFKA_CONSUMER_FROM_TIME_KEY, 0L);
        return buildSource011(env, topic, offsetTime);
    }

    public static DataStreamSource<String> buildSource011(StreamExecutionEnvironment env, String topic, Long offsetTime) {
        ParameterTool parameter = (ParameterTool) env.getConfig().getGlobalJobParameters();
        Properties props = buildkafkaProps(parameter);
        FlinkKafkaConsumer011<String> consumer = new FlinkKafkaConsumer011<String>(topic, new SimpleStringSchema(), props);
        //Consumer限流
        if (props.contains(PropertiesConstants.KAFKA_SOURCE_RATE_LIMIT_KEY)) {
            FlinkConnectorRateLimiter rateLimiter = new GuavaFlinkConnectorRateLimiter();
            rateLimiter.setRate(parameter.getLong(PropertiesConstants.KAFKA_SOURCE_RATE_LIMIT_KEY, PropertiesConstants.DEFAULT_KAFKA_SOURCE_RATE_LIMIT_VALUE));
            consumer.setRateLimiter(rateLimiter);
        }

        if (offsetTime != 0L) { //重置offset到time时刻
            Map<KafkaTopicPartition, Long> partitionOffset = buildOffsetByTime(props, parameter, offsetTime);
            consumer.setStartFromSpecificOffsets(partitionOffset);
        } else if (props.contains("group.id")) {
            consumer.setStartFromGroupOffsets();
        }

        return env.addSource(consumer);
    }

    public static DataStreamSource<String> buildSource011(StreamExecutionEnvironment env, List<String> topicList) {
        ParameterTool parameter = (ParameterTool) env.getConfig().getGlobalJobParameters();
        Long offsetTime = parameter.getLong(PropertiesConstants.KAFKA_CONSUMER_FROM_TIME_KEY, 0L);
        return buildSource011(env, topicList, offsetTime);
    }

    public static DataStreamSource<String> buildSource011(StreamExecutionEnvironment env, List<String> topicList, Long offsetTime) {
        ParameterTool parameter = (ParameterTool) env.getConfig().getGlobalJobParameters();
        Properties props = buildkafkaProps(parameter);
        FlinkKafkaConsumer011<String> consumer = new FlinkKafkaConsumer011<String>(topicList, new SimpleStringSchema(), props);
        FlinkConnectorRateLimiter rateLimiter = new GuavaFlinkConnectorRateLimiter();
        rateLimiter.setRate(parameter.getLong(PropertiesConstants.KAFKA_SOURCE_RATE_LIMIT_KEY, PropertiesConstants.DEFAULT_KAFKA_SOURCE_RATE_LIMIT_VALUE));
        consumer.setRateLimiter(rateLimiter);

        if (offsetTime != 0L) { //重置offset到time时刻
            for (String topic : topicList) {
                Map<KafkaTopicPartition, Long> partitionOffset = buildOffsetByTime(props, topic, offsetTime);
                consumer.setStartFromSpecificOffsets(partitionOffset);
            }
        } else if (props.contains("group.id")) {
            consumer.setStartFromGroupOffsets();
        }
        return env.addSource(consumer);
    }*/
}
