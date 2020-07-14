package org.flink.example.usercase;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigChangeListener;
import com.ctrip.framework.apollo.ConfigService;
import com.ctrip.framework.apollo.model.ConfigChange;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;

import java.util.Set;

public class TestApollo {

    public static void main(String[] args) {
        System.setProperty("APOLLO_META", "http://localhost:8080");
        Config config = ConfigService.getConfig("application");
        Set<String> names = config.getPropertyNames();
        for (String name: names) {
            System.out.println(name);
            System.out.println(config.getProperty(name,"1"));
        }

        config.addChangeListener(new ConfigChangeListener() {
            @Override
            public void onChange(ConfigChangeEvent configChangeEvent) {
                for (String key : configChangeEvent.changedKeys()) {
                    ConfigChange change = configChangeEvent.getChange(key);
                    System.out.println(key + ":" +change.getNewValue());
                }
            }
        });
    }
}
