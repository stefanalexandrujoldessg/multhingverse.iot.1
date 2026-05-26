package com.liciot.usermanagementms.dto.response.device;

public class DeviceConfigurationDTO {
    String configurationJSON;
    String extra = "ana";

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

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }
}

