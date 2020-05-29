package org.flink.example;

import com.alibaba.fastjson.JSONObject;
import org.flink.example.common.constant.PropertiesConstants;
import org.flink.example.usercase.streaming.application.configcenter.ConfigCenterManager;
import org.flink.example.usercase.streaming.application.configcenter.ConfigValue;

import java.io.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

public class Tests {
    private static HashMap<String, ConfigValue> appConfigs = new HashMap<String, ConfigValue>();
    private static HashMap<String, ArrayList<String>> tableFields = new HashMap<String, ArrayList<String>>();

    public static void main(String[] args) {

        /*
        // 自定义格式化:
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        System.out.println(dtf.format(LocalDateTime.now()));

        // 用自定义格式解析:
        LocalDateTime dt2 = LocalDateTime.parse("2019/11/30 15:16:17", dtf);
        System.out.println(dt2.plusDays(5).minusHours(5).withHour(3));
       */
        String nameservices = "canal.cl.ad,canal.cl.app";
        String[] serviceNames = nameservices.split(",");
        ConfigCenterManager.init();
        for (String ns : serviceNames) {
            String table = ConfigCenterManager.getConfigValues(ns + ".table");
            String fields = ConfigCenterManager.getConfigValues(ns + ".fields");
            String sinkTopic = ConfigCenterManager.getConfigValues(ns + ".kafka.sink.topic");
            System.out.println("table:" + table + ", fields:" + fields + ", sinkTopic:" + sinkTopic);

            ConfigValue cv = new ConfigValue(table);
            cv.putConfigs("kafka.sink.topic", sinkTopic);
            cv.putConfigs("table.fields", fields);
            appConfigs.put(table, cv);
            tableFields.put(table, getConfigFields(fields));
        }
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader("D:\\canal.data"));
            String eventJsonStr = null;
            while ((eventJsonStr = in.readLine()) != null) {
                JSONObject json = JSONObject.parseObject(eventJsonStr);
                String table = json.getString(PropertiesConstants.CANAL_JSON_DATA_TABLE_KEY);
                String sinkTopic = appConfigs.get(table).getConfig("kafka.sink.topic");
;                System.out.println(table + " " + sinkTopic);
            }

        } catch (Exception ex) {

        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static ArrayList<String> getConfigFields(String config_fields) {
        ArrayList<String> fieldList = new ArrayList<String>();
        String[] fields = config_fields.split(",");
        for (String field : fields) {
            fieldList.add(field);
        }
        return fieldList;
    }

}
