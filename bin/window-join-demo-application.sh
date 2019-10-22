#!/bin/sh
/usr/local/flink/bin/flink run \
	-m yarn-cluster \
	-ys 3 \
	-yjm 2048m \
	-ytm 3096m \
	-yn 4 \
	-d \
	-c org.flink.example.usercase.streaming.application.demo.FlinkTumblingWindowsLeftJoinDemo \
	/root/flinkcase-java/flinkcase-java-0.0.1.jar \
        --demo.result.path hdfs://yzj-nn-01:9000/flink-out/demo-leftjoin-1 
