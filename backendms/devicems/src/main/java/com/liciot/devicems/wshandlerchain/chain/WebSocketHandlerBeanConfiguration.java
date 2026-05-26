package com.liciot.devicems.wshandlerchain.chain;

import com.liciot.devicems.wshandlerchain.chain.handler.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebSocketHandlerBeanConfiguration {
@Autowired
    WebSocketConnectionEstablishedHandler webSocketConnectionEstablishedHandler;
@Autowired
    WebSocketAuthorizationMessageHandler webSocketAuthorizationMessageHandler;
    @Autowired
    WebSocketHeartbeatMessageTerminatorHandler webSocketHeartbeatMessageTerminatorHandler;
    @Autowired
    WebSocketPrettyPrintHandler webSocketPrettyPrintHandler;
    @Autowired
    WebSocketConnectionClosedHandler webSocketConnectionClosedHandler;
    @Autowired
    WebSocketStateMessageHandler webSocketStateMessageHandler;
    @Bean
    public WebSocketHandlerChain getWebSocketHandlerChain()
    {
        WebSocketHandlerChain webSocketHandlerChain = new WebSocketHandlerChain();
        webSocketHandlerChain.addWebSocketChainHandler(this.webSocketHeartbeatMessageTerminatorHandler);
        webSocketHandlerChain.addWebSocketChainHandler(this.webSocketConnectionEstablishedHandler);
        webSocketHandlerChain.addWebSocketChainHandler(this.webSocketAuthorizationMessageHandler);
        webSocketHandlerChain.addWebSocketChainHandler(this.webSocketConnectionClosedHandler);
        webSocketHandlerChain.addWebSocketChainHandler(this.webSocketStateMessageHandler);

        webSocketHandlerChain.addWebSocketChainHandler(this.webSocketPrettyPrintHandler);

        return  webSocketHandlerChain;

    }
}
