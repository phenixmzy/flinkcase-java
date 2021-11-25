#!/usr/bin/env bash
export HADOOP_CONF_DIR=/etc/hadoop/conf
export HBASE_CONF_DIR=/etc/hbase/conf
export HADOOP_HOME=/opt/cloudera/parcels/CDH
export HADOOP_CLASSPATH=/opt/cloudera/parcels/CDH/jars/*
export FLINK_HOME=/opt/cloudera/parcels/FLINK/lib/flink

/usr/local/flink/bin/flink  run -t yarn-per-job --detached \
        -Dflink.yarn.queue=root.default -Dflink.tm.slot=2 \
        -c org.flink.example.usercase.streaming.cdc.MySQLCDC2 \
        /home/kduser/flinkcase-java/flinkcase-java-0.0.1.jar \
        --db.host 172.20.187.22 \
        --db.port 3306  \
        --db.user root \
        --db.pwd Middle@2020 \
        --db.dblist test_cdc\
        --db.tablelist t_cdc_1\
        --kafka.sink.brokers idc-bigdata-185-57.jdy.kd.internal:9092,idc-bigdata-185-58.jdy.kd.internal:9092,idc-bigdata-185-59.jdy.kd.internal:9092 \
        --kafka.sink.topic test-flink-ccd \
        --kafka.security.kerberos true \
        --group.id flink-cdc2-gid-01 \
        --task-num 2

        #-Dsecurity.kerberos.login.contexts=Client,KafkaClient \
        #-Dsecurity.kerberos.login.keytab=/var/data/service/keytabs/services.keytab \
        #-Dsecurity.kerberos.login.principal=flink@JDY.KD.INTERNAL \
        #-Djava.security.auth.login.config=/var/data/service/keytabs/flink_jaas.conf \
        #-Djava.security.krb5.conf=/etc/krb5.conf \