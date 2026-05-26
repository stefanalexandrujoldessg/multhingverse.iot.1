package com.liciot.devicems.wshandlerchain.entity.wsmessage.received;

import com.fasterxml.jackson.databind.JsonNode;

public class StatePayload {
String id;
JsonNode state;

    public StatePayload(String id, JsonNode state) {
        this.id = id;
        this.state = state;
    }

    public StatePayload() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public JsonNode getState() {
        return state;
    }

    public void setState(JsonNode state) {
        this.state = state;
    }
}
