package org.flink.example.common.constant;

public class PropertiesConstants {
    public static final String NAME_SERVICES_KEY = "name.services";

    // kafka config
    public static final String KAFKA_BROKERS_KEY = "kafka.brokers";
    public static final String DEFAULT_KAFKA_BROKERS_VALUE = "localhost:9092";

    public static final String KAFKA_SOURCE_BROKERS_KEY = "kafka.source.brokers";
    public static final String KAFKA_SINK_BROKERS_KEY = "kafka.sink.brokers";

    public static final String KAFKA_ZOOKEEPER_CONNECT_KEY = "kafka.zookeeper.connect";
    public static final String DEFAULT_KAFKA_ZOOKEEPER_CONNECT_VALUE = "localhost:2181";

    public static final String KAFKA_GROUP_ID_KEY = "kafka.group.id";
    public static final String DEFAULT_KAFKA_GROUP_ID_VALUE = "phenix-bigdata";

    public static final String KAFKA_KEY_SERIALIZER_KEY = "kafka.key.serializer";
    public static final String DEFAULT_KAFKA_KEY_SERIALIZER_VALUE = "org.apache.kafka.common.serialization.StringSerializer";

    public static final String KAFKA_VALUE_SERIALIZER_KEY = "kafka.value.serializer";
    public static final String DEFAULT_KAFKA_VALUE_SERIALIZER_VALUE = "org.apache.kafka.common.serialization.StringSerializer";

    public static final String KAFKA_KEY_DESERIALIZER_KEY = "kafka.key.deserializer";
    public static final String DEFAULT_KAFKA_KEY_DESERIALIZER_VALUE = "org.apache.kafka.common.serialization.StringDeserializer";

    public static final String KAFKA_VALUE_DESERIALIZER_KEY = "kafka.key.deserializer";
    public static final String DEFAULT_KAFKA_VALUE_DESERIALIZER_VALUE = "org.apache.kafka.common.serialization.StringDeserializer";

    public static final String KAFKA_CONSUMER_FROM_TIME_KEY = "consumer.from.time";

    public static final String KAFKA_SOURCE_TOPIC_KEY = "kafka.source.topic";
    public static final String KAFKA_SINK_TOPIC_KEY = "kafka.sink.topic";
    // flink config
    public static final String FLINK_STREAM_PARALLELISM_KEY = "flink.stream.parallelism";
    public static final String FLINK_STREAM_SINK_PARALLELISM_KEY = "flink.stream.sink.parallelism";
    public static final String FLINK_STREAM_DEFAULT_PARALLELISM_KEY = "flink.stream.default.parallelism";
    public static final String FLINK_STREAM_CHECKPOINT_ENABLE_KEY = "flink.stream.checkpoint.enable";
    public static final String FLINK_STREAM_CHECKPOINT_INTERVAL_KEY = "flink.stream.checkpoint.interval";
    public static final String FLINK_STREAM_CHECKPOINT_TIMEOUT_MS_KEY = "flink.stream.checkpoint.timeout.ms";

    public static final String PROPERTIES_FILE_NAME = "/application.properties";

    public static final String FLINK_WINDOW_SIZE = "flink.window.size";
    public static final String FLINK_WINDOW_SLIDE ="flink.window.slide";
    public static final String FLINK_WINDOW_MAX_OUTOFORDERNESS_MS = "flink.window.max.outoforderness.ms";

    // es config
    public static final String ELASTICSEARCH_BULK_FLUSH_MAX_ACTIONS = "elasticsearch.bulk.flush.max.actions";
    public static final String ELASTICSEARCH_HOSTS = "elasticsearch.hosts";

    // mysql config
    public static final String MYSQL_DATABASE = "mysql.database";
    public static final String MYSQL_HOST = "mysql.host";
    public static final String MYSQL_PASSWORD = "mysql.password";
    public static final String MYSQL_PORT = "mysql.port";
    public static final String MYSQL_USERNAME = "mysql.username";
}
