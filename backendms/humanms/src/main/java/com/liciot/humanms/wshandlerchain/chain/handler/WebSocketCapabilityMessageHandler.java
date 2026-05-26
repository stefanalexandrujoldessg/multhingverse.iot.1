package com.liciot.humanms.wshandlerchain.chain.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.liciot.humanms.pool.userpool.UserPool;
import com.liciot.humanms.service.ApiConsumer;
import com.liciot.humanms.wshandlerchain.chain.WebSocketHandlerChain;
import com.liciot.humanms.wshandlerchain.entity.HandlerChainEntityType;
import com.liciot.humanms.wshandlerchain.entity.ToWSHandlerChainEntity;
import com.liciot.humanms.wshandlerchain.entity.wsmessage.received.CapabilityMessageEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class WebSocketCapabilityMessageHandler implements WebSocketChainHandler{
    @Autowired
    ApiConsumer apiConsumer;
    @Autowired
    UserPool userPool;
    @Override
    public void handle(ToWSHandlerChainEntity entity, WebSocketHandlerChain webSocketHandlerChain, int currentIndex) {

        try {
            //specialSystem.out.println("[WebSocketCapabilityMessageHandler]");
            if (entity.getEntityType().equals(HandlerChainEntityType.WS_TEXT_MESSAGE)) {
                //specialSystem.out.println("[WebSocketCapabilityMessageHandler]: new message " + entity.getTextMessage());

                    ObjectMapper objectMapper = new ObjectMapper();



                CapabilityMessageEntity capabilityMessageEntity = objectMapper.readValue(entity.getTextMessage().getPayload(), CapabilityMessageEntity.class);
                if(capabilityMessageEntity.getType()!=null && capabilityMessageEntity.getType().toLowerCase().equals("devicecapability"))
                {
                    if(capabilityMessageEntity.getDeviceCapability()!=null )//&& message.A().toLowerCase().equals("authorization"))
                    {

                        ObjectWriter objectWriter = objectMapper.writer();
                        this.userPool.handleDeviceCapability(UUID.fromString(capabilityMessageEntity.getDeviceCapability().getId()), objectWriter.writeValueAsString(capabilityMessageEntity.getDeviceCapability().getCapability() ), entity.getWebSocketSession());

                    }
                }
            }
        }catch (Exception e)
        {

            e.printStackTrace();
            //specialSystem.out.println("[WebSocketAuthorizationMessageHandler] exception parsing the message");
        }
        finally {
            webSocketHandlerChain.doHandle(entity, currentIndex);
        }
    }
}
