package com.liciot.devicems.kafkahandlerchain.chain.handler;

import com.liciot.devicems.kafkahandlerchain.chain.KafkaHandlerChain;
import com.liciot.devicems.kafkahandlerchain.entity.HandlerChainEntityType;
import com.liciot.devicems.kafkahandlerchain.entity.ToKafkaHandlerChainEntity;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Component;

@Component
public class KafkaNewTopicRecordPrinterHandler implements KafkaChainHandler {
  //  @Autowired
   // ApiConsumer apiConsumer;
   // @Autowired
   // UserPool userPool;
    @Override
    public void handle(ToKafkaHandlerChainEntity entity, KafkaHandlerChain kafkaHandlerChain, int currentIndex) {

        try {
            System.out.println("[KafkaNewTopicRecordPrinterHandler]");
            if (entity.getEntityType().equals(HandlerChainEntityType.TOPIC_NEW_RECORD)) {
                System.out.println("[KafkaNewTopicRecordPrinterHandler]: new topic records "  );
                for (ConsumerRecord<String, String> record: entity.getRecords()) {
                    System.out.println("Topic: "+record.topic()+" "+record.key()+" : "+record.value());
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
                            System.out.println(token);
                           HttpHeaders headers = new HttpHeaders();
                            headers.put("Authorization", Arrays.asList("Bearer "+  token  ));
                            System.out.println("Will make authorization request");
                            UserInitializationDTO userInitializationDTO =  this.apiConsumer.getForObject("http://localhost:9090/crud/user/getByToken/force", UserInitializationDTO.class, headers);

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

                            this.userPool.addUser(userInitializationDTO.getId(), userInitializationDTO.getAdminDevicesIds(),userInitializationDTO.getAccessDevicesIds(), entity.getWebSocketSession());
                        }
                    }
                }

 */
            }


        }catch (Exception e)
        {

            e.printStackTrace();
            System.out.println("[KafkaNewTopicRecordHandler] exception parsing the message");
        }
        finally {
            kafkaHandlerChain.doHandle(entity, currentIndex);
        }
    }
}
