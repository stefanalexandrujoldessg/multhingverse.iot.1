package com.liciot.usermanagementms.dto;

import java.util.UUID;

public class InsertDeviceWithUsernameBody {
    UUID deviceId;
    String configurationJSON;
    String  adminUserUsername;// va trevui dupa username este msi usor pt usert cand conf disozitivulawa
   //// String username;
    //String password;

    public InsertDeviceWithUsernameBody(UUID deviceId, String configurationJSON, String adminUserUsername) {
        this.deviceId = deviceId;
        this.configurationJSON = configurationJSON;
        this.adminUserUsername = adminUserUsername;
    }

    public InsertDeviceWithUsernameBody() {
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

    public String getAdminUserUsername() {
        return adminUserUsername;
    }

    public void setAdminUserUsername(String adminUserUsername) {
        this.adminUserUsername = adminUserUsername;
    }
}
