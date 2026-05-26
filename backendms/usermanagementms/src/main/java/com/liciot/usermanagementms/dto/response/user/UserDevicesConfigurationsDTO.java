package com.liciot.usermanagementms.dto.response.user;

import java.util.HashMap;
import java.util.Map;

public class UserDevicesConfigurationsDTO {
    Map<String,String> devicesConfigurations = new HashMap<>();

    public UserDevicesConfigurationsDTO() {
    }

    public UserDevicesConfigurationsDTO(Map<String, String> devicesConfigurations) {

        this.devicesConfigurations = devicesConfigurations;
    }

    public Map<String, String> getDevicesConfigurations() {
        return devicesConfigurations;
    }

    public void setDevicesConfigurations(Map<String, String> devicesConfigurations) {
        this.devicesConfigurations = devicesConfigurations;
    }
    public void addDeviceWithConfiguration(String deviceId, String configurationJSON)
    {
        this.devicesConfigurations.put(deviceId, configurationJSON);
    }
}
