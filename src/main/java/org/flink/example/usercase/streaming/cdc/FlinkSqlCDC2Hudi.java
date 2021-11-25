package org.flink.example.usercase.streaming.cdc;

import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.flink.example.usercase.streaming.util.ExecutionEnvUtil;

public class FlinkSqlCDC2Hudi {
    public static void main(String[] args) throws Exception {
        final ParameterTool parameterTool = ExecutionEnvUtil.createParameterTool(args);
        String dbHost = parameterTool.getRequired("db.host");
        int dbPort = Integer.parseInt(parameterTool.getRequired("db.port"));
        String dbUser = parameterTool.getRequired("db.user");
        String dbPwd = parameterTool.getRequired("db.pwd");
        String dbName = parameterTool.getRequired("db.dbname");
        String tableName = parameterTool.getRequired("db.tableName");

        String hdfsUrl = parameterTool.getRequired("hdfsUrl");

        StreamExecutionEnvironment env = ExecutionEnvUtil.prepare(parameterTool);
        StreamTableEnvironment tableEnv =  StreamTableEnvironment.create(env);
        String ddlCdcTable = "CREATE TABLE flink_cdc2 (\n" +
                " id INT NOT NULL PRIMARY KEY NOT ENFORCED,\n" +
                " pname STRING,\n" +
                " s_state STRING,\n" +
                " s_value INT,\n" +
                " c_value INT,\n" +
                " op_ts TIMESTAMP(3) \n" +
                ") WITH (\n" +
                " 'connector' = 'mysql-cdc',\n" +
                " 'hostname' = '"+ dbHost + "',\n" +
                " 'port' = '" + dbPort + "',\n" +
                " 'username' = '" + dbUser + "',\n" +
                " 'password' = '" + dbPwd +"',\n" +
                " 'database-name' = '" + dbName + "',\n" +
                " 'table-name' = '" + tableName + "'\n" +
                ")";

        String ddlHudiTable = "CREATE TABLE cdc2hudi_mor (\n" +
                " id INT PRIMARY KEY NOT ENFORCED,\n" +
                " pname STRING,\n" +
                " s_state STRING,\n" +
                " s_value INT,\n" +
                " c_value INT,\n" +
                " op_ts TIMESTAMP(3) \n" +
                ") WITH (\n" +
                "    'connector' = 'hudi',\n" +
                "    'table.type' = 'MERGE_ON_READ',\n" +
                "    'path' = 'hdfs://" + hdfsUrl + "/hudi/hudi_users2',\n" +
                "    'read.streaming.enabled' = 'true',\n" +
                "    'read.streaming.check-interval' = '30', \n" +
                "    'changelog.enabled' = 'true', \n" +
                "    'compaction.async.enabled' = 'false', \n" +
                "    'table.dynamic-table-options.enabled' = 'true', \n" +
                "    'write.precombine.field' = 'op_ts'" +
                ")";
        String insertIntoHudi = "insert into cdc2hudi_mor select * from flink_cdc2";
        tableEnv.executeSql(ddlCdcTable);
        tableEnv.executeSql(ddlHudiTable);
        tableEnv.executeSql(insertIntoHudi);
        env.execute("flink-cdc2-hudi");
    }
}
