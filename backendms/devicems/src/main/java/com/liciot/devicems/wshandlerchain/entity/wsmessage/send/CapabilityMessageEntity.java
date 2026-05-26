package com.liciot.devicems.wshandlerchain.entity.wsmessage.send;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CapabilityMessageEntity {
    String type;
    //ramane macr omventia asta valida pui json unde este json serializat
    JsonNode deviceCapability;// now it wil be only capabilityJOSN withouth device id like the fontend sends you must doument the API
    //urat ca frontendul trimite neserializat configuration in nAUNTRU LA DEVICEoNFIGURATION D EU DE IEI iDUL si il serializesi in java//
    //cred ca pentru viteza comfigutrtia o poti serializa in front


    public CapabilityMessageEntity(String type, JsonNode deviceCapability) {
        this.type = type;
        this.deviceCapability = deviceCapability;
    }

    public CapabilityMessageEntity() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public JsonNode getDeviceCapability() {
        return deviceCapability;
    }

    public void setDeviceCapability(JsonNode deviceCapability) {
        this.deviceCapability = deviceCapability;
    }
}
