package com.liciot.devicems.dto;

import java.util.List;
import java.util.UUID;

public class AccessUsersForDeviceBody {
    UUID deviceId;
    List<UUID> accessUsersId;

    public AccessUsersForDeviceBody(UUID deviceId, List<UUID> accessUsersId) {
        this.deviceId = deviceId;
        this.accessUsersId = accessUsersId;
    }

    public AccessUsersForDeviceBody() {
    }

    public UUID getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(UUID deviceId) {
        this.deviceId = deviceId;
    }

    public List<UUID> getAccessUsersId() {
        return accessUsersId;
    }

    public void setAccessUsersId(List<UUID> accessUsersId) {
        this.accessUsersId = accessUsersId;
    }
}
