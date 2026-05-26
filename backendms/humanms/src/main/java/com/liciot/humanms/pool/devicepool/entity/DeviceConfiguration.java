package com.liciot.humanms.pool.devicepool.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.liciot.humanms.pool.devicepool.entity.Attribute;

import java.util.Map;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DeviceConfiguration {
    UUID id;// or string ?
    Map<String, Attribute> state;//<attributeId, Value json objject>

    public DeviceConfiguration(UUID id, Map<String, Attribute> state) {
        this.id = id;
        this.state = state;
    }

    public Map<String, Attribute> getState() {

        return state;
    }

    public void setState(Map<String, Attribute> state) {
        this.state = state;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public DeviceConfiguration() {
     }
    @Override
    public String toString()
    {
        return this.id+ " state: " +state.toString();
    }

}
