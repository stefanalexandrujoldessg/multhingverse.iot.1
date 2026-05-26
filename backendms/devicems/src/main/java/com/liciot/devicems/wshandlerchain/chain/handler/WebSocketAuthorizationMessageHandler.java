package com.liciot.devicems.wshandlerchain.chain.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.liciot.devicems.apiprovider.ApiProvider;
import com.liciot.devicems.dto.response.device.DeviceConfigurationDTO;
import com.liciot.devicems.pool.devicepool.DevicePool;

import com.liciot.devicems.pool.devicepool.DevicePoolInjector;
import com.liciot.devicems.service.ApiConsumer;
import com.liciot.devicems.wshandlerchain.chain.WebSocketHandlerChain;
import com.liciot.devicems.wshandlerchain.entity.HandlerChainEntityType;
import com.liciot.devicems.wshandlerchain.entity.ToWSHandlerChainEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import com.liciot.devicems.wshandlerchain.entity.wsmessage.received.AuthorizationMessageEntity;

import java.util.UUID;
/// we need to have support for multi evice for websocket session thath means hub support
@Component
public  class WebSocketAuthorizationMessageHandler implements WebSocketChainHandler{
    @Autowired
    ApiConsumer apiConsumer;
    @Autowired
    DevicePoolInjector devicePool;
    @Autowired
    ApiProvider apiProvider;
    @Override
    public void handle(ToWSHandlerChainEntity entity, WebSocketHandlerChain webSocketHandlerChain, int currentIndex) {

        try {
            System.out.println("[WebSocketAuthorizationMessageHandler]");
            if (entity.getEntityType().equals(HandlerChainEntityType.WS_TEXT_MESSAGE)) {
                System.out.println("[WebSocketAuthorizationMessageHandler]: new message " + entity.getTextMessage());

                    ObjectMapper objectMapper = new ObjectMapper();



                AuthorizationMessageEntity message = objectMapper.readValue(entity.getTextMessage().getPayload(), AuthorizationMessageEntity.class);
                if(message.getType()!=null && message.getType().toLowerCase().equals("authorization"))
                {
                    if(message.getAuthorization()!=null )//&& message.A().toLowerCase().equals("authorization"))
                    {
                        if(message.getAuthorization().contains("Id "))
                        {
                           String id = message.getAuthorization().substring(message.getAuthorization().indexOf("Id ")+String.valueOf("Id ").length()).trim();
                            System.out.println(id);
                           //HttpHeaders headers = new HttpHeaders();
                            //headers.put("Authorization", Arrays.asList("Id "+  token  ));
                            System.out.println("Will make authorization request");
                            ResponseEntity<DeviceConfigurationDTO> response=  this.apiConsumer.getFullResponseForObject(apiProvider.getLiciotUserManagementMicroservice()+"/crud/device/getConfigurationById/"+id, DeviceConfigurationDTO.class, new HttpHeaders());



                            if(response.getStatusCode().equals(HttpStatus.OK)) {
                                System.out.println("[Device id]: " + id + " deviceConfiguration: " + response.getBody());

                                UUID deviceId = UUID.fromString(id);
                                this.devicePool.addDevice(deviceId,response.getBody().getConfigurationJSON(), entity.getWebSocketSession());
                            }
                        }

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
