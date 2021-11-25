package org.flink.example.usercase.streaming.cdc;

import com.ververica.cdc.connectors.mysql.table.StartupMode;
import com.ververica.cdc.debezium.JsonDebeziumDeserializationSchema;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import com.ververica.cdc.debezium.StringDebeziumDeserializationSchema;
import com.ververica.cdc.connectors.mysql.MySqlSource;
import org.flink.example.usercase.streaming.util.ExecutionEnvUtil;
import org.flink.example.usercase.streaming.util.KafkaConfigUtil;

public class MySQLCDC2 {
    public static void main(String[] args) throws Exception {
        final ParameterTool parameterTool = ExecutionEnvUtil.createParameterTool(args);

        String dbHost = parameterTool.getRequired("db.host");
        int dbPort = Integer.parseInt(parameterTool.getRequired("db.port"));
        String dbUser = parameterTool.getRequired("db.user");
        String dbPwd = parameterTool.getRequired("db.pwd");
        String dbList = parameterTool.getRequired("db.dblist");
        String tableList = parameterTool.getRequired("db.tablelist");

        SourceFunction<String> sourceFunction = MySqlSource.<String>builder()
                .hostname(dbHost)
                .port(dbPort)
                .databaseList(dbList)
                .username(dbUser)
                .password(dbPwd)
                .deserializer(new JsonDebeziumDeserializationSchema())// converts SourceRecord to String
                .build();

        StreamExecutionEnvironment env = ExecutionEnvUtil.prepare(parameterTool);
        env.addSource(sourceFunction).addSink(KafkaConfigUtil.buildSink(parameterTool));
        env.execute("flink-mysqlcdc2");
    }
}