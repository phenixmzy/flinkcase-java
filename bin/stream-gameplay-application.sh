#!/bin/sh
/usr/local/flink/bin/flink run \
        -m yarn-cluster \
        -ynm flink-gameplay-application \
        -yqu root.statis \
        -d \
        -c org.flink.example.usercase.streaming.application.gameplay.GamePlayStreamingApplication \
        /home/mark/flinkcase-java/jar/flinkcase-java-0.0.1.jar \
        --kafka.source.topic source-gameplay \
        --kafka.sink.topic test  \
        --bootstrap.servers vmphenix:9092,vmnarsi:9092,vmramon:9092 \
        --group.id test-source-gameplay-gid \
        --zookeeper.connect vmphenix:2181,vmnarsi:2181,vmramon:2181

