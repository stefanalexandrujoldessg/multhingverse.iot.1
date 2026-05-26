package com.liciot.humanms.wshandlerchain.entity.wsmessage.received;

import com.fasterxml.jackson.databind.JsonNode;

public class CapabilityPayload {
    String id;
    JsonNode capability;//sas sa ramana nu cu string ca va fi mai comlicat dupa in font// = {capability:{ capid1:{}}...  de la capability nu ma mai intereseaza oricum


    public CapabilityPayload(String id, JsonNode capability) {
        this.id = id;
        this.capability = capability;
     }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public JsonNode getCapability() {
        return capability;
    }

    public void setCapability(JsonNode capability) {
        this.capability = capability;
    }

    public CapabilityPayload() {
    }
}
