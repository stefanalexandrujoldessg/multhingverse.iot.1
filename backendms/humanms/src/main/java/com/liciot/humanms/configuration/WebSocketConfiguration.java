package com.liciot.humanms.configuration;

import com.liciot.humanms.controller.websocket.WebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@EnableScheduling
public class WebSocketConfiguration implements WebSocketConfigurer {
@Autowired
    WebSocketHandler webSocketHandler;
@Override
public void registerWebSocketHandlers(WebSocketHandlerRegistry registry)
{
    registry.addHandler(webSocketHandler,"/websocket").setAllowedOrigins("*");
    registry.addHandler(webSocketHandler,"/websocket/").setAllowedOrigins("*");

}


}
