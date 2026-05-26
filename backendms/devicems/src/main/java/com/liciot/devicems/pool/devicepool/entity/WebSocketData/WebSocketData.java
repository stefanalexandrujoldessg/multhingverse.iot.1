package com.liciot.devicems.pool.devicepool.entity.WebSocketData;

import com.liciot.devicems.pool.devicepool.Device;
import org.springframework.web.socket.WebSocketSession;

import java.time.LocalDateTime;

public class WebSocketData {


    Device device;
    WebSocketSession webSocketSession;
    LocalDateTime expire;
    LocalDateTime sendOnline;

    public WebSocketData(Device device, WebSocketSession webSocketSession, LocalDateTime expire, LocalDateTime sendOnline) {
        this.device = device;
        this.webSocketSession = webSocketSession;
        this.expire = expire;
        this.sendOnline = sendOnline;
    }



    public WebSocketData() {
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public LocalDateTime getExpire() {
        return expire;
    }

    public void setExpire(LocalDateTime expire) {
        this.expire = expire;
    }

    public LocalDateTime getSendOnline() {
        return sendOnline;
    }

    public void setSendOnline(LocalDateTime sendOnline) {
        this.sendOnline = sendOnline;
    }

    public WebSocketSession getWebSocketSession() {
        return webSocketSession;
    }

    public void setWebSocketSession(WebSocketSession webSocketSession) {
        this.webSocketSession = webSocketSession;
    }
}
