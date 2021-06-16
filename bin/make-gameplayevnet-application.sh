#!/bin/sh
/usr/local/flink/bin/flink run \
	-m yarn-cluster \
	-ys 3 \
	-yjm 2048m \
	-ytm 3096m \
	-yn 4 \
	-d \
	-c org.flink.example.usercase.streaming.application.gameplay.MakeGamePlayEventApplication \
	/root/flinkcase-java/flinkcase-java-0.0.1.jar \
	--gameplay.record.max.num 10000 \
	--gameplay.gid.max.num 100000  \
	--gameplay.uid.max.num 10000000 \
	--gameplay.delay.max.num 5\
	--gameplay.timelen.max.num 300\
	--kafka.sink.brokers yzj-client-01:9092,yzj-client-02:9092,yzj-client-03:9092 \
	--kafka.sink.topic gameplay \
	--group.id make-gameplayenvent-gid \
	--task-num 3
