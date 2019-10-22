#!/bin/sh
/usr/local/flink/bin/flink run \
	-m yarn-cluster \
	-ys 3 \
	-yjm 2048m \
	-ytm 3096m \
	-yn 4 \
	-d \
	-c org.flink.example.usercase.streaming.application.gameplay.GamePlayJoinWindowApplication \
	/root/flinkcase-java/flinkcase-java-0.0.1.jar \
	--gameplay.record.max.num 100000 \
	--gameplay.gid.max.num 1000  \
	--gameplay.uid.max.num 10000000 \
	--gameplay.delay.max.num 5\
	--gameplay.timelen.max.num 300\
        --browse.record.max.num 200000 \
        --browse.gid.max.num 1000  \
        --browse.uid.max.num 10000000 \
        --browse.delay.max.num 3\
        --browse.timelen.max.num 300\
	--bootstrap.servers yzj-client-01:9092,yzj-client-02:9092,yzj-client-03:9092 \
	--kafka.sink.topic gameplay \
	--flink.window.max.outoforderness 1 \
	--flink.window.size 60 \
	--group.id test-java-gameplay-log-input-gid \
	--task-num 3
