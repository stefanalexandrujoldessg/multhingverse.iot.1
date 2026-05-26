package com.liciot.devicems.wshandlerchain.chain.handler;

import org.springframework.stereotype.Component;
import com.liciot.devicems.wshandlerchain.entity.HandlerChainEntityType;
import com.liciot.devicems.wshandlerchain.entity.ToWSHandlerChainEntity;
import com.liciot.devicems.wshandlerchain.chain.WebSocketHandlerChain;
@Component
public class WebSocketConnectionEstablishedHandler implements WebSocketChainHandler{
    @Override
    public void handle(ToWSHandlerChainEntity entity, WebSocketHandlerChain webSocketHandlerChain, int currentIndex) {
        try {
            System.out.println("[WebSocketConnectionEstablishedHandler]");

            if (entity.getEntityType().equals(HandlerChainEntityType.WS_CONNECTION_ESTABLISHED)) {
                System.out.println("[WebSocketConnectionEstablishedHandler]: new connection established " + entity.getWebSocketSession().getHandshakeHeaders().toString());
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
