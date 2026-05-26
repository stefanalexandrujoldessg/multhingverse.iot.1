package com.liciot.humanms.wshandlerchain.entity.wsmessage.send.devices;

import com.fasterxml.jackson.databind.JsonNode;

public class DevicesMessageEntity {
    String type;
    JsonNode devices;



    public DevicesMessageEntity() {
    }

    public DevicesMessageEntity(String type, JsonNode devices) {
        this.type = type;
        this.devices = devices;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public JsonNode getDevices() {
        return devices;
    }

    public void setDevices(JsonNode devices) {
        this.devices = devices;
    }
}
