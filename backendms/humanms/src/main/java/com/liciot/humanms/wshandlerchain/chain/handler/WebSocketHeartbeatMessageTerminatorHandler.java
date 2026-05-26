package com.liciot.humanms.wshandlerchain.chain.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.liciot.humanms.pool.userpool.UserPool;
import com.liciot.humanms.wshandlerchain.chain.WebSocketHandlerChain;
import com.liciot.humanms.wshandlerchain.entity.HandlerChainEntityType;
import com.liciot.humanms.wshandlerchain.entity.ToWSHandlerChainEntity;
import com.liciot.humanms.wshandlerchain.entity.wsmessage.send.authorization.RequestAuthorizationMessageEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;

@Component
public class WebSocketHeartbeatMessageTerminatorHandler implements WebSocketChainHandler{
    //@Autowired
    //ApiConsumer apiConsumer;
    @Autowired
    UserPool userPool;
    @Override
    public void handle(ToWSHandlerChainEntity entity, WebSocketHandlerChain webSocketHandlerChain, int currentIndex) {


        boolean shouldTerminateChain = false;
        try {
            //specialSystem.out.println("[WebSocketHeartbeatMessageTerminatorHandler]");
            if (entity.getEntityType().equals(HandlerChainEntityType.WS_TEXT_MESSAGE)) {
                ////specialSystem.out.println("[WebSocketHeartbeatMessageTerminatorHandler]: new message " + entity.getTextMessage());

                if (entity.getTextMessage().getPayload().toLowerCase().equals("h")) {
                    //specialSystem.out.println("[WebSocketHeartbeatMessageTerminatorHandler]:  heartbeat detected");
                    shouldTerminateChain = true;
                    if(!this.userPool.isAuthorized(entity.getWebSocketSession()))
                    {
                        ObjectMapper objectMapper = new ObjectMapper();
                        synchronized (entity.getWebSocketSession()) { //notsure
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
            //specialSystem.out.println("[WebSocketHeartbeatMessageTerminatorHandler] exception parsing the message");
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
