package org.flink.example.usercase.streaming.application.ad;

import java.util.HashMap;

class ConfigValue {
    private String serviceName;
    private String fields;
    private String sourceTopic;
    private String sinkTopic;

    public ConfigValue(String serviceName, String fields, String sourceTopic, String sinkTopic) {
        this.serviceName = serviceName;
        this.fields = fields;
        this.sourceTopic = sourceTopic;
        this.sinkTopic = sinkTopic;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getFields() {
        return fields;
    }

    public String getSourceTopic() {
        return sourceTopic;
    }

    public String getSinkTopic() {
        return sinkTopic;
    }
}

public class ConfigCenter {

    public final static String AD_CONFIG_FIELDS = "ad_id,stat_date,campaign_id,cost,show_number,avg_show_cost,click,avg_click_cost,ctr,convert_number,convert_cost,convert_rate,deep_convert,deep_convert_cost,deep_convert_rate,attribution_convert,attribution_convert_cost,attribution_deep_convert,attribution_deep_convert_cost,download_start,download_start_cost,download_start_rate,download_finish,download_finish_cost,download_finish_rate,click_install,install_finish,install_finish_cost,install_finish_rate,active,active_cost,active_rate,register,active_register_cost,active_register_rate,next_day_open,next_day_open_cost,next_day_open_rate,attribution_next_day_open_cnt,attribution_next_day_open_cost,attribution_next_day_open_rate,game_addiction,game_addiction_cost,game_addiction_rate,pay_count,active_pay_cost,active_pay_rate,loan_completion,loan_completion_cost,loan_completion_rate,pre_loan_credit,pre_loan_credit_cost,loan_credit,loan_credit_cost,loan_credit_rate,in_app_uv,in_app_detail_uv,in_app_cart,in_app_pay,in_app_order,phone,form,map_search,button,view_number,download,qq,lottery,vote,message,redirect,shopping,consult,wechat,phone_confirm,phone_connect,consult_effective,coupon,coupon_single_page,total_play,valid_play,valid_play_cost,valid_play_rate,play_25_feed_break,play_50_feed_break,play_75_feed_break,play_100_feed_break,average_play_time_per_play,play_over,play_over_rate,wifi_play_rate,wifi_play,play_duration_sum,advanced_creative_phone_click,advanced_creative_counsel_click,advanced_creative_form_click,advanced_creative_coupon_addition,share,comment,like_number,follow,home_visited,ies_challenge_click,ies_music_click,location_click";
    public final static String APP_CONFIG_FIELDS = "ad_id,stat_date,campaign_id,cost,show_number,avg_show_cost,click,avg_click_cost,ctr,convert_number,convert_cost,convert_rate,deep_convert,deep_convert_cost,deep_convert_rate,attribution_convert,attribution_convert_cost,attribution_deep_convert,attribution_deep_convert_cost,download_start,download_start_cost,download_start_rate,download_finish,download_finish_cost,download_finish_rate,click_install,install_finish,install_finish_cost,install_finish_rate,active,active_cost,active_rate,register,active_register_cost,active_register_rate,next_day_open,next_day_open_cost,next_day_open_rate,attribution_next_day_open_cnt,attribution_next_day_open_cost,attribution_next_day_open_rate,game_addiction,game_addiction_cost,game_addiction_rate,pay_count,active_pay_cost,active_pay_rate,loan_completion,loan_completion_cost,loan_completion_rate,pre_loan_credit,pre_loan_credit_cost,loan_credit,loan_credit_cost,loan_credit_rate,in_app_uv,in_app_detail_uv,in_app_cart,in_app_pay,in_app_order,phone,form,map_search,button,view_number,download,qq,lottery,vote,message,redirect,shopping,consult,wechat,phone_confirm,phone_connect,consult_effective,coupon,coupon_single_page,total_play,valid_play,valid_play_cost,valid_play_rate,play_25_feed_break,play_50_feed_break,play_75_feed_break,play_100_feed_break,average_play_time_per_play,play_over,play_over_rate,wifi_play_rate,wifi_play,play_duration_sum,advanced_creative_phone_click,advanced_creative_counsel_click,advanced_creative_form_click,advanced_creative_coupon_addition,share,comment,like_number,follow,home_visited,ies_challenge_click,ies_music_click,location_click";

    public final static String NULL_STR = "NULL";
    public final static String SPLIT_FLAG = "\u0001";

    public static HashMap<String, ConfigValue> CONFIG_VALUES = new HashMap<String, ConfigValue>();

    public final static String AD_SERVICE_NAME = "AD_SERVICE";
    public final static String APP_SERVICE_NAME = "APP_SERVICE";

    public static void init() {
        ConfigValue adConfigValue = new ConfigValue("AD_SERVICE", AD_CONFIG_FIELDS, "ad-source", "ad-slink");
        ConfigValue appConfigValue = new ConfigValue("APP_SERVICE", APP_CONFIG_FIELDS, "app-source", "ap-slink");
        CONFIG_VALUES.put(AD_SERVICE_NAME, adConfigValue);
        CONFIG_VALUES.put(APP_SERVICE_NAME, appConfigValue);
    }

    public static HashMap<String, ConfigValue> getConfigValues() {
        return CONFIG_VALUES;
    }
}
