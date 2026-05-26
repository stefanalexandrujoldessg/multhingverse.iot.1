package com.liciot.devicems.pool.devicepool.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Attribute {
    JsonNode value;

    public JsonNode getValue() {
        return value;
    }

    public void setValue(JsonNode value) {
        this.value = value;
    }

    public Attribute() {
    }

    public Attribute(JsonNode value) {
        this.value = value;
    }
    @Override
    public String toString()
    {
        return this.value.toString();
    }
}
