package com.liciot.usermanagementms.dto.response.user;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class UserAccessDevicesDTO {
    Set<String> accessDevices = new HashSet<String>();
    Map<String, String> devicesConfiguration = new HashMap<>();
    Map<String, String> userDevicesConfiguration = new HashMap<>();
    public UserAccessDevicesDTO(Set<String> accessDevices) {
        this.accessDevices = accessDevices;
    }

    public Set<String> getAccessDevices() {
        return accessDevices;
    }

    public void setAccessDevices(Set<String> accessDevices) {
        this.accessDevices = accessDevices;
    }

    public UserAccessDevicesDTO() {
    }

    public Map<String, String> getDevicesConfiguration() {
        return devicesConfiguration;
    }

    public void setDevicesConfiguration(Map<String, String> devicesConfiguration) {
        this.devicesConfiguration = devicesConfiguration;
    }

    public void addAccessDevice(String deviceId, String configurationJSON)
    {
        this.accessDevices.add(deviceId);
        this.devicesConfiguration.put(deviceId, configurationJSON);
    }
    public void addAccessDevice(String deviceId, String configurationJSON, String userConfigurationJSON)
    {
        this.accessDevices.add(deviceId);
        this.devicesConfiguration.put(deviceId, configurationJSON);
        this.userDevicesConfiguration.put(deviceId, userConfigurationJSON);

    }

    public Map<String, String> getUserDevicesConfiguration() {
        return userDevicesConfiguration;
    }

    public void setUserDevicesConfiguration(Map<String, String> userDevicesConfiguration) {
        this.userDevicesConfiguration = userDevicesConfiguration;
    }

    public UserAccessDevicesDTO(Set<String> accessDevices, Map<String, String> devicesConfiguration, Map<String, String> userDevicesConfiguration) {
        this.accessDevices = accessDevices;
        this.devicesConfiguration = devicesConfiguration;
        this.userDevicesConfiguration = userDevicesConfiguration;
    }
}