package com.liciot.devicems.wshandlerchain.chain.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.JsonObject;
import com.liciot.devicems.pool.devicepool.DevicePool;

import com.liciot.devicems.wshandlerchain.chain.WebSocketHandlerChain;
import com.liciot.devicems.wshandlerchain.entity.HandlerChainEntityType;
import com.liciot.devicems.wshandlerchain.entity.ToWSHandlerChainEntity;
import com.liciot.devicems.wshandlerchain.entity.wsmessage.received.StateMessageEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

@Component
public class WebSocketStateMessageHandler implements WebSocketChainHandler{

    @Autowired
    DevicePool devicePool;
    @Override
    public void handle(ToWSHandlerChainEntity entity, WebSocketHandlerChain webSocketHandlerChain, int currentIndex) {

        try {
            System.out.println("[WebSocketCapabilityMessageHandler]");
            if (entity.getEntityType().equals(HandlerChainEntityType.WS_TEXT_MESSAGE)) {
                System.out.println("[WebSocketCapabilityMessageHandler]: new message " + entity.getTextMessage());

                    ObjectMapper objectMapper = new ObjectMapper();



                StateMessageEntity stateMessageEntity = objectMapper.readValue(entity.getTextMessage().getPayload(), StateMessageEntity.class);
                if(stateMessageEntity.getType()!=null && stateMessageEntity.getType().toLowerCase().equals("devicestate"))
                {
                    if(stateMessageEntity.getDeviceState()!=null )//&& message.A().toLowerCase().equals("authorization"))
                    {
                        UUID deviceId = UUID.fromString(stateMessageEntity.getDeviceState().getId());
                        JsonNode state = stateMessageEntity.getDeviceState().getState();
                        Iterator<String> attrIterator = state.fieldNames();
                        Map<String, String>  attributeMap = new HashMap<>();
                        ObjectWriter objectWriter = objectMapper.writer();
                        while(attrIterator.hasNext())
                        {
                            String attrId = attrIterator.next();
                            JsonNode attribute  = state.get(attrId);

                            ObjectNode toPublish  = objectMapper.createObjectNode();
                            toPublish.set("value", attribute.get("value"));
                            //attribute.get("value");
                            attributeMap.put(attrId,objectWriter.writeValueAsString(toPublish));
                        }

                        this.devicePool.handleDeviceState(deviceId, attributeMap);//UUID.fromString(capabilityMessageEntity.getDeviceCapability().getId()), objectWriter.writeValueAsString(capabilityMessageEntity.getDeviceCapability().getCapability() ), entity.getWebSocketSession());

                    }
                }
            }
        }catch (Exception e)
        {

            e.printStackTrace();
            System.out.println("[WebSocketAuthorizationMessageHandler] exception parsing the message");
        }
        finally {
            webSocketHandlerChain.doHandle(entity, currentIndex);
        }
    }
}
