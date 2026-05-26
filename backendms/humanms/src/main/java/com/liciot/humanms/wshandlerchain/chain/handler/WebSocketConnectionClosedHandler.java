package com.liciot.humanms.wshandlerchain.chain.handler;

import com.liciot.humanms.pool.userpool.UserPool;
import com.liciot.humanms.wshandlerchain.chain.WebSocketHandlerChain;
import com.liciot.humanms.wshandlerchain.entity.HandlerChainEntityType;
import com.liciot.humanms.wshandlerchain.entity.ToWSHandlerChainEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WebSocketConnectionClosedHandler implements WebSocketChainHandler{
    @Autowired
    UserPool userPool;
    @Override
    public void handle(ToWSHandlerChainEntity entity, WebSocketHandlerChain webSocketHandlerChain, int currentIndex) {
        try {
            //specialSystem.out.println("[WebSocketConnectionClosedHandler]");

            if (entity.getEntityType().equals(HandlerChainEntityType.WS_CONNECTION_CLOSED)) {
              System.out.println("[WebSocketConnectionClosedHandler]: connecction closed" + entity.getWebSocketSession());
                this.userPool.removeWebSocketSession(entity.getWebSocketSession());
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            webSocketHandlerChain.doHandle(entity, currentIndex);
        }
    }
}
