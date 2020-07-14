package org.flink.example.usercase.streaming.util;

import org.apache.flink.api.common.io.ratelimiting.FlinkConnectorRateLimiter;
import org.apache.flink.api.common.io.ratelimiting.GuavaFlinkConnectorRateLimiter;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.flink.streaming.connectors.kafka.internals.KafkaTopicPartition;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndTimestamp;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.flink.example.common.constant.PropertiesConstants;
import org.flink.example.usercase.streaming.application.ad.RecordData;
import org.flink.example.usercase.streaming.application.ad.RecordDataKafkaSerialization;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class KafkaConfigUtil {

    public static Properties builderKafkaProps() {
        return buildkafkaProps(ParameterTool.fromSystemProperties());
    }

    public static Properties builderKafkaConsumerSideProps(ParameterTool parameterTool) {
        Properties props = parameterTool.getProperties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, parameterTool.getRequired(PropertiesConstants.KAFKA_SOURCE_BROKERS_KEY));
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "60000");
        props.put(ConsumerConfig.FETCH_MAX_BYTES_CONFIG, "524288000");
        props.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, "100000");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, parameterTool.get(PropertiesConstants.KAFKA_KEY_DESERIALIZER_KEY, PropertiesConstants.DEFAULT_KAFKA_KEY_DESERIALIZER_VALUE));
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, parameterTool.get(PropertiesConstants.KAFKA_VALUE_DESERIALIZER_KEY, PropertiesConstants.DEFAULT_KAFKA_VALUE_DESERIALIZER_VALUE));
        props.put(ConsumerConfig.GROUP_ID_CONFIG, parameterTool.get(PropertiesConstants.KAFKA_GROUP_ID_KEY, PropertiesConstants.DEFAULT_KAFKA_GROUP_ID_VALUE));
        return props;
    }

    public static Properties builderKafkaProducerSideProps(ParameterTool parameterTool) {
        Properties props = parameterTool.getProperties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, parameterTool.getRequired(PropertiesConstants.KAFKA_SINK_BROKERS_KEY));
        props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, parameterTool.get(PropertiesConstants.KAFKA_REQUEST_TIMEOUT_MS_KEY, PropertiesConstants.DEFAULT_KAFKA_REQUEST_TIMEOUT_MS_VALUE));
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, parameterTool.getInt(PropertiesConstants.KAFKA_BATCH_SIZE_KEY, PropertiesConstants.DEFAULT_DKAFKA_BATCH_SIZE_VALUE));
        props.put(ProducerConfig.LINGER_MS_CONFIG, "100");
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "lz4");
        props.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, "524288000");
        props.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, "134217728");
        props.put(ProducerConfig.SEND_BUFFER_CONFIG, "134217728");
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, "134217728");
        props.put(ProducerConfig.RETRIES_CONFIG, parameterTool.get(PropertiesConstants.KAFKA_RETRIES_CONFIG_KEY, PropertiesConstants.DEFAULT_KAFKA_RETRIES_CONFIG_VALUE));
        return props;
    }

    public static Properties builderKafkaProducerSidePropsForEXACTLYONCE(ParameterTool parameterTool) {
        Properties props = builderKafkaProducerSideProps(parameterTool);
        //  kafka producer 在使用EXACTLY_ONCE的时候需要增加一些配置（用到了事务）
        props.put(ProducerConfig.TRANSACTION_TIMEOUT_CONFIG, parameterTool.get(PropertiesConstants.KAFKA_TRANSACTION_TIMEOUT_CONFIG_KEY, PropertiesConstants.DEFAULT_KAFKA_TRANSACTION_TIMEOUT_VALUE));
        props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, "1");
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
        return props;
    }


    public static Properties buildkafkaProps(ParameterTool parameterTool) {
        Properties props = parameterTool.getProperties();
        props.put("zookeeper.connect", parameterTool.get(PropertiesConstants.KAFKA_ZOOKEEPER_CONNECT_KEY, PropertiesConstants.DEFAULT_KAFKA_ZOOKEEPER_CONNECT_VALUE));
        props.put("group.id", parameterTool.get(PropertiesConstants.KAFKA_GROUP_ID_KEY, PropertiesConstants.DEFAULT_KAFKA_GROUP_ID_VALUE));
        props.put("key.deserializer", parameterTool.get(PropertiesConstants.KAFKA_KEY_DESERIALIZER_KEY, PropertiesConstants.DEFAULT_KAFKA_KEY_DESERIALIZER_VALUE));
        props.put("value.deserializer", parameterTool.get(PropertiesConstants.KAFKA_VALUE_DESERIALIZER_KEY, PropertiesConstants.DEFAULT_KAFKA_VALUE_DESERIALIZER_VALUE));
        props.put("auto.offset.reset", "latest");

        props.put("bootstrap.servers", parameterTool.getRequired(PropertiesConstants.KAFKA_BROKERS_KEY));
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "60000");
        props.put(ConsumerConfig.FETCH_MAX_BYTES_CONFIG, "524288000");
        props.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, "100000");
        props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, "120000");
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, "200000");
        props.put(ProducerConfig.LINGER_MS_CONFIG, "100");
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "lz4");
        props.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, "524288000");
        props.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, "134217728");
        props.put(ProducerConfig.SEND_BUFFER_CONFIG, "134217728");
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, "134217728");
        return props;
    }



    public static DataStreamSource<String> buildSource011(StreamExecutionEnvironment env) {
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
    }

    public static DataStreamSource<String> buildSource(StreamExecutionEnvironment env) {
        ParameterTool parameter = (ParameterTool) env.getConfig().getGlobalJobParameters();
        String topic = parameter.getRequired(PropertiesConstants.KAFKA_SOURCE_TOPIC_KEY);
        Long offsetTime = parameter.getLong(PropertiesConstants.KAFKA_CONSUMER_FROM_TIME_KEY, 0L);
        return buildSource(env, topic, offsetTime);
    }

    public static DataStreamSource<String> buildSource(StreamExecutionEnvironment env, String topic, Long offsetTime) {
        ParameterTool parameter = (ParameterTool) env.getConfig().getGlobalJobParameters();
        Properties props = buildkafkaProps(parameter);
        FlinkKafkaConsumer<String> consumer = new FlinkKafkaConsumer<String>(topic, new SimpleStringSchema(), props);
        if (offsetTime != 0L) { //重置offset到time时刻
            Map<KafkaTopicPartition, Long> partitionOffset = buildOffsetByTime(props, parameter, offsetTime);
            consumer.setStartFromSpecificOffsets(partitionOffset);
        } else if (props.contains("group.id")) {
            consumer.setStartFromGroupOffsets();
        }

        return env.addSource(consumer);
    }

    public static DataStreamSource<String> buildSource(StreamExecutionEnvironment env, List<String> topicList) {
        ParameterTool parameter = (ParameterTool) env.getConfig().getGlobalJobParameters();
        Long offsetTime = parameter.getLong(PropertiesConstants.KAFKA_CONSUMER_FROM_TIME_KEY, 0L);
        return buildSource(env, topicList, offsetTime);
    }

    public static DataStreamSource<String> buildSource(StreamExecutionEnvironment env, List<String> topicList, Long offsetTime) {
        ParameterTool parameter = (ParameterTool) env.getConfig().getGlobalJobParameters();
        Properties props = buildkafkaProps(parameter);
        FlinkKafkaConsumer<String> consumer = new FlinkKafkaConsumer<String>(topicList, new SimpleStringSchema(), props);

        if (offsetTime != 0L) { //重置offset到time时刻
            for (String topic : topicList) {
                Map<KafkaTopicPartition, Long> partitionOffset = buildOffsetByTime(props, topic, offsetTime);
                consumer.setStartFromSpecificOffsets(partitionOffset);
            }
        } else if (props.contains("group.id")) {
            consumer.setStartFromGroupOffsets();
        }
        return env.addSource(consumer);
    }

    public static FlinkKafkaProducer buildSink(ParameterTool parameterTool) {
        String sinkBrokers = parameterTool.get(PropertiesConstants.KAFKA_SINK_BROKERS_KEY, parameterTool.get(PropertiesConstants.KAFKA_BROKERS_KEY));
        String sinkTopic = parameterTool.getRequired(PropertiesConstants.KAFKA_SINK_TOPIC_KEY);
        return new FlinkKafkaProducer(sinkBrokers, sinkTopic, new SimpleStringSchema());
    }

    /**
     * 注意,如果使用到kafka事务-TRANSACTION_TIMEOUT_CONFIG,则需要在addSink() 后设置uid()
     * eg: addSink(buildSinkRecordDataForEXACTLYONCE(parameterTool)).uid("gameplay-sink")
     * */
    public static FlinkKafkaProducer buildSinkRecordDataForEXACTLYONCE(ParameterTool parameterTool) {
        Properties producerProps = builderKafkaProducerSideProps(parameterTool);
        FlinkKafkaProducer producer = new FlinkKafkaProducer<RecordData>("", new RecordDataKafkaSerialization(),
                producerProps,
                FlinkKafkaProducer.Semantic.EXACTLY_ONCE);
        producer.setLogFailuresOnly(false);
        return producer;
    }


    private static Map<KafkaTopicPartition, Long> buildOffsetByTime(Properties props, ParameterTool parameterTool, Long time) {
        String topic = parameterTool.getRequired(PropertiesConstants.KAFKA_SOURCE_TOPIC_KEY);
        return buildOffsetByTime(props, topic, time);
    }

    private static Map<KafkaTopicPartition, Long> buildOffsetByTime(Properties props, String topic, Long time) {
        //props.setProperty("group.id", "query_time_" + time);
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