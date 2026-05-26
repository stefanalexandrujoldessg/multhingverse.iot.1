package com.liciot.humanms.wshandlerchain.entity.wsmessage.send.stateUpdate;

import com.fasterxml.jackson.databind.JsonNode;

public class AttributeEntity {
    public JsonNode getValue() {
        return value;
    }

    public void setValue(JsonNode value) {
        this.value = value;
    }

    JsonNode value;

    public AttributeEntity() {
    }

    public AttributeEntity(JsonNode value) {
        this.value = value;
    }
}
