package org.flink.example.usercase.streaming.application.configcenter;

import java.util.HashMap;

public class FileConfigCenterManager {
    public final static String NULL_STR = "NULL";
    public final static String SPLIT_FLAG = "\u0001";

    public static HashMap<String, String> CONFIG_VALUES = new HashMap<String, String>();

    public static void init() {
        CONFIG_VALUES.put("file.chuangliang_ad.admin_user", "file.chuangliang_ad.admin_user.doc_type,file.chuangliang_ad.admin_user.fields,file.chuangliang_ad.admin_user.kafka.sink.topic,file.chuangliang_ad.admin_user.kafka.source.topic");
        CONFIG_VALUES.put("file.chuangliang_ad.admin_user.doc_type", "admin_user");
        CONFIG_VALUES.put("file.chuangliang_ad.admin_user.fields", "user_id,project_id,group_id,email,user_name,password,mobile,note,is_lock,parent_id,create_time,create_user_id,update_time,update_user_id,gray_permissions,data_range,is_delete");
        CONFIG_VALUES.put("file.chuangliang_ad.admin_user.kafka.sink.topic", "admin_user-sink");
        CONFIG_VALUES.put("file.chuangliang_ad.admin_user.kafka.source.topic", "admin_user-source");

        CONFIG_VALUES.put("file.chuangliang_ad.media_account", "file.chuangliang_ad.media_account.doc_type,file.chuangliang_ad.media_account.fields,file.chuangliang_ad.media_account.kafka.sink.topic,file.chuangliang_ad.media_account.kafka.source.topic");
        CONFIG_VALUES.put("file.chuangliang_ad.media_account.doc_type", "media_account");
        CONFIG_VALUES.put("file.chuangliang_ad.media_account.fields", "media_account_id,parent_id,owner_user_id,media_type,media_agent_id,advertiser_type,advertiser_id,advertiser_name,advertiser_nick,advertiser_source,advertiser_status,develop_app_key,develop_app_secret,access_token,access_token_time,access_token_expires,access_token_retry_times,refresh_token,refresh_token_expires,company,note,create_time,create_user_id,update_time,update_user_id,is_delete,balance,today_cost,yesterday_cost");
        CONFIG_VALUES.put("file.chuangliang_ad.media_account.kafka.sink.topic", "media_account-sink");
        CONFIG_VALUES.put("file.chuangliang_ad.media_account.kafka.source.topic", "media_account-source");

        CONFIG_VALUES.put("file.chuangliang_ad.media_agent", "file.chuangliang_ad.media_agent.doc_type,file.chuangliang_ad.media_agent.fields,file.chuangliang_ad.media_agent.kafka.sink.topic,file.chuangliang_ad.media_agent.kafka.source.topic");
        CONFIG_VALUES.put("file.chuangliang_ad.media_agent.doc_type", "media_agent");
        CONFIG_VALUES.put("file.chuangliang_ad.media_agent.fields", "media_agent_id,agent_name,agent_company,agent_status,media_type,rebate,note,create_time,owner_user_id,create_user_id,update_time,update_user_id,is_delete");
        CONFIG_VALUES.put("file.chuangliang_ad.media_agent.kafka.sink.topic", "media_agent-sink");
        CONFIG_VALUES.put("file.chuangliang_ad.media_agent.kafka.source.topic", "media_agent-source");

        CONFIG_VALUES.put("file.chuangliang_ad.admin_project", "file.chuangliang_ad.admin_project.doc_type,file.chuangliang_ad.admin_project.fields,file.chuangliang_ad.admin_project.kafka.sink.topic,file.chuangliang_ad.admin_project.kafka.source.topic");
        CONFIG_VALUES.put("file.chuangliang_ad.admin_project.doc_type", "admin_project");
        CONFIG_VALUES.put("file.chuangliang_ad.admin_project.fields", "project_id,project_name,owner_user_id,note,create_time,create_user_id,update_time,update_user_id,is_delete");
        CONFIG_VALUES.put("file.chuangliang_ad.admin_project.kafka.sink.topic", "admin_project-sink");
        CONFIG_VALUES.put("file.chuangliang_ad.admin_project.kafka.source.topic", "admin_project-source");

        CONFIG_VALUES.put("file.chuangliang_ad.app", "file.chuangliang_ad.app.doc_type,file.chuangliang_ad.app.fields,file.chuangliang_ad.app.kafka.sink.topic,file.chuangliang_ad.app.kafka.source.topic");
        CONFIG_VALUES.put("file.chuangliang_ad.app.doc_type", "app");
        CONFIG_VALUES.put("file.chuangliang_ad.app.fields", "app_id,app_group_id,app_name,app_key,owner_user_id,package_name,appstore_id,appstore_url,platform,icon_uri,is_active,cost_target_config,create_time,create_user_id ,update_time,update_user_id,is_delete");
        CONFIG_VALUES.put("file.chuangliang_ad.app.kafka.sink.topic", "app-sink");
        CONFIG_VALUES.put("file.chuangliang_ad.app.kafka.source.topic", "app-source");

        CONFIG_VALUES.put("file.chuangliang_ad.material", "file.chuangliang_ad.material.doc_type,file.chuangliang_ad.material.fields,file.chuangliang_ad.material.kafka.sink.topic,file.chuangliang_ad.material.kafka.source.topic");
        CONFIG_VALUES.put("file.chuangliang_ad.material.doc_type", "material");
        CONFIG_VALUES.put("file.chuangliang_ad.material.fields", "material_id,special_id,copy_material_id,group_id,material_name,material_type,height,width,aspect_ratio,video_duration,thumbnail_uri,file_uri,file_siz,file_direction,file_md5,extra_info,creative_user,make_user,note,owner_user_id,create_time,create_user_id,update_time,update_user_id,is_delete");
        CONFIG_VALUES.put("file.chuangliang_ad.material.kafka.sink.topic", "material-sink");
        CONFIG_VALUES.put("file.chuangliang_ad.material.kafka.source.topic", "material-source");

        CONFIG_VALUES.put("file.chuangliang_ad.material_group", "file.chuangliang_ad.material_group.doc_type,file.chuangliang_ad.material_group.fields,file.chuangliang_ad.material_group.kafka.sink.topic,file.chuangliang_ad.material_group.kafka.source.topic");
        CONFIG_VALUES.put("file.chuangliang_ad.material_group.doc_type", "material_group");
        CONFIG_VALUES.put("file.chuangliang_ad.material_group.fields", "group_id,group_name,special_id,note,create_time,create_user_id,update_time,update_user_id,is_delete");
        CONFIG_VALUES.put("file.chuangliang_ad.material_group.kafka.sink.topic", "material_group-sink");
        CONFIG_VALUES.put("file.chuangliang_ad.material_group.kafka.source.topic", "material_group-source");

        CONFIG_VALUES.put("file.chuangliang_ad.material_special", "file.chuangliang_ad.material_special.doc_type,file.chuangliang_ad.material_special.fields,file.chuangliang_ad.material_special.kafka.sink.topic,file.chuangliang_ad.material_special.kafka.source.topic");
        CONFIG_VALUES.put("file.chuangliang_ad.material_special.doc_type", "material_special");
        CONFIG_VALUES.put("file.chuangliang_ad.material_special.fields", "special_id,special_name,note,owner_user_id,is_open_permission,create_time,create_user_id,update_time,update_user_id,is_delete");
        CONFIG_VALUES.put("file.chuangliang_ad.material_special.kafka.sink.topic", "material_special-sink");
        CONFIG_VALUES.put("file.chuangliang_ad.material_special.kafka.source.topic", "material_special-source");

        CONFIG_VALUES.put("file.chuangliang_ad.toutiao_campaign_info", "file.chuangliang_ad.toutiao_campaign_info.doc_type,file.chuangliang_ad.toutiao_campaign_info.fields,file.chuangliang_ad.toutiao_campaign_info.kafka.sink.topic,file.chuangliang_ad.toutiao_campaign_info.kafka.source.topic");
        CONFIG_VALUES.put("file.chuangliang_ad.toutiao_campaign_info.doc_type", "toutiao_campaign_info");
        CONFIG_VALUES.put("file.chuangliang_ad.toutiao_campaign_info.fields", "id,campaign_id,advertiser_id,name,status,budget_mode,landing_type,budget,campaign_create_time,campaign_modify_time,unique_fk,is_locate_create,create_time,update_time");
        CONFIG_VALUES.put("file.chuangliang_ad.toutiao_campaign_info.kafka.sink.topic", "toutiao_campaign_info-sink");
        CONFIG_VALUES.put("file.chuangliang_ad.toutiao_campaign_info.kafka.source.topic", "toutiao_campaign_info-source");

        CONFIG_VALUES.put("file.chuangliang_ad.toutiao_ad_info", "file.chuangliang_ad.toutiao_ad_info.doc_type,file.chuangliang_ad.toutiao_ad_info.fields,file.chuangliang_ad.toutiao_ad_info.kafka.sink.topic,file.chuangliang_ad.toutiao_ad_info.kafka.source.topic");
        CONFIG_VALUES.put("file.chuangliang_ad.toutiao_ad_info.doc_type", "toutiao_ad_info");
        CONFIG_VALUES.put("file.chuangliang_ad.toutiao_ad_info.fields", "id,advertiser_id,ad_id,campaign_id,inventory_type,delivery_range,status,opt_status,name,budget_mode,budget,start_time,end_time,bid,pricing,schedule_type,schedule_time,flow_control_mode,open_url,download_type,download_url,external_url,app_type,union_video_type,package,hide_if_exists,hide_if_converted,cpa_bid,audience,convert_id,deep_bid_type,deep_cpabid,roi_goal,smart_bid_type,adjust_cpa,is_local_create,download_mode,learning_phase,cl_app_id,activity_id,group_id,ad_create_time,ad_modify_time,create_time,update_time");
        CONFIG_VALUES.put("file.chuangliang_ad.toutiao_ad_info.kafka.sink.topic", "toutiao_ad_info-sink");
        CONFIG_VALUES.put("file.chuangliang_ad.toutiao_ad_info.kafka.source.topic", "toutiao_ad_info-source");

        CONFIG_VALUES.put("file.chuangliang_ad.toutiao_inventory_info", "file.chuangliang_ad.toutiao_inventory_info.doc_type,file.chuangliang_ad.toutiao_inventory_info.fields,file.chuangliang_ad.toutiao_inventory_info.kafka.sink.topic,file.chuangliang_ad.toutiao_inventory_info.kafka.source.topic");
        CONFIG_VALUES.put("file.chuangliang_ad.toutiao_inventory_info.doc_type", "toutiao_inventory_info");
        CONFIG_VALUES.put("file.chuangliang_ad.toutiao_inventory_info.fields", "inventory_key,inventory_name");
        CONFIG_VALUES.put("file.chuangliang_ad.toutiao_inventory_info.kafka.sink.topic", "toutiao_inventory_info-sink");
        CONFIG_VALUES.put("file.chuangliang_ad.toutiao_inventory_info.kafka.source.topic", "toutiao_inventory_info-source");

        CONFIG_VALUES.put("file.chuangliang_ad.toutiao_material_info", "file.chuangliang_ad.toutiao_material_info.doc_type,file.chuangliang_ad.toutiao_material_info.fields,file.chuangliang_ad.toutiao_material_info.kafka.sink.topic,file.chuangliang_ad.toutiao_material_info.kafka.source.topic");
        CONFIG_VALUES.put("file.chuangliang_ad.toutiao_material_info.doc_type", "toutiao_material_info");
        CONFIG_VALUES.put("file.chuangliang_ad.toutiao_material_info.fields", "id,advertiser_id,touttiao_material_str_id,toutiao_material_id,material_type,signature,size,width,height,format,bit_rate,duration,source,update_time");
        CONFIG_VALUES.put("file.chuangliang_ad.toutiao_material_info.kafka.sink.topic", "toutiao_material_info-sink");
        CONFIG_VALUES.put("file.chuangliang_ad.toutiao_material_info.kafka.source.topic", "toutiao_material_info-source");
    }

    public static String getConfigValues(String key) {
        return CONFIG_VALUES.get(key);
    }
}
