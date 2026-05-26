package com.liciot.humanms.controller.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import com.liciot.humanms.wshandlerchain.chain.WebSocketHandlerChain;
import com.liciot.humanms.wshandlerchain.entity.HandlerChainEntityType;
import com.liciot.humanms.wshandlerchain.entity.ToWSHandlerChainEntity;

import java.util.concurrent.ExecutorService;

@Component
public class WebSocketHandler extends TextWebSocketHandler {
@Autowired
    WebSocketHandlerChain webSocketHandlerChain;
@Autowired
    ExecutorService executorService;
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        ToWSHandlerChainEntity entity = new ToWSHandlerChainEntity();
        entity.setEntityType(HandlerChainEntityType.WS_CONNECTION_ESTABLISHED);
        entity.setWebSocketSession(session);
       executorService.execute(()->{ this.webSocketHandlerChain.doHandle(entity);});
     }
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        ToWSHandlerChainEntity entity = new ToWSHandlerChainEntity();
        entity.setEntityType(HandlerChainEntityType.WS_CONNECTION_CLOSED);
        entity.setWebSocketSession(session);
        executorService.execute(()->{ this.webSocketHandlerChain.doHandle(entity);});
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage textMessage)
    {
        ToWSHandlerChainEntity entity = new ToWSHandlerChainEntity();
        entity.setEntityType(HandlerChainEntityType.WS_TEXT_MESSAGE);
        entity.setWebSocketSession(session);
        entity.setTextMessage(textMessage);
        executorService.execute(()->{ this.webSocketHandlerChain.doHandle(entity);});
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        //specialSystem.out.println("[Web socket transport error]");
        super.handleTransportError(session, exception);
    }
}
