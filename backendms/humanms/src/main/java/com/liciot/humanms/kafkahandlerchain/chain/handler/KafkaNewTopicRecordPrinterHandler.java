package com.liciot.humanms.kafkahandlerchain.chain.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.liciot.humanms.dto.response.user.UserInitializationDTO;
import com.liciot.humanms.kafkahandlerchain.chain.KafkaHandlerChain;
import com.liciot.humanms.kafkahandlerchain.entity.HandlerChainEntityType;
import com.liciot.humanms.kafkahandlerchain.entity.ToKafkaHandlerChainEntity;
import com.liciot.humanms.kafkahandlerchain.entity.wsmessage.AuthorizationMessageEntity;
import com.liciot.humanms.pool.userpool.UserPool;
import com.liciot.humanms.service.ApiConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.UUID;

@Component
public class KafkaNewTopicRecordPrinterHandler implements KafkaChainHandler {
  //  @Autowired
   // ApiConsumer apiConsumer;
   // @Autowired
   // UserPool userPool;
    @Override
    public void handle(ToKafkaHandlerChainEntity entity, KafkaHandlerChain kafkaHandlerChain, int currentIndex) {

        try {
            //special//specialSystem.out.println("[KafkaNewTopicRecordPrinterHandler]");
            if (entity.getEntityType().equals(HandlerChainEntityType.TOPIC_NEW_RECORD)) {
                //specialSystem.out.println("[KafkaNewTopicRecordPrinterHandler]: new topic records "  );
                for (ConsumerRecord<String, String> record: entity.getRecords()) {
                    //specialSystem.out.println("Topic: "+record.topic()+" "+record.key()+" : "+record.value());
                   // ObjectMapper objectMapper = new ObjectMapper();
                }

/*
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
                            UserInitializationDTO userInitializationDTO =  this.apiConsumer.getForObject("http://localhost:9090/crud/user/getByToken/force", UserInitializationDTO.class, headers);

                            if(userInitializationDTO.getId()!=null)
                            {
                                //specialSystem.out.println("[User id]: "+userInitializationDTO.getId());
                            }
                            if(userInitializationDTO.getAdminDevicesIds()!=null)
                            {
                                //specialSystem.out.println("[Admin devices]: ");
                                for ( UUID ac: userInitializationDTO.getAdminDevicesIds())
                                {
                                    //specialSystem.out.println(ac);
                                }
                            }
                            if(userInitializationDTO.getAccessDevicesIds()!=null)
                            {
                                //specialSystem.out.println("[Access devices]: ");
                                for ( UUID ac: userInitializationDTO.getAccessDevicesIds())
                                {
                                    //specialSystem.out.println(ac);
                                }
                            }

                            this.userPool.addUser(userInitializationDTO.getId(), userInitializationDTO.getAdminDevicesIds(),userInitializationDTO.getAccessDevicesIds(), entity.getWebSocketSession());
                        }
                    }
                }

 */
            }


        }catch (Exception e)
        {

            e.printStackTrace();
            //specialSystem.out.println("[KafkaNewTopicRecordHandler] exception parsing the message");
        }
        finally {
            kafkaHandlerChain.doHandle(entity, currentIndex);
        }
    }
}
