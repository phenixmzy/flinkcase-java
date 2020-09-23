package org.flink.example.usercase.streaming.application.gameplay.join.dim;


import com.alibaba.fastjson.JSONObject;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.stumbleupon.async.Callback;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.async.ResultFuture;
import org.apache.flink.streaming.api.functions.async.RichAsyncFunction;
import org.flink.example.usercase.model.GameInfo;
import org.flink.example.usercase.model.GameInfoHelper;
import org.hbase.async.GetRequest;
import org.hbase.async.HBaseClient;
import org.hbase.async.KeyValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeUnit;


public class GameInfoDimHBaseAsyncFunction extends RichAsyncFunction<String, String> {
    private String zkHosts;
    private String zkPort;
    private String hTable;

    private Cache<String, GameInfo> cache = null;
    private HBaseClient client = null;

    public GameInfoDimHBaseAsyncFunction(String zkHosts, String zkPort, String hTable) {
        this.zkHosts = zkHosts;
        this.zkPort = zkPort;
        this.hTable = hTable;

    }

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        client = new HBaseClient(zkHosts, zkPort);
        cache = CacheBuilder.newBuilder()
                //设置cache的初始大小为10，要合理设置该值
                .initialCapacity(10000)
                //设置并发数为5，即同一时间最多只能有5个线程往cache执行写入操作
                .concurrencyLevel(5)
                //设置cache中的数据在写入之后的存活时间为10小时
                .expireAfterWrite(10, TimeUnit.HOURS)
                .recordStats()
                //构建cache实例
                .build();
    }

    @Override
    public void asyncInvoke(String input, ResultFuture<String> resultFuture) throws Exception {
        JSONObject inputJson = JSONObject.parseObject(input);
        String gameId = inputJson.getString("game_id");
        GameInfo gameInfo = cache.getIfPresent(gameId);
        if (gameInfo == null) {
            client.get(new GetRequest(hTable,gameId)).addCallback((Callback<String, ArrayList<KeyValue>>) arg -> {
                for (KeyValue kv : arg) {
                    String key = new String(kv.key());
                    GameInfoHelper.set(gameInfo, key, kv.value());
                }
                cache.put(gameId, gameInfo);
                return null;
            });
        }
        inputJson.put("game_name", gameInfo.getGameName());
        inputJson.put("game_label", gameInfo.getGameLabel());
        inputJson.put("game_name_size", gameInfo.getGameSize());
        inputJson.put("game_type", gameInfo.getGameType());
        inputJson.put("game_op_type", gameInfo.getOperatorType());
        inputJson.put("language", gameInfo.getLanguage());
        inputJson.put("developer", gameInfo.getDeveloper());
        inputJson.put("game_name", gameInfo.getPublishingTime());
        inputJson.put("update_time", gameInfo.getUpdateTime());
        resultFuture.complete(Collections.singleton(inputJson.toJSONString()));
    }

    @Override
    public void timeout(String input, ResultFuture<String> resultFuture) throws Exception {

    }
}
