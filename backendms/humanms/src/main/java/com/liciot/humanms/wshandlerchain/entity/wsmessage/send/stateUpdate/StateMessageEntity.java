package com.liciot.humanms.wshandlerchain.entity.wsmessage.send.stateUpdate;

import java.util.HashMap;
import java.util.Map;

public class StateMessageEntity {
    String type;
    Map<String, StateEntity> devices = new HashMap<>();

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, StateEntity> getDevices() {
        return devices;
    }

    public void setDevices(Map<String, StateEntity> devices) {
        this.devices = devices;
    }

    public StateMessageEntity() {
    }

    public StateMessageEntity(String type) {
        this.type = type;
    }

    public StateMessageEntity(String type, Map<String, StateEntity> devices) {
        this.type = type;
        this.devices = devices;
    }
}
