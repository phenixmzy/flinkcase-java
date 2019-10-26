#!/bin/sh
/usr/local/flink/bin/flink run \
        -m yarn-cluster \
        -ys 3 \
        -yjm 2048m \
        -ytm 3096m \
        -yn 4 \
        -d \
        -c org.flink.example.usercase.streaming.application.cep.FlinkLoginFail \
        /root/flinkcase-java/flinkcase-java-0.0.1.jar \
        --source.record.max 10 \
        --kafka.sink.topic test \
        --bootstrap.servers yzj-client-01:9092,yzj-client-02:9092,yzj-client-03:9092 \
        --group.id test-cep-gid \
        --zookeeper.connect yzj-client-01:2181,yzj-client-02:2181,yzj-client-03:2181/kafka \
        --hdfs.sink.path hdfs://hqcluster/flink-out/gameplay \
        --task-num 3