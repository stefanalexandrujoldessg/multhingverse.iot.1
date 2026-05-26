package com.liciot.humanms.wshandlerchain.chain.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.liciot.humanms.apiprovider.ApiProvider;
import com.liciot.humanms.pool.userpool.UserPoolInjector;
import com.liciot.humanms.service.ApiConsumer;
import com.liciot.humanms.wshandlerchain.chain.WebSocketHandlerChain;
import com.liciot.humanms.wshandlerchain.entity.HandlerChainEntityType;
import com.liciot.humanms.wshandlerchain.entity.ToWSHandlerChainEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import com.liciot.humanms.wshandlerchain.entity.wsmessage.received.AuthorizationMessageEntity;
import com.liciot.humanms.dto.response.user.UserInitializationDTO;

import java.time.Instant;
import java.util.Arrays;
import java.util.UUID;

@Component
public class WebSocketAuthorizationMessageHandler implements WebSocketChainHandler{
    @Autowired
    ApiConsumer apiConsumer;
    @Autowired
    UserPoolInjector userPool;
    @Autowired
    ApiProvider apiProvider;
    @Override
    public void handle(ToWSHandlerChainEntity entity, WebSocketHandlerChain webSocketHandlerChain, int currentIndex) {

        try {
            //specialSystem.out.println("[WebSocketAuthorizationMessageHandler]");
            if (entity.getEntityType().equals(HandlerChainEntityType.WS_TEXT_MESSAGE)) {
                //specialSystem.out.println("[WebSocketAuthorizationMessageHandler]: new message " + entity.getTextMessage());

                    ObjectMapper objectMapper = new ObjectMapper();



                AuthorizationMessageEntity message = objectMapper.readValue(entity.getTextMessage().getPayload(), AuthorizationMessageEntity.class);
                if(message.getType()!=null && message.getType().toLowerCase().equals("authorization"))
                {
                    if(message.getAuthorization()!=null )//&& message.A().toLowerCase().equals("authorization"))
                    {
                        if(message.getAuthorization().contains("Bearer "))
                        {
                           String token = message.getAuthorization().substring(message.getAuthorization().indexOf("Bearer ")+String.valueOf("Bearer ").length()).trim();
                            //specialSystem.out.println(token);
                           HttpHeaders headers = new HttpHeaders();
                            headers.put("Authorization", Arrays.asList("Bearer "+  token  ));
                            //specialSystem.out.println("Will make authorization request");
                            Long kafkaTimeOffset = Long.valueOf( Instant.now().toEpochMilli());
                            UserInitializationDTO userInitializationDTO =  this.apiConsumer.getForObject(apiProvider.getLiciotUserManagementMicroservice()+"/crud/user/getByToken/force", UserInitializationDTO.class, headers);

                            if(userInitializationDTO.getId()!=null)
                            {
                                 System.out.println("[User id]: "+userInitializationDTO.getId());
                            }
                            if(userInitializationDTO.getAdminDevicesIds()!=null)
                            {
                                 System.out.println("[Admin devices]: ");
                                for ( UUID ac: userInitializationDTO.getAdminDevicesIds())
                                {
                                     System.out.println(ac);
                                }
                            }
                            if(userInitializationDTO.getAccessDevicesIds()!=null)
                            {
                                 System.out.println("[Access devices]: ");
                                for ( UUID ac: userInitializationDTO.getAccessDevicesIds())
                                {
                                     System.out.println(ac);
                                }
                            }

                            this.userPool.addUser(userInitializationDTO.getId(), userInitializationDTO.getAdminDevicesIds(),userInitializationDTO.getAccessDevicesIds(), entity.getWebSocketSession(), kafkaTimeOffset);
                        }
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
