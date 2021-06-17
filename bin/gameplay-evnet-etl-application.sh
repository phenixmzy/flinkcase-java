#!/bin/sh
export HADOOP_CONF_DIR=/etc/hadoop/conf
export HBASE_CONF_DIR=/etc/hbase/conf
export HADOOP_HOME=/opt/cloudera/parcels/CDH
export HADOOP_CLASSPATH=/opt/cloudera/parcels/CDH/jars/*
export FLINK_HOME=/opt/cloudera/parcels/FLINK/lib/flink

/usr/local/flink/bin/flink  run -t yarn-per-job --detached \
        -Dflink.yarn.queue=root.default -Dflink.tm.slot=2 \
        -Dsecurity.kerberos.login.contexts=Client,KafkaClient \
        -Dsecurity.kerberos.login.keytab=/var/data/service/keytabs/services.keytab \
        -Dsecurity.kerberos.login.principal=flink@JDY.KD.INTERNAL \
        -Djava.security.krb5.conf=/etc/krb5.conf \
        -Djava.security.auth.login.config=/var/data/service/keytabs/flink_jaas.conf \
        -c org.flink.example.usercase.streaming.application.gameplay.MakeGamePlayEventApplication \
        /home/kduser/flinkcase-java/flinkcase-java-0.0.1.jar \
        --kafka.source.topic gameplay \
	    --bootstrap.servers yzj-client-01:9092,yzj-client-02:9092,yzj-client-03:9092 \
	    --kafka.sink.topic gameplay-etl-out \
	    --kafka.sink.brokers yzj-client-01:9092,yzj-client-02:9092,yzj-client-03:9092 \
        --task-num 2