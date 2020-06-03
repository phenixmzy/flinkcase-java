package org.flink.example.usercase.streaming.application.configcenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class FileConfigCenterManager {
    public final static String NULL_STR = "NULL";
    public final static String SPLIT_FLAG = "\u0001";

    public static HashMap<String, String> CONFIG_VALUES = new HashMap<String, String>();

    public static void init() {
        CONFIG_VALUES.put("file.cl_ad.toutiao_ad_report_data", "file.cl_ad.toutiao_ad_report_data.doc_name,file.cl_ad.toutiao_ad_report_data.fields,file.cl_ad.toutiao_ad_report_data.kafka.sink.topic,file.cl_ad.toutiao_ad_report_data.kafka.source.topic");
        CONFIG_VALUES.put("file.cl_ad.toutiao_ad_report_data.doc_name", "toutiao_ad_report_data");
        CONFIG_VALUES.put("file.cl_ad.toutiao_ad_report_data.fields", "advertiser_id,ad_id,stat_datetime,months,days,hours,campaign_id,cost,show_count,avg_show_cost,click,avg_click_cost,ctr,convert_count,convert_cost,convert_rate,deep_convert,deep_convert_cost,deep_convert_rate,attribution_convert,attribution_convert_cost,attribution_deep_convert,attribution_deep_convert_cost,download_start,download_start_cost,download_start_rate,download_finish,download_finish_cost,download_finish_rate,click_install,install_finish,install_finish_cost,install_finish_rate,active,active_cost,active_rate,register,active_register_cost,active_register_rate,next_day_open,next_day_open_cost,next_day_open_rate,attribution_next_day_open_cnt,attribution_next_day_open_cost,attribution_next_day_open_rate,game_addiction,game_addiction_cost,game_addiction_rate,pay_count,active_pay_cost,active_pay_rate,loan_completion,loan_completion_cost,loan_completion_rate,pre_loan_credit,pre_loan_credit_cost,loan_credit,loan_credit_cost,loan_credit_rate,in_app_uv,in_app_detail_uv,in_app_cart,in_app_pay,in_app_order,phone,form,map_search,button,view_count,download,qq,lottery,vote,message,redirect,shopping,consult,wechat,phone_confirm,phone_connect,consult_effective,coupon,coupon_single_page,total_play,valid_play,valid_play_cost,valid_play_rate,play_25_feed_break,play_50_feed_break,play_75_feed_break,play_100_feed_break,average_play_time_per_play,play_over_rate,wifi_play_rate,wifi_play,play_duration_sum,advanced_creative_phone_click,advanced_creative_counsel_click,advanced_creative_form_click,advanced_creative_coupon_addition,share,comment,like_count,follow,home_visited,ies_challenge_click,ies_music_click,location_click");
        CONFIG_VALUES.put("file.cl_ad.toutiao_ad_report_data.kafka.sink.topic", "toutiao_ad_report_data-sink");
        CONFIG_VALUES.put("file.cl_ad.toutiao_ad_report_data.kafka.source.topic", "toutiao_ad_report_data-source");

        CONFIG_VALUES.put("file.cl_ad.toutiao_advertiser_report_data", "file.cl_ad.toutiao_advertiser_report_data.doc_name,file.cl_ad.toutiao_advertiser_report_data.fields,file.cl_ad.toutiao_advertiser_report_data.kafka.sink.topic,file.cl_ad.toutiao_advertiser_report_data.kafka.source.topic");
        CONFIG_VALUES.put("file.cl_ad.toutiao_advertiser_report_data.doc_name", "toutiao_advertiser_report_data");
        CONFIG_VALUES.put("file.cl_ad.toutiao_advertiser_report_data.fields", "advertiser_id,stat_datetime,months,days,hours,cost,show_count,avg_show_cost,click,avg_click_cost,ctr,convert_count,convert_cost,convert_rate,deep_convert,deep_convert_cost,deep_convert_rate,attribution_convert,attribution_convert_cost,attribution_deep_convert,attribution_deep_convert_cost,download_start,download_start_cost,download_start_rate,download_finish,download_finish_cost,download_finish_rate,click_install,install_finish,install_finish_cost,install_finish_rate,active,active_cost,active_rate,register,active_register_cost,active_register_rate,next_day_open,next_day_open_cost,next_day_open_rate,attribution_next_day_open_cnt,attribution_next_day_open_cost,attribution_next_day_open_rate,game_addiction,game_addiction_cost,game_addiction_rate,pay_count,active_pay_cost,active_pay_rate,loan_completion,loan_completion_cost,loan_completion_rate,pre_loan_credit,pre_loan_credit_cost,loan_credit,loan_credit_cost,loan_credit_rate,in_app_uv,in_app_detail_uv,in_app_cart,in_app_pay,in_app_order,phone,form,map_search,button,view_count,download,qq,lottery,vote,message,redirect,shopping,consult,wechat,phone_confirm,phone_connect,consult_effective,coupon,coupon_single_page,total_play,valid_play,valid_play_cost,valid_play_rate,play_25_feed_break,play_50_feed_break,play_75_feed_break,play_100_feed_break,average_play_time_per_play,play_over_rate,wifi_play_rate,wifi_play,play_duration_sum,advanced_creative_phone_click,advanced_creative_counsel_click,advanced_creative_form_click,advanced_creative_coupon_addition,share,comment,like_count,follow,home_visited,ies_challenge_click,ies_music_click,location_click");
        CONFIG_VALUES.put("file.cl_ad.toutiao_advertiser_report_data.kafka.sink.topic", "toutiao_advertiser_report_data-sink");
        CONFIG_VALUES.put("file.cl_ad.toutiao_advertiser_report_data.kafka.source.topic", "toutiao_advertiser_report_data-source");

        CONFIG_VALUES.put("file.cl_ad.toutiao_material_report_data", "file.cl_ad.toutiao_material_report.doc_name,file.cl_ad.toutiao_material_report.fields,file.cl_ad.toutiao_material_report.kafka.sink.topic,file.cl_ad.toutiao_material_report.kafka.source.topic");
        CONFIG_VALUES.put("file.cl_ad.toutiao_material_report.doc_name", "toutiao_material_report_data");
        CONFIG_VALUES.put("file.cl_ad.toutiao_material_report.fields", "advertiser_id,ad_id,material_id,inventory,stat_datetime,months,days,campaign_id,cost,show_count,avg_show_cost,click,avg_click_cost,ctr,convert_count,convert_cost,convert_rate,deep_convert,deep_convert_cost,deep_convert_rate,attribution_convert,attribution_convert_cost,attribution_deep_convert,attribution_deep_convert_cost,download_start,download_start_cost,download_start_rate,download_finish,download_finish_cost,download_finish_rate,click_install,install_finish,install_finish_cost,install_finish_rate,active,active_cost,active_rate,register,active_register_cost,active_register_rate,next_day_open,next_day_open_cost,next_day_open_rate,attribution_next_day_open_cnt,attribution_next_day_open_cost,attribution_next_day_open_rate ,game_addiction ,game_addiction_cost,game_addiction_rate,pay_count,active_pay_cost,active_pay_rate,loan_completion,loan_completion_cost,loan_completion_rate,pre_loan_credit,pre_loan_credit_cost,loan_credit,loan_credit_cost  ,loan_credit_rate,in_app_uv,in_app_detail_uv,in_app_cart,in_app_pay,in_app_order,phone,form,map_search,button,view_count,download,qq,lottery,vote,message,redirect,shopping,consult,wechat,phone_confirm,phone_connect,consult_effective,coupon,coupon_single_page,total_play,valid_play,valid_play_cost,valid_play_rate,play_25_feed_break,play_50_feed_break,play_75_feed_break,play_100_feed_break,average_play_time_per_play,play_over_rate,wifi_play_rate,wifi_play ,play_duration_sum,advanced_creative_phone_click,advanced_creative_counsel_click,advanced_creative_form_click,advanced_creative_coupon_addition,share,comment,like_count,follow,home_visited,ies_challenge_click,ies_music_click,location_click");
        CONFIG_VALUES.put("file.cl_ad.toutiao_material_report.kafka.sink.topic", "toutiao_material_report_data-sink");
        CONFIG_VALUES.put("file.cl_ad.toutiao_material_report.kafka.source.topic", "toutiao_material_report_data-source");
    }

    public static String getConfigValues(String key) {
        return CONFIG_VALUES.get(key);
    }

    public static HashMap<String, ConfigValue> getConfigValueByNameSpaces(String[] nameServices) {
        HashMap<String, ConfigValue> appConfigs = new HashMap<String, ConfigValue>();
        for (String ns : nameServices) {
            String docName = FileConfigCenterManager.getConfigValues(ns + ".doc_name");
            String sourceTopic = FileConfigCenterManager.getConfigValues(ns + ".kafka.source.topic");
            String sinkTopic = FileConfigCenterManager.getConfigValues(ns + ".kafka.sink.topic");
            String fields = FileConfigCenterManager.getConfigValues(ns + ".fields");
            ConfigValue cv = new ConfigValue(docName);
            cv.putConfigs("kafka.source.topic", sourceTopic);
            cv.putConfigs("kafka.sink.topic", sinkTopic);
            cv.putConfigs("fields", fields);
            appConfigs.put(docName, cv);
        }
        return appConfigs;
    }

    public static HashMap<String, ArrayList<String>> getDocNameFiledsByNameSpaces(String[] nameServices) {
        HashMap<String, ArrayList<String>> docNamefields = new HashMap<String, ArrayList<String>>();
        HashMap<String, ConfigValue> appConfigs = getConfigValueByNameSpaces(nameServices);
        Set<String> docNames = appConfigs.keySet();
        for (String docName : docNames) {
            ConfigValue cv = appConfigs.get(docName);
            String fields = cv.getConfig("fields");
            docNamefields.put(docName, getConfigFields(fields));
        }
        return docNamefields;
    }

    private static ArrayList<String> getConfigFields(String config_fields) {
        ArrayList<String> fieldList = new ArrayList<String>();
        String[] fields = config_fields.split(",");
        for (String field : fields) {
            String tableField = field.replace(" ", "");
            fieldList.add(tableField);
        }
        return fieldList;
    }
}
