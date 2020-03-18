#!/bin/sh
/usr/local/flink/bin/flink run \
	-m yarn-cluster \
	-ys 1 \
	-yjm 2048m \
	-ytm 3096m \
	-yn 1 \
	-d \
	-c org.flink.example.usercase.streaming.application.stateapp.CPUAlertApp \
	/home/mark/flinkcase-java/flinkcase-java-0.0.1.jar \
	--kafka.source.topic cpu-log-input \
	--kafka.sink.topic cpu-alert-output \
	--bootstrap.servers vmphenix:9092,vmnarsi:9092,vmramon:9092 \
	--group.id cpu-alert-gid \
	--zookeeper.connect vmphenix:2181,vmnarsi:2181,vmramon:2181 \
	--task-num 1
