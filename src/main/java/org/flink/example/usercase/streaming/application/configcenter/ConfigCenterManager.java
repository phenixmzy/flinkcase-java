package org.flink.example.usercase.streaming.application.configcenter;

import java.util.HashMap;

public class ConfigCenterManager {
    public final static String AD_CONFIG_FIELDS = "ad_id,ad_name,ad_channel";
    public final static String APP_CONFIG_FIELDS = "app_id,app_name,app_size";

    public final static String NULL_STR = "NULL";
    public final static String SPLIT_FLAG = "\u0001";

    public static HashMap<String, String> CONFIG_VALUES = new HashMap<String, String>();

    public static void init() {
        CONFIG_VALUES.put("canal.chuangliang_ad.toutiao_advertiser_daily_fund", "canal.chuangliang_ad.toutiao_advertiser_daily_fund.table,canal.chuangliang_ad.toutiao_advertiser_daily_fund.fields,canal.chuangliang_ad.toutiao_advertiser_daily_fund.kafka.sink.topic");
        CONFIG_VALUES.put("canal.chuangliang_ad.toutiao_advertiser_daily_fund.table", "toutiao_advertiser_daily_fund");
        CONFIG_VALUES.put("canal.chuangliang_ad.toutiao_advertiser_daily_fund.fields", "id,advertiser_id,days,balance,cash_cost,cost,frozen,income,reward_cost,return_goods_cost,transfer_in,transfer_out,create_time,update_time");
        CONFIG_VALUES.put("canal.chuangliang_ad.toutiao_advertiser_daily_fund.kafka.sink.topic", "toutiao_advertiser_daily_fund-sink");

        CONFIG_VALUES.put("canal.chuangliang_ad.admin_user", "canal.chuangliang_ad.admin_user.table,canal.chuangliang_ad.admin_user.fields,canal.chuangliang_ad.admin_user.kafka.sink.topic");
        CONFIG_VALUES.put("canal.chuangliang_ad.admin_user.table", "admin_user");
        CONFIG_VALUES.put("canal.chuangliang_ad.admin_user.fields", "user_id,project_id,group_id,email,user_name,password,mobile,note,is_lock,parent_id,create_time,create_user_id,update_time,update_user_id,gray_permissions,data_range,is_delete");
        CONFIG_VALUES.put("canal.chuangliang_ad.admin_user.kafka.sink.topic", "admin_user-sink");

        CONFIG_VALUES.put("canal.chuangliang_ad.media_account", "canal.chuangliang_ad.media_account.table,canal.chuangliang_ad.media_account.fields,canal.chuangliang_ad.media_account.kafka.sink.topic");
        CONFIG_VALUES.put("canal.chuangliang_ad.media_account.table", "media_account");
        CONFIG_VALUES.put("canal.chuangliang_ad.media_account.fields", "media_account_id,parent_id,owner_user_id,media_type,media_agent_id,advertiser_type,advertiser_id,advertiser_name,advertiser_nick,advertiser_source,advertiser_status,develop_app_key,develop_app_secret,access_token,access_token_time,access_token_expires,access_token_retry_times,refresh_token,refresh_token_expires,company,note,create_time,create_user_id,update_time,update_user_id,is_delete,balance,today_cost,yesterday_cost");
        CONFIG_VALUES.put("canal.chuangliang_ad.media_account.kafka.sink.topic", "media_account-sink");

        CONFIG_VALUES.put("canal.chuangliang_ad.media_agent", "canal.chuangliang_ad.media_agent.table,canal.chuangliang_ad.media_agent.fields,canal.chuangliang_ad.media_agent.kafka.sink.topic");
        CONFIG_VALUES.put("canal.chuangliang_ad.media_agent.table", "media_agent");
        CONFIG_VALUES.put("canal.chuangliang_ad.media_agent.fields", "media_agent_id,agent_name,agent_company,agent_status,media_type,rebate,note,create_time,owner_user_id,create_user_id,update_time,update_user_id,is_delete");
        CONFIG_VALUES.put("canal.chuangliang_ad.media_agent.kafka.sink.topic", "media_agent-sink");

        CONFIG_VALUES.put("canal.chuangliang_ad.admin_project", "canal.chuangliang_ad.admin_project.table,canal.chuangliang_ad.admin_project.fields,canal.chuangliang_ad.admin_project.kafka.sink.topic");
        CONFIG_VALUES.put("canal.chuangliang_ad.admin_project.table", "admin_project");
        CONFIG_VALUES.put("canal.chuangliang_ad.admin_project.fields", "project_id,project_name,owner_user_id,note,create_time,create_user_id,update_time,update_user_id,is_delete");
        CONFIG_VALUES.put("canal.chuangliang_ad.admin_project.kafka.sink.topic", "admin_project-sink");

        CONFIG_VALUES.put("canal.chuangliang_ad.app", "canal.chuangliang_ad.app.table,canal.chuangliang_ad.app.fields,canal.chuangliang_ad.app.kafka.sink.topic");
        CONFIG_VALUES.put("canal.chuangliang_ad.app.table", "app");
        CONFIG_VALUES.put("canal.chuangliang_ad.app.fields", "app_id,app_group_id,app_name,app_key,owner_user_id,package_name,appstore_id,appstore_url,platform,icon_uri,is_active,cost_target_config,create_time,create_user_id ,update_time,update_user_id,is_delete");
        CONFIG_VALUES.put("canal.chuangliang_ad.app.kafka.sink.topic", "app-sink");

        CONFIG_VALUES.put("canal.chuangliang_ad.material", "canal.chuangliang_ad.material.table,canal.chuangliang_ad.material.fields,canal.chuangliang_ad.material.kafka.sink.topic");
        CONFIG_VALUES.put("canal.chuangliang_ad.material.table", "material");
        CONFIG_VALUES.put("canal.chuangliang_ad.material.fields", "material_id,special_id,copy_material_id,group_id,material_name,material_type,height,width,aspect_ratio,video_duration,thumbnail_uri,file_uri,file_siz,file_direction,file_md5,extra_info,creative_user,make_user,note,owner_user_id,create_time,create_user_id,update_time,update_user_id,is_delete");
        CONFIG_VALUES.put("canal.chuangliang_ad.material.kafka.sink.topic", "material-sink");

        CONFIG_VALUES.put("canal.chuangliang_ad.material_group", "canal.chuangliang_ad.material_group.table,canal.chuangliang_ad.material_group.fields,canal.chuangliang_ad.material_group.kafka.sink.topic");
        CONFIG_VALUES.put("canal.chuangliang_ad.material_group.table", "material_group");
        CONFIG_VALUES.put("canal.chuangliang_ad.material_group.fields", "group_id,group_name,special_id,note,create_time,create_user_id,update_time,update_user_id,is_delete");
        CONFIG_VALUES.put("canal.chuangliang_ad.material_group.kafka.sink.topic", "material_group-sink");

        CONFIG_VALUES.put("canal.chuangliang_ad.material_special", "canal.chuangliang_ad.material_special.table,canal.chuangliang_ad.material_special.fields,canal.chuangliang_ad.material_special.kafka.sink.topic");
        CONFIG_VALUES.put("canal.chuangliang_ad.material_special.table", "material_special");
        CONFIG_VALUES.put("canal.chuangliang_ad.material_special.fields", "special_id,special_name,note,owner_user_id,is_open_permission,create_time,create_user_id,update_time,update_user_id,is_delete");
        CONFIG_VALUES.put("canal.chuangliang_ad.material_special.kafka.sink.topic", "material_special-sink");

        CONFIG_VALUES.put("canal.chuangliang_ad.toutiao_campaign_info", "canal.chuangliang_ad.toutiao_campaign_info.table,canal.chuangliang_ad.toutiao_campaign_info.fields,canal.chuangliang_ad.toutiao_campaign_info.kafka.sink.topic");
        CONFIG_VALUES.put("canal.chuangliang_ad.toutiao_campaign_info.table", "toutiao_campaign_info");
        CONFIG_VALUES.put("canal.chuangliang_ad.toutiao_campaign_info.fields", "id,campaign_id,advertiser_id,name,status,budget_mode,landing_type,budget,campaign_create_time,campaign_modify_time,unique_fk,is_locate_create,create_time,update_time");
        CONFIG_VALUES.put("canal.chuangliang_ad.toutiao_campaign_info.kafka.sink.topic", "toutiao_campaign_info-sink");

        CONFIG_VALUES.put("canal.chuangliang_ad.toutiao_ad_info", "canal.chuangliang_ad.toutiao_ad_info.table,canal.chuangliang_ad.toutiao_ad_info.fields,canal.chuangliang_ad.toutiao_ad_info.kafka.sink.topic");
        CONFIG_VALUES.put("canal.chuangliang_ad.toutiao_ad_info.table", "toutiao_ad_info");
        CONFIG_VALUES.put("canal.chuangliang_ad.toutiao_ad_info.fields", "id,advertiser_id,ad_id,campaign_id,inventory_type,delivery_range,status,opt_status,name,budget_mode,budget,start_time,end_time,bid,pricing,schedule_type,schedule_time,flow_control_mode,open_url,download_type,download_url,external_url,app_type,union_video_type,package,hide_if_exists,hide_if_converted,cpa_bid,audience,convert_id,deep_bid_type,deep_cpabid,roi_goal,smart_bid_type,adjust_cpa,is_local_create,download_mode,learning_phase,cl_app_id,activity_id,group_id,ad_create_time,ad_modify_time,create_time,update_time");
        CONFIG_VALUES.put("canal.chuangliang_ad.toutiao_ad_info.kafka.sink.topic", "toutiao_ad_info-sink");

        CONFIG_VALUES.put("canal.chuangliang_ad.toutiao_inventory_info", "canal.chuangliang_ad.toutiao_inventory_info.table,canal.chuangliang_ad.toutiao_inventory_info.fields,canal.chuangliang_ad.toutiao_inventory_info.kafka.sink.topic");
        CONFIG_VALUES.put("canal.chuangliang_ad.toutiao_inventory_info.table", "toutiao_inventory_info");
        CONFIG_VALUES.put("canal.chuangliang_ad.toutiao_inventory_info.fields", "inventory_key,inventory_name");
        CONFIG_VALUES.put("canal.chuangliang_ad.toutiao_inventory_info.kafka.sink.topic", "toutiao_inventory_info-sink");

        CONFIG_VALUES.put("canal.chuangliang_ad.toutiao_material_info", "canal.chuangliang_ad.toutiao_material_info.table,canal.chuangliang_ad.toutiao_material_info.fields,canal.chuangliang_ad.toutiao_material_info.kafka.sink.topic");
        CONFIG_VALUES.put("canal.chuangliang_ad.toutiao_material_info.table", "toutiao_material_info");
        CONFIG_VALUES.put("canal.chuangliang_ad.toutiao_material_info.fields", "id,advertiser_id,touttiao_material_str_id,toutiao_material_id,material_type,signature,size,width,height,format,bit_rate,duration,source,update_time");
        CONFIG_VALUES.put("canal.chuangliang_ad.toutiao_material_info.kafka.sink.topic", "toutiao_material_info-sink");
    }

    public static String getConfigValues(String key) {
        return CONFIG_VALUES.get(key);
    }
}
