package org.flink.example.usercase.streaming.application.ad;

import org.apache.flink.streaming.connectors.kafka.KafkaSerializationSchema;
import org.apache.kafka.clients.producer.ProducerRecord;

import javax.annotation.Nullable;
import java.nio.charset.StandardCharsets;


public class ADKafkaSerialization implements KafkaSerializationSchema<ADBean> {
    @Override
    public ProducerRecord<byte[], byte[]> serialize(ADBean ad, @Nullable Long aLong) {
        return new ProducerRecord(ad.getNameService(), ad.getDataJson().getBytes(StandardCharsets.UTF_8));
    }
}
