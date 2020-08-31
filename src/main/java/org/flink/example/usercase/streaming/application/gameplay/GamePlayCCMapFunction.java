package org.flink.example.usercase.streaming.application.gameplay;

import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;
import org.flink.example.usercase.model.GamePlayEvent;
import org.flink.example.usercase.streaming.apollo.ConfigurationCenterManagerProxy;
import org.flink.example.usercase.streaming.application.ad.ConfigurationChange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class GamePlayConfigurationChange implements ConfigurationChange {
    private static final Logger LOGGER = LoggerFactory.getLogger(GamePlayConfigurationChange.class);

    @Override
    public void load() {

    }

    @Override
    public void update(String key, String newValue) {
        LOGGER.info("update key:{}, value:{}", key, newValue);
    }
}

public class GamePlayCCMapFunction extends RichMapFunction<GamePlayEvent, String> {
    private ConfigurationCenterManagerProxy proxy;
    private String namespace;



    public GamePlayCCMapFunction(String namespace) {
        this.namespace = namespace;
    }

    private void init() {
        /*proxy = new ConfigurationCenterManagerProxy(this.namespace);*/
    }


    @Override
    public String map(GamePlayEvent gamePlayEvent) throws Exception {
        return null;//proxy.getValue("fields");
    }

    @Override
    public void open(Configuration parameters) throws Exception{
        super.open(parameters);
        init();
    }
}
