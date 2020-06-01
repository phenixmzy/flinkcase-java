package org.flink.example.usercase.streaming.application.ad;

import org.flink.example.usercase.streaming.application.configcenter.ConfigValue;
import org.flink.example.usercase.streaming.application.configcenter.RecordDataConfigValue;

import java.util.HashMap;

public class FileConfigurationService extends ConfigurationService{

    public FileConfigurationService(String[] nameServices, String configNameSpaces) {
        super(nameServices, configNameSpaces);
    }

    @Override
    protected RecordDataConfigValue getRecordDataConfigValueBy(String recordDataName) {
        return null;
    }


}
