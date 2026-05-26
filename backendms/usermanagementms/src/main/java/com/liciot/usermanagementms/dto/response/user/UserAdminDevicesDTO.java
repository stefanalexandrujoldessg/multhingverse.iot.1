package com.liciot.usermanagementms.dto.response.user;

import com.google.gson.JsonObject;
import com.liciot.usermanagementms.dto.response.device.DeviceIdentificationDTO;

import java.util.*;

public class UserAdminDevicesDTO {
    Set<String> adminDevices = new HashSet<String>();
    Map<String, String> devicesConfiguration = new HashMap<>();
    Map<String, String> userDevicesConfiguration = new HashMap<>();
    public UserAdminDevicesDTO(Set<String> adminDevices) {
        this.adminDevices = adminDevices;
    }

    public Set<String> getAdminDevices() {
        return adminDevices;
    }

    public void setAdminDevices(Set<String> adminDevices) {
        this.adminDevices = adminDevices;
    }

    public UserAdminDevicesDTO() {
    }

    public Map<String, String> getDevicesConfiguration() {
        return devicesConfiguration;
    }

    public void setDevicesConfiguration(Map<String, String> devicesConfiguration) {
        this.devicesConfiguration = devicesConfiguration;
    }

    public void addAdminDevice(String deviceId, String configurationJSON)
    {
        this.adminDevices.add(deviceId);
        this.devicesConfiguration.put(deviceId, configurationJSON);
    }
    public void addAdminDevice(String deviceId, String configurationJSON, String userConfigurationJSON)
    {
        this.adminDevices.add(deviceId);
        this.devicesConfiguration.put(deviceId, configurationJSON);
        this.userDevicesConfiguration.put(deviceId, userConfigurationJSON);

    }

    public Map<String, String> getUserDevicesConfiguration() {
        return userDevicesConfiguration;
    }

    public void setUserDevicesConfiguration(Map<String, String> userDevicesConfiguration) {
        this.userDevicesConfiguration = userDevicesConfiguration;
    }

    public UserAdminDevicesDTO(Set<String> adminDevices, Map<String, String> devicesConfiguration, Map<String, String> userDevicesConfiguration) {
        this.adminDevices = adminDevices;
        this.devicesConfiguration = devicesConfiguration;
        this.userDevicesConfiguration = userDevicesConfiguration;
    }
}