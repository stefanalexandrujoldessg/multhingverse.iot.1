package com.liciot.humanms.wshandlerchain.entity.wsmessage.received;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CapabilityMessageEntity {

    //deviceCapability//asa ramane
    String type;// stiu ca am zis csa pastram daca pun la type auth sa caut cam p auth dar aici va di dubios c doua key la doua nivdluridiferite ccapability
    //hai sa pastram conventia unde pun json ca string serializat sA RAMANCA    cevanumeJSON
    //ex si la configuratie avei ceva discrepante asifuraet ca este peste tot la fel unde e cevaJSON e string unde e ceva este obiectul in JS si mapat aici sici va fi direct json pt ca pe mine nu ma intereseaza nimic pun exact ce primesc ba ma intereseaza j  dev ID
   //aici nu ba mai fi cazul de
    CapabilityPayload deviceCapability;

    public CapabilityMessageEntity(String type, CapabilityPayload deviceCapability) {
        this.type = type;
        this.deviceCapability = deviceCapability;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public CapabilityPayload getDeviceCapability() {
        return deviceCapability;
    }

    public void setDeviceCapability(CapabilityPayload deviceCapability) {
        this.deviceCapability = deviceCapability;
    }

    public CapabilityMessageEntity() {
    }
}
