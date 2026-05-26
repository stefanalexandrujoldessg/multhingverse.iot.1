package com.liciot.usermanagementms.dto.response.device;

public class DeviceIdentificationDTO {
    String name ;

    public DeviceIdentificationDTO(String name) {
        this.name = name;
    }

    public DeviceIdentificationDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
