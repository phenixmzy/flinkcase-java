package org.flink.example.usercase.streaming.util;

import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;

import java.util.Properties;

public class KafakPropertiesUtil {
    public static Properties getCommonProperties(ParameterTool params) {
        Properties kafkaProperties = new Properties();
        kafkaProperties.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG,"60000");
        kafkaProperties.put(ConsumerConfig.FETCH_MAX_BYTES_CONFIG,"524288000");
        kafkaProperties.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG,"100000");

        kafkaProperties.put(ConsumerConfig.GROUP_ID_CONFIG, params.getRequired("group.id"));

        // config producer
        kafkaProperties.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, "120000");
        kafkaProperties.put(ProducerConfig.BATCH_SIZE_CONFIG,"200000");
        kafkaProperties.put(ProducerConfig.LINGER_MS_CONFIG, "100");
        kafkaProperties.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "lz4");
        kafkaProperties.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG,"524288000");
        kafkaProperties.put(ProducerConfig.MAX_BLOCK_MS_CONFIG,"134217728");
        kafkaProperties.put(ProducerConfig.SEND_BUFFER_CONFIG,"134217728");
        kafkaProperties.put(ProducerConfig.BUFFER_MEMORY_CONFIG,"134217728");

        kafkaProperties.put("bootstrap.servers", params.getRequired("bootstrap.servers"));
        return kafkaProperties;
    }
}
