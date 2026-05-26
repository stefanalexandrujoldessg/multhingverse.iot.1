package com.liciot.devicems.wshandlerchain.chain.handler;

import com.liciot.devicems.pool.devicepool.DevicePool;
//import com.liciot.devicems.pool.userpool.UserPool;
import com.liciot.devicems.wshandlerchain.chain.WebSocketHandlerChain;
import com.liciot.devicems.wshandlerchain.entity.HandlerChainEntityType;
import com.liciot.devicems.wshandlerchain.entity.ToWSHandlerChainEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WebSocketConnectionClosedHandler implements WebSocketChainHandler{
    @Autowired
    DevicePool devicePool;
    @Override
    public  void handle(ToWSHandlerChainEntity entity, WebSocketHandlerChain webSocketHandlerChain, int currentIndex) {
        try {
            System.out.println("[WebSocketConnectionClosedHandler]");

            if (entity.getEntityType().equals(HandlerChainEntityType.WS_CONNECTION_CLOSED)) {
                System.out.println("[WebSocketConnectionClosedHandler]: connecction closed" + entity.getWebSocketSession());
                this.devicePool.removeWebSocketSession(entity.getWebSocketSession());
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
