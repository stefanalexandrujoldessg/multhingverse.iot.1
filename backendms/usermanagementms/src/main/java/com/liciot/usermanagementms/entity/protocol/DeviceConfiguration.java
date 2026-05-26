package com.liciot.usermanagementms.entity.protocol;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DeviceConfiguration {
    String id;
    Map<String,Attribute> state = new HashMap<>();

    public DeviceConfiguration() {
    }

    public DeviceConfiguration(String id, Map<String, Attribute> state) {
        this.id = id;
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, Attribute> getState() {
        return state;
    }

    public void setState(Map<String, Attribute> state) {
        this.state = state;
    }
}
