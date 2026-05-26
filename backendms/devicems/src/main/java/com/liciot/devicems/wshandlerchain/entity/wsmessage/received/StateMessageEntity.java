package com.liciot.devicems.wshandlerchain.entity.wsmessage.received;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StateMessageEntity {

    //deviceCapability//asa ramane
    String type;// stiu ca am zis csa pastram daca pun la type auth sa caut cam p auth dar aici va di dubios c doua key la doua nivdluridiferite ccapability
    //hai sa pastram conventia unde pun json ca string serializat sA RAMANCA    cevanumeJSON
    //ex si la configuratie avei ceva discrepante asifuraet ca este peste tot la fel unde e cevaJSON e string unde e ceva este obiectul in JS si mapat aici sici va fi direct json pt ca pe mine nu ma intereseaza nimic pun exact ce primesc ba ma intereseaza j  dev ID
   //aici nu ba mai fi cazul de
StatePayload deviceState;// aici nu e ca la capability in font pt ca acolo trebuie sa / nu si aiaci trbee ca in caz de hub trebue ie saidentifica

    public StateMessageEntity() {
    }

    public StateMessageEntity(String type, StatePayload deviceState) {
        this.type = type;
        this.deviceState = deviceState;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public StatePayload getDeviceState() {
        return deviceState;
    }

    public void setDeviceState(StatePayload deviceState) {
        this.deviceState = deviceState;
    }
}
