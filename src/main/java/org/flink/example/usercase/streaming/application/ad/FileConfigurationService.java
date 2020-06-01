package org.flink.example.usercase.streaming.application.ad;

import org.flink.example.usercase.streaming.application.configcenter.RecordDataConfigValue;

public class FileConfigurationService extends ConfigurationService{

    public FileConfigurationService(String[] nameServices, String configNameSpaces) {
        super(nameServices, configNameSpaces);
    }

    @Override
    protected RecordDataConfigValue getRecordDataConfigValueBy(String recordDataName) {
        return null;
    }


}
