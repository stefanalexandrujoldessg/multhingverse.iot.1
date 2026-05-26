package com.liciot.devicems.dto.response;

import java.util.List;
import java.util.UUID;

public class DeviceDTO {
    UUID deviceId;
    String configurationJSON;
    UserForDeviceDTO adminUser;
    List<UserForDeviceDTO> accessUsers;

    public DeviceDTO(UUID deviceId, String configurationJSON, UserForDeviceDTO adminUser, List<UserForDeviceDTO> accessUsers) {
        this.deviceId = deviceId;
        this.configurationJSON = configurationJSON;
        this.adminUser = adminUser;
        this.accessUsers = accessUsers;
    }

    public DeviceDTO() {
    }

    public UUID getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(UUID deviceId) {
        this.deviceId = deviceId;
    }

    public String getConfigurationJSON() {
        return configurationJSON;
    }

    public void setConfigurationJSON(String configurationJSON) {
        this.configurationJSON = configurationJSON;
    }

    public UserForDeviceDTO getAdminUser() {
        return adminUser;
    }

    public void setAdminUser(UserForDeviceDTO adminUser) {
        this.adminUser = adminUser;
    }

    public List<UserForDeviceDTO> getAccessUsers() {
        return accessUsers;
    }

    public void setAccessUsers(List<UserForDeviceDTO> accessUsers) {
        this.accessUsers = accessUsers;
    }
}
