package com.liciot.humanms.wshandlerchain.chain.handler;

import org.springframework.stereotype.Component;
import com.liciot.humanms.wshandlerchain.entity.HandlerChainEntityType;
import com.liciot.humanms.wshandlerchain.entity.ToWSHandlerChainEntity;
import com.liciot.humanms.wshandlerchain.chain.WebSocketHandlerChain;
@Component
public class WebSocketConnectionEstablishedHandler implements WebSocketChainHandler{
    @Override
    public void handle(ToWSHandlerChainEntity entity, WebSocketHandlerChain webSocketHandlerChain, int currentIndex) {
        try {
            //specialSystem.out.println("[WebSocketConnectionEstablishedHandler]");

            if (entity.getEntityType().equals(HandlerChainEntityType.WS_CONNECTION_ESTABLISHED)) {
                //specialSystem.out.println("[WebSocketConnectionEstablishedHandler]: new connection established " + entity.getWebSocketSession().getHandshakeHeaders().toString());
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
