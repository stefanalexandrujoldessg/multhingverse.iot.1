package com.liciot.usermanagementms.dto.response.user;

import java.util.List;
import java.util.UUID;

public class UserInitializationDTO {
    UUID id;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public List<UUID> getAdminDevicesIds() {
        return adminDevicesIds;
    }

    public void setAdminDevicesIds(List<UUID> adminDevicesIds) {
        this.adminDevicesIds = adminDevicesIds;
    }

    public List<UUID> getAccessDevicesIds() {
        return accessDevicesIds;
    }

    public void setAccessDevicesIds(List<UUID> accessDevicesIds) {
        this.accessDevicesIds = accessDevicesIds;
    }

    List<UUID> adminDevicesIds;
    List<UUID> accessDevicesIds;

    public UserInitializationDTO() {
    }

    public UserInitializationDTO(UUID id, List<UUID> adminDevicesIds, List<UUID> accessDevicesIds) {
        this.id = id;
        this.adminDevicesIds = adminDevicesIds;
        this.accessDevicesIds = accessDevicesIds;
    }
}