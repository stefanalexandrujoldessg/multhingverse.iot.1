package com.liciot.devicems.dto.response.device;

public class DeviceConfigurationDTO {
    String configurationJSON;

    public DeviceConfigurationDTO(String configurationJSON) {
        this.configurationJSON = configurationJSON;
    }

    public DeviceConfigurationDTO() {

    }

    public String getConfigurationJSON() {
        return configurationJSON;
    }

    public void setConfigurationJSON(String configurationJSON) {
        this.configurationJSON = configurationJSON;
    }
}

