package com.liciot.humanms.dto;

import java.util.UUID;

public class InsertDeviceBody {
    UUID deviceId;
    String configurationJSON;
    UUID adminUserId;
   //// String username;
    //String password;

    public InsertDeviceBody(UUID deviceId, String configurationJSON, UUID adminUserId) {
        this.deviceId = deviceId;
        this.configurationJSON = configurationJSON;
        this.adminUserId = adminUserId;
    }

    public InsertDeviceBody() {
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

    public UUID getAdminUserId() {
        return adminUserId;
    }

    public void setAdminUserId(UUID adminUserId) {
        this.adminUserId = adminUserId;
    }
}
