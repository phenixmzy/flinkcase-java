package org.flink.example.usercase.streaming.application.ad;

import org.apache.flink.streaming.connectors.kafka.KafkaSerializationSchema;
import org.apache.kafka.clients.producer.ProducerRecord;

import javax.annotation.Nullable;
import java.nio.charset.StandardCharsets;

public class RecordDataKafkaSerialization implements KafkaSerializationSchema<RecordData> {
    @Override
    public ProducerRecord<byte[], byte[]> serialize(RecordData ad, @Nullable Long aLong) {
        return new ProducerRecord(ad.getTopic(), ad.getData().getBytes(StandardCharsets.UTF_8));
    }
}
