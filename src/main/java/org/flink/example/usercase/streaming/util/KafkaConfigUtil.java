package org.flink.example.usercase.streaming.util;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStreamSink;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer011;
import org.apache.flink.streaming.connectors.kafka.internals.KafkaTopicPartition;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndTimestamp;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.flink.example.common.constant.PropertiesConstants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class KafkaConfigUtil {
    public static Properties builderKafkaProps() {
        return buildkafkaProps(ParameterTool.fromSystemProperties());
    }

    public static Properties builderKafkaProducerSideProps(ParameterTool parameterTool) {
        Properties props = parameterTool.getProperties();
        props.put("bootstrap.servers", parameterTool.getRequired(PropertiesConstants.KAFKA_SINK_BROKERS_KEY));
        props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, "120000");
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, "200000");
        props.put(ProducerConfig.LINGER_MS_CONFIG, "100");
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "lz4");
        props.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG,"524288000");
        props.put(ProducerConfig.MAX_BLOCK_MS_CONFIG,"134217728");
        props.put(ProducerConfig.SEND_BUFFER_CONFIG,"134217728");
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG,"134217728");
        return props;
    }

    public static Properties builderKafkaConsumerSideProps(ParameterTool parameterTool) {
        Properties props = parameterTool.getProperties();
        props.put("bootstrap.servers", parameterTool.getRequired(PropertiesConstants.KAFKA_SOURCE_BROKERS_KEY));
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG,"60000");
        props.put(ConsumerConfig.FETCH_MAX_BYTES_CONFIG,"524288000");
        props.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG,"100000");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"latest");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, parameterTool.get(PropertiesConstants.KAFKA_KEY_DESERIALIZER_KEY, PropertiesConstants.DEFAULT_KAFKA_KEY_DESERIALIZER_VALUE));
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, parameterTool.get(PropertiesConstants.KAFKA_VALUE_DESERIALIZER_KEY, PropertiesConstants.DEFAULT_KAFKA_VALUE_DESERIALIZER_VALUE));
        props.put(ConsumerConfig.GROUP_ID_CONFIG,  parameterTool.get(PropertiesConstants.KAFKA_GROUP_ID_KEY, PropertiesConstants.DEFAULT_KAFKA_GROUP_ID_VALUE));
        return props;
    }


    public static Properties buildkafkaProps(ParameterTool parameterTool) {
        Properties props = parameterTool.getProperties();
        props.put("bootstrap.servers", parameterTool.getRequired(PropertiesConstants.KAFKA_BROKERS_KEY));
        props.put("zookeeper.connect", parameterTool.get(PropertiesConstants.KAFKA_ZOOKEEPER_CONNECT_KEY, PropertiesConstants.DEFAULT_KAFKA_ZOOKEEPER_CONNECT_VALUE));
        props.put("group.id", parameterTool.get(PropertiesConstants.KAFKA_GROUP_ID_KEY, PropertiesConstants.DEFAULT_KAFKA_GROUP_ID_VALUE));
        props.put("key.deserializer",  parameterTool.get(PropertiesConstants.KAFKA_KEY_DESERIALIZER_KEY, PropertiesConstants.DEFAULT_KAFKA_KEY_DESERIALIZER_VALUE));
        props.put("value.deserializer", parameterTool.get(PropertiesConstants.KAFKA_VALUE_DESERIALIZER_KEY, PropertiesConstants.DEFAULT_KAFKA_VALUE_DESERIALIZER_VALUE));
        props.put("auto.offset.reset", "latest");

        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG,"60000");
        props.put(ConsumerConfig.FETCH_MAX_BYTES_CONFIG,"524288000");
        props.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG,"100000");
        props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, "120000");
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, "200000");
        props.put(ProducerConfig.LINGER_MS_CONFIG, "100");
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "lz4");
        props.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG,"524288000");
        props.put(ProducerConfig.MAX_BLOCK_MS_CONFIG,"134217728");
        props.put(ProducerConfig.SEND_BUFFER_CONFIG,"134217728");
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG,"134217728");
        return props;
    }

    public static DataStreamSource<String> buildSource(StreamExecutionEnvironment env) {
        ParameterTool parameter = (ParameterTool) env.getConfig().getGlobalJobParameters();
        String topic = parameter.getRequired(PropertiesConstants.KAFKA_SOURCE_TOPIC_KEY);
        Long offsetTime = parameter.getLong(PropertiesConstants.KAFKA_CONSUMER_FROM_TIME_KEY, 0L);
        return buildSource(env, topic, offsetTime);
    }

    public static DataStreamSource<String> buildSource(StreamExecutionEnvironment env, String topic, Long offsetTime) {
        ParameterTool parameter = (ParameterTool)env.getConfig().getGlobalJobParameters();
        Properties props = buildkafkaProps(parameter);
        FlinkKafkaConsumer011<String> consumer = new FlinkKafkaConsumer011<String>(topic, new SimpleStringSchema(), props);
        if (offsetTime != 0L) { //重置offset到time时刻
            Map<KafkaTopicPartition, Long> partitionOffset = buildOffsetByTime(props, parameter, offsetTime);
            consumer.setStartFromSpecificOffsets(partitionOffset);
        }
        return env.addSource(consumer);
    }

    public static DataStreamSource<String> buildSource(StreamExecutionEnvironment env, List<String> topicList, Long offsetTime) {
        ParameterTool parameter = (ParameterTool)env.getConfig().getGlobalJobParameters();
        Properties props = buildkafkaProps(parameter);
        FlinkKafkaConsumer011<String> consumer = new FlinkKafkaConsumer011<String>(topicList, new SimpleStringSchema(), props);
       if (offsetTime != 0L) { //重置offset到time时刻
           for (String topic : topicList) {
               Map<KafkaTopicPartition, Long> partitionOffset = buildOffsetByTime(props, topic, offsetTime);
               consumer.setStartFromSpecificOffsets(partitionOffset);
           }
        }
        return env.addSource(consumer);
    }


    private static Map<KafkaTopicPartition, Long> buildOffsetByTime(Properties props, ParameterTool parameterTool, Long time) {
        String topic = parameterTool.getRequired(PropertiesConstants.KAFKA_SOURCE_TOPIC_KEY);
        return buildOffsetByTime(props, topic, time);
    }

    private static Map<KafkaTopicPartition, Long> buildOffsetByTime(Properties props, String topic, Long time) {
        props.setProperty("group.id", "query_time_" + time);
        KafkaConsumer consumer = new KafkaConsumer(props);
        List<PartitionInfo> partitions = consumer.partitionsFor(topic);
        Map<TopicPartition, Long> partitionInfoLongMap = new HashMap();
        for (PartitionInfo partitionInfo : partitions) {
            partitionInfoLongMap.put(new TopicPartition(partitionInfo.topic(), partitionInfo.partition()), time);
        }
        Map<TopicPartition, OffsetAndTimestamp> offsetResult = consumer.offsetsForTimes(partitionInfoLongMap);
        Map<KafkaTopicPartition, Long> partitionOffset = new HashMap();
        offsetResult.forEach((key, value) -> partitionOffset.put(new KafkaTopicPartition(key.topic(), key.partition()), value.offset()));
        consumer.close();
        return partitionOffset;
    }
}
