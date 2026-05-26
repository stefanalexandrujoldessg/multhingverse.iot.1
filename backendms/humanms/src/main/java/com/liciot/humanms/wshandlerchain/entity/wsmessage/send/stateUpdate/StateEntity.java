package com.liciot.humanms.wshandlerchain.entity.wsmessage.send.stateUpdate;

import java.util.HashMap;
import java.util.Map;

public class StateEntity {

    Map<String, AttributeEntity> state = new HashMap<>();

    public Map<String, AttributeEntity> getState() {
        return state;
    }

    public void setState(Map<String, AttributeEntity> state) {
        this.state = state;
    }

    public StateEntity(Map<String, AttributeEntity> state) {
        this.state = state;
    }

    public StateEntity() {
    }
}
