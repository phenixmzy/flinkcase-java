package org.flink.example.usercase.streaming.apollo;

//import com.ctrip.framework.apollo.Config;
//import com.ctrip.framework.apollo.ConfigChangeListener;
//import com.ctrip.framework.apollo.ConfigService;
//import com.ctrip.framework.apollo.model.ConfigChange;
//import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import org.flink.example.usercase.streaming.application.ad.ConfigurationChange;

public class ConfigurationCenterManagerProxy {
  /*  private String configNameSpace;
    private String DEFAULT_VALUE = "undefined";
    private Config config;
    private ConfigurationChange configurationChange;

    public ConfigurationCenterManagerProxy(String configNameSpace) {
        this.configNameSpace = configNameSpace;
        init();
    }

    public void register(ConfigurationChange configurationChange) {
        this.configurationChange = configurationChange;
    }

    public void init() {
        config = ConfigService.getConfig(configNameSpace);
        config.addChangeListener(new ConfigChangeListener() {
            @Override
            public void onChange(ConfigChangeEvent configChangeEvent) {
                for (String key : configChangeEvent.changedKeys()) {
                    ConfigChange change = configChangeEvent.getChange(key);
                    configurationChange.update(key, change.getNewValue());
                }
            }
        });
    }

    public String getValue(String key) {
        return config.getProperty(key, DEFAULT_VALUE);
    }*/

}
