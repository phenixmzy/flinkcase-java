package org.flink.example;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.alibaba.fastjson.JSONObject;
import org.flink.example.common.constant.PropertiesConstants;
import org.flink.example.usercase.streaming.application.ad.configcenter.ConfigCenterManager;
import org.flink.example.usercase.streaming.application.ad.configcenter.ConfigValue;
import org.flink.example.usercase.streaming.application.ad.configcenter.FileConfigCenterManager;

import java.io.*;
class T {
    public static int count = 2;
    public static T t  = new T();
    public T () {
        count++;
        System.out.println("T():" + count);
    }
}

public class Tests {
    //private static HashMap<String, ConfigValue> appConfigs = new HashMap<String, ConfigValue>();
    //private static HashMap<String, ArrayList<String>> tableFields = new HashMap<String, ArrayList<String>>();

    public static void main(String[] args) throws Exception {
        System.out.println(T.count);
        T.count++;
        System.out.println(T.count);
        T.count++;
        System.out.println(T.count);
        //printDate("2020-06-17" ,365);
        /*
        // 自定义格式化:
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        System.out.println(dtf.format(LocalDateTime.now()));

        // 用自定义格式解析:
        LocalDateTime dt2 = LocalDateTime.parse("2019/11/30 15:16:17", dtf);
        System.out.println(dt2.plusDays(5).minusHours(5).withHour(3));
       */
        //testLoad();
    }

//    private static ArrayList<String> getConfigFields(String config_fields) {
//        ArrayList<String> fieldList = new ArrayList<String>();
//        String[] fields = config_fields.split(",");
//        for (String field : fields) {
//            fieldList.add(field);
//        }
//        return fieldList;
//    }
//
//    private static void testConfig() {
//        String nameservices = "canal.chuangliang_ad.toutiao_campaign_info";
//        String[] serviceNames = nameservices.split(",");
//        ConfigCenterManager.init();
//        for (String ns : serviceNames) {
//            String table = ConfigCenterManager.getConfigValues(ns + ".table");
//            String fields = ConfigCenterManager.getConfigValues(ns + ".fields");
//            String sinkTopic = ConfigCenterManager.getConfigValues(ns + ".kafka.sink.topic");
//            System.out.println("table:" + table + ", fields:" + fields + ", sinkTopic:" + sinkTopic);
//
//            ConfigValue cv = new ConfigValue(table);
//            cv.putConfigs("kafka.sink.topic", sinkTopic);
//            cv.putConfigs("table.fields", fields);
//            appConfigs.put(table, cv);
//            tableFields.put(table, getConfigFields(fields));
//        }
//        BufferedReader in = null;
//        try {
//            in = new BufferedReader(new FileReader("D:\\canal.data"));
//            String eventJsonStr = null;
//            while ((eventJsonStr = in.readLine()) != null) {
//                JSONObject json = JSONObject.parseObject(eventJsonStr);
//                String table = json.getString(PropertiesConstants.CANAL_JSON_DATA_TABLE_KEY);
//                String sinkTopic = appConfigs.get(table).getConfig("kafka.sink.topic");
//                ;                System.out.println(table + " " + sinkTopic);
//            }
//        } catch (Exception ex) {
//
//        } finally {
//            try {
//                if (in != null) {
//                    in.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    private static void testLoad() {
//        String[] serviceNames = {"",""};
//        FileConfigCenterManager.init();
//        for (String ns : serviceNames) {
//            String docName = FileConfigCenterManager.getConfigValues(ns + ".doc_name");
//            String sourceTopic = FileConfigCenterManager.getConfigValues(ns + ".kafka.source.topic");
//            String sinkTopic = FileConfigCenterManager.getConfigValues(ns + ".kafka.sink.topic");
//            String fields = FileConfigCenterManager.getConfigValues(ns + ".fields");
//            ConfigValue cv = new ConfigValue(docName);
//            cv.putConfigs("kafka.source.topic", sourceTopic);
//            cv.putConfigs("kafka.sink.topic", sinkTopic);
//            cv.putConfigs("fields", fields);
//            appConfigs.put(docName, cv);
//            tableFields.put(docName, getConfigFields(fields));
//        }
//    }
//
//    private static void printDate(String specifiedDay, int count) {
//        Calendar calendar = new GregorianCalendar();
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//        SimpleDateFormat dfNum = new SimpleDateFormat("yyyyMMdd");
//        String startDay = specifiedDay;
//        for (int i = 0; i< count; i++) {
//            Date date = null;//取时间
//            try {
//                date = df.parse(startDay);
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//            calendar.setTime(date);
//            calendar.add(calendar.DATE,1);
//            String lns = df.format(calendar.getTime());
//            calendar.add(calendar.DATE,-1);
//            String pn = dfNum.format(calendar.getTime());
//            System.out.println(String.format("PARTITION p%s VALUES LESS THAN ('%s'),", pn, lns));
//            startDay = lns;
//        }
//
//
//
//
//    }
}
