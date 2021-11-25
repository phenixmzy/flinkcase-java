package org.flink.example.usercase.streaming.cdc;

import com.ververica.cdc.connectors.postgres.PostgreSQLSource;
import com.ververica.cdc.debezium.DebeziumSourceFunction;
import com.ververica.cdc.debezium.JsonDebeziumDeserializationSchema;
import com.ververica.cdc.debezium.StringDebeziumDeserializationSchema;
import org.apache.flink.api.java.utils.ParameterTool;
import org.flink.example.usercase.streaming.util.ExecutionEnvUtil;

public class PGCDC2 {
    public static void main(String[] args) throws Exception {
        final ParameterTool parameterTool = ExecutionEnvUtil.createParameterTool(args);

        String dbHost = parameterTool.getRequired("db.host");
        int dbPort = Integer.parseInt(parameterTool.getRequired("db.port"));
        String dbUser = parameterTool.getRequired("db.user");
        String dbPwd = parameterTool.getRequired("db.pwd");
        String dbList = parameterTool.getRequired("db.dblist");
        String tableList = parameterTool.getRequired("db.tablelist");
        String slotName = parameterTool.getRequired("db.slot.name");
        String pgPluginName = parameterTool.getRequired("pg.plugin.name");

        DebeziumSourceFunction<String> pg2 = PostgreSQLSource.<String>builder()
                .hostname(dbHost) //172.20.185.57
                .port(dbPort) //5432
                .database("shard1") //shard1
                .username(dbUser) //postgres
                .password(dbPwd) //Middle@2020
                .slotName(slotName) //shard3
                .decodingPluginName(pgPluginName) //pgoutput
                .deserializer(new JsonDebeziumDeserializationSchema())
                .build();
    }
}
