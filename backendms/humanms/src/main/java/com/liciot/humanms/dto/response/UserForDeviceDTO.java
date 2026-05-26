package com.liciot.humanms.dto.response;

import java.util.UUID;

public class UserForDeviceDTO {
UUID id;
String username;

    public UserForDeviceDTO(UUID id, String username) {
        this.id = id;
        this.username = username;
    }

    public UserForDeviceDTO() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
