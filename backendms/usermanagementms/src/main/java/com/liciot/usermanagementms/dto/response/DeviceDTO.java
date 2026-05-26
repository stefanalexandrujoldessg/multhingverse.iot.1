package com.liciot.usermanagementms.dto.response;

import java.util.List;
import java.util.UUID;

public class DeviceDTO {
    UUID id;
    String configurationJSON;
    UserForDeviceDTO adminUser;
    List<UserForDeviceDTO> accessUsers;

    public DeviceDTO(UUID id, String configurationJSON, UserForDeviceDTO adminUser, List<UserForDeviceDTO> accessUsers) {
        this.id = id;
        this.configurationJSON = configurationJSON;
        this.adminUser = adminUser;
        this.accessUsers = accessUsers;
    }

    public DeviceDTO() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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
