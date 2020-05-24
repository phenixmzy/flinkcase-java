package org.flink.example.usercase.streaming.application.ad;

import com.alibaba.fastjson.JSONObject;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer011;
import org.apache.flink.streaming.connectors.kafka.internal.FlinkKafkaProducer;
import org.flink.example.common.constant.PropertiesConstants;
import org.flink.example.usercase.streaming.util.ExecutionEnvUtil;
import org.flink.example.usercase.streaming.util.KafkaConfigUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class ADReportFormatStreamingApplicationCC {
    private static final Logger LOGGER = LoggerFactory.getLogger(ADReportFormatStreamingApplicationCC.class);

    public static void main(String[] args) throws Exception {
        final ParameterTool parameterTool = ExecutionEnvUtil.createParameterTool(args);
        StreamExecutionEnvironment env = ExecutionEnvUtil.prepare(parameterTool);
        DataStreamSource<String> source = KafkaConfigUtil.buildSource(env);


        /**
         * https://blog.csdn.net/xianpanjia4616/article/details/105571388
        new FlinkKafkaProducer<ADBean>("",
                new ADKafkaSerialization,
                producerProps,
                FlinkKafkaProducer.Semantic.EXACTLY_ONCE)
        producer.setLogFailuresOnly(false)
*/

        source.map(new RichMapFunction<String, String>() {
            private final static String CONFIG_FIELDS = "ad_id,stat_date,campaign_id,cost,show_number,avg_show_cost,click,avg_click_cost,ctr,convert_number,convert_cost,convert_rate,deep_convert,deep_convert_cost,deep_convert_rate,attribution_convert,attribution_convert_cost,attribution_deep_convert,attribution_deep_convert_cost,download_start,download_start_cost,download_start_rate,download_finish,download_finish_cost,download_finish_rate,click_install,install_finish,install_finish_cost,install_finish_rate,active,active_cost,active_rate,register,active_register_cost,active_register_rate,next_day_open,next_day_open_cost,next_day_open_rate,attribution_next_day_open_cnt,attribution_next_day_open_cost,attribution_next_day_open_rate,game_addiction,game_addiction_cost,game_addiction_rate,pay_count,active_pay_cost,active_pay_rate,loan_completion,loan_completion_cost,loan_completion_rate,pre_loan_credit,pre_loan_credit_cost,loan_credit,loan_credit_cost,loan_credit_rate,in_app_uv,in_app_detail_uv,in_app_cart,in_app_pay,in_app_order,phone,form,map_search,button,view_number,download,qq,lottery,vote,message,redirect,shopping,consult,wechat,phone_confirm,phone_connect,consult_effective,coupon,coupon_single_page,total_play,valid_play,valid_play_cost,valid_play_rate,play_25_feed_break,play_50_feed_break,play_75_feed_break,play_100_feed_break,average_play_time_per_play,play_over,play_over_rate,wifi_play_rate,wifi_play,play_duration_sum,advanced_creative_phone_click,advanced_creative_counsel_click,advanced_creative_form_click,advanced_creative_coupon_addition,share,comment,like_number,follow,home_visited,ies_challenge_click,ies_music_click,location_click";
            private final static String NULL_STR = "NULL";
            private final static String SPLIT_FLAG = "\u0001";

            private ParameterTool config;
            private ArrayList<String> FIELD_LIST;

            @Override
            public void open(Configuration parameters) throws Exception {
                super.open(parameters);
//config = (ParameterTool) getRuntimeContext().getExecutionConfig().getGlobalJobParameters();
                FIELD_LIST = getConfigFields();
                LOGGER.info("GET FIELD_LIST:");
                LOGGER.info(FIELD_LIST.toString());
            }

            @Override
            public String map(String eventJsonStr) throws Exception {
                LOGGER.info("GET FIELD_LIST:");
                LOGGER.info(FIELD_LIST.toString());
                LOGGER.info(eventJsonStr);

                JSONObject json = JSONObject.parseObject(eventJsonStr);
                return getFormatStr(json);
            }

            private ArrayList<String> getConfigFields() {
                ArrayList<String> fieldList = new ArrayList<String>();
                String[] fields = CONFIG_FIELDS.split(",");
                for (String field : fields) {
                    fieldList.add(field);
                }
                return fieldList;
            }

            private String getFormatStr(JSONObject json) {
                StringBuilder builder = new StringBuilder();
                for(String field : FIELD_LIST) {
                    Object value = json.get(field);
                    if (value == null) {
                        builder.append(NULL_STR).append(SPLIT_FLAG);
                        continue;
                    }
                    builder.append(value.toString()).append(SPLIT_FLAG);
                }
                return builder.length() > 0 ? builder.substring(0, builder.length() - SPLIT_FLAG.length()) : "";
            }
        }).addSink(new FlinkKafkaProducer011(parameterTool.getRequired("kafka.brokers"),
                parameterTool.getRequired(PropertiesConstants.KAFKA_SINK_TOPIC_KEY),
                new SimpleStringSchema()));
        env.execute("Streaming toutiao-ad-report json to cvs to kafka");

    }

}