package org.flink.example.usercase.streaming.cdc;

import com.alibaba.fastjson.JSONObject;
import com.ververica.cdc.connectors.postgres.PostgreSQLSource;
import com.ververica.cdc.debezium.DebeziumSourceFunction;
import com.ververica.cdc.debezium.JsonDebeziumDeserializationSchema;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.flink.example.usercase.streaming.util.ExecutionEnvUtil;
import scala.Tuple2;

public class PGCDC2 {
    public static void main(String[] args) throws Exception {

        final ParameterTool parameterTool = ExecutionEnvUtil.createParameterTool(args);
        /*
        String dbHost = parameterTool.getRequired("db.host");
        int dbPort = Integer.parseInt(parameterTool.getRequired("db.port"));
        String dbUser = parameterTool.getRequired("db.user");
        String dbPwd = parameterTool.getRequired("db.pwd");
        String dbList = parameterTool.getRequired("db.dblist");
        String tableList = parameterTool.getRequired("db.tablelist");
        String slotName = parameterTool.getRequired("db.slot.name");
        String pgPluginName = parameterTool.getRequired("pg.plugin.name");
        */

        DebeziumSourceFunction<String> pg1 = PostgreSQLSource.<String>builder()
                .hostname("xxx") //
                .port(5432) //5432
                .database("shard1") //shard1
                .username("postgres") //postgres
                .password("xxx") //xxx
                .slotName("shard3") //shard3
                .decodingPluginName("pgoutput") //pgoutput
                .deserializer(new JsonDebeziumDeserializationSchema())
                .build();

        DebeziumSourceFunction<String> pg2 = PostgreSQLSource.<String>builder()
                .hostname("xxx") //
                .port(5432) //5432
                .database("shard1") //shard1
                .username("postgres") //postgres
                .password("xxx") //xxx
                .slotName("shard3") //shard3
                .decodingPluginName("pgoutput") //pgoutput
                .deserializer(new JsonDebeziumDeserializationSchema())
                .build();

        DebeziumSourceFunction<String> pg3 = PostgreSQLSource.<String>builder()
                .hostname("xxx") //
                .port(5432) //5432
                .database("shard1") //shard1
                .username("postgres") //postgres
                .password("xxx") //xxx
                .slotName("shard3") //shard3
                .decodingPluginName("pgoutput") //pgoutput
                .deserializer(new JsonDebeziumDeserializationSchema())
                .build();

        StreamExecutionEnvironment env = ExecutionEnvUtil.prepare(parameterTool);
        env.addSource(pg1).map(cdcJsonStr -> {
            JSONObject json = JSONObject.parseObject(cdcJsonStr);
            String tableName = json.getString("table_name");
            String schemaName = json.getString("schema_name");
            String dataBaseName = json.getString("database_name");
            StringBuilder builder = new StringBuilder(dataBaseName).append(":").append(schemaName).append(":").append(tableName);
            String key = builder.toString();
            return new Tuple2(key, cdcJsonStr);
        });
    }
}
