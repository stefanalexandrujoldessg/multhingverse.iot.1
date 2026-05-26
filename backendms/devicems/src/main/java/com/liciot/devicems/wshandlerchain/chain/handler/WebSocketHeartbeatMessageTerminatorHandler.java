package com.liciot.devicems.wshandlerchain.chain.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.liciot.devicems.pool.devicepool.DevicePool;
import com.liciot.devicems.wshandlerchain.chain.WebSocketHandlerChain;
import com.liciot.devicems.wshandlerchain.entity.HandlerChainEntityType;
import com.liciot.devicems.wshandlerchain.entity.ToWSHandlerChainEntity;
import com.liciot.devicems.wshandlerchain.entity.wsmessage.send.authorization.RequestAuthorizationMessageEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;

@Component
public class WebSocketHeartbeatMessageTerminatorHandler implements WebSocketChainHandler{
    //@Autowired
    //ApiConsumer apiConsumer;
    @Autowired
    DevicePool devicePool;
    @Override
    public void handle(ToWSHandlerChainEntity entity, WebSocketHandlerChain webSocketHandlerChain, int currentIndex) {


        boolean shouldTerminateChain = false;
        try {
            System.out.println("[WebSocketHeartbeatMessageTerminatorHandler]");
            if (entity.getEntityType().equals(HandlerChainEntityType.WS_TEXT_MESSAGE)) {
             //   System.out.println("[WebSocketHeartbeatMessageTerminatorHandler]: new message " + entity.getTextMessage().getPayload().toString());

                if (entity.getTextMessage().getPayload().toLowerCase().equals("h")) {
                    System.out.println("[WebSocketHeartbeatMessageTerminatorHandler]:  heartbeat detected "+entity.getWebSocketSession().getId());
                    shouldTerminateChain = true;
                    if(!this.devicePool.isAuthorized(entity.getWebSocketSession()))
                    {
                        ObjectMapper objectMapper = new ObjectMapper();
                        synchronized (entity.getWebSocketSession()) {
                            entity.getWebSocketSession().sendMessage(new TextMessage(objectMapper.writerFor(RequestAuthorizationMessageEntity.class).writeValueAsString(new RequestAuthorizationMessageEntity())));
                        }
                    }
                    //return ;
                    //here we stop the execution of the chain
                }
            }
        }catch (Exception e)
        {
            //e.printStackTrace();
            System.out.println("[WebSocketHeartbeatMessageTerminatorHandler] exception parsing the message");
        }
        finally {
            //daca as fi pus doar doHanle aici si retun mi sus in fi nu mergea ca ce este in finally se pareca se executa neapoarat si daca dai return;
            if(shouldTerminateChain)
            {
                return;
            }
            else
            {
                webSocketHandlerChain.doHandle(entity, currentIndex);

            }
        }

    }
}
