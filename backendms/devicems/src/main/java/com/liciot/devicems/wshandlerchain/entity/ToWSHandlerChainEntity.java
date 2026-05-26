package com.liciot.devicems.wshandlerchain.entity;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

public class ToWSHandlerChainEntity {
    HandlerChainEntityType entityType;
    WebSocketSession webSocketSession;
    TextMessage textMessage;
    CloseStatus closeStatus;
    public ToWSHandlerChainEntity(    ) {

    }

    public ToWSHandlerChainEntity(HandlerChainEntityType entityType, WebSocketSession webSocketSession, TextMessage textMessage, CloseStatus closeStatus) {
        this.entityType = entityType;
        this.webSocketSession = webSocketSession;
        this.textMessage = textMessage;
        this.closeStatus = closeStatus;
    }

    public HandlerChainEntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(HandlerChainEntityType entityType) {
        this.entityType = entityType;
    }

    public WebSocketSession getWebSocketSession() {
        return webSocketSession;
    }

    public void setWebSocketSession(WebSocketSession webSocketSession) {
        this.webSocketSession = webSocketSession;
    }

    public TextMessage getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(TextMessage textMessage) {
        this.textMessage = textMessage;
    }

    public CloseStatus getCloseStatus() {
        return closeStatus;
    }

    public void setCloseStatus(CloseStatus closeStatus) {
        this.closeStatus = closeStatus;
    }
}
