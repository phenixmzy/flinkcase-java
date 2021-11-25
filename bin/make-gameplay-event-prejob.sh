#!/usr/bin/env bash
export HADOOP_CONF_DIR=/etc/hadoop/conf
export HBASE_CONF_DIR=/etc/hbase/conf
export HADOOP_HOME=/opt/cloudera/parcels/CDH
export HADOOP_CLASSPATH=/opt/cloudera/parcels/CDH/jars/*
export FLINK_HOME=/opt/cloudera/parcels/FLINK/lib/flink

/usr/local/flink/bin/flink  run -t yarn-per-job --detached \
        -Dflink.yarn.queue=root.default -Dflink.tm.slot=2 \
        -c org.flink.example.usercase.streaming.application.gameplay.MakeGamePlayEventApplication \
        /home/kduser/flinkcase-java/flinkcase-java-0.0.1.jar \
        --gameplay.record.max.num 1000 \
        --gameplay.gid.max.num 100000  \
        --gameplay.uid.max.num 10000000 \
        --gameplay.delay.max.num 5 \
        --gameplay.timelen.max.num 300\
        --kafka.sink.brokers idc-bigdata-185-57.jdy.kd.internal:9092,idc-bigdata-185-58.jdy.kd.internal:9092,idc-bigdata-185-59.jdy.kd.internal:9092 \
        --kafka.sink.topic gameplay \
        --kafka.security.kerberos true \
        --group.id make-gameplay-event-gid-01 \
        --task-num 2

        #-Dsecurity.kerberos.login.contexts=Client,KafkaClient \
        #-Dsecurity.kerberos.login.keytab=/var/data/service/keytabs/services.keytab \
        #-Dsecurity.kerberos.login.principal=flink@JDY.KD.INTERNAL \
        #-Djava.security.auth.login.config=/var/data/service/keytabs/flink_jaas.conf \
        #-Djava.security.krb5.conf=/etc/krb5.conf \