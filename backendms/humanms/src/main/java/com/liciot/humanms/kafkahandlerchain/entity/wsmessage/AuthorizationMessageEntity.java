package com.liciot.humanms.kafkahandlerchain.entity.wsmessage;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthorizationMessageEntity {
    String type;
    String authorization;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAuthorization() {
        return authorization;
    }

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }

    public AuthorizationMessageEntity() {
    }

    public AuthorizationMessageEntity(String type, String authorization) {
        this.type = type;
        this.authorization = authorization;
    }
}
