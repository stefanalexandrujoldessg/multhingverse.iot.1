package com.liciot.usermanagementms.dto.request;

public class UserDeviceConfigurationDTO {
    String userId;
    String deviceId;
    String configurationJSON;

    public UserDeviceConfigurationDTO() {
    }

    public UserDeviceConfigurationDTO(String userId, String deviceId, String configurationJSON) {
        this.userId = userId;
        this.deviceId = deviceId;
        this.configurationJSON = configurationJSON;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getConfigurationJSON() {
        return configurationJSON;
    }

    public void setConfigurationJSON(String configurationJSON) {
        this.configurationJSON = configurationJSON;
    }
}
