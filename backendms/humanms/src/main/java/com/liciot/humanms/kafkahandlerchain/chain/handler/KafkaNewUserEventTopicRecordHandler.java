package com.liciot.humanms.kafkahandlerchain.chain.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liciot.humanms.kafkahandlerchain.chain.KafkaHandlerChain;
import com.liciot.humanms.kafkahandlerchain.entity.HandlerChainEntityType;
import com.liciot.humanms.kafkahandlerchain.entity.ToKafkaHandlerChainEntity;
import com.liciot.humanms.pool.devicepool.DevicePool;
import com.liciot.humanms.pool.userpool.User;
import com.liciot.humanms.pool.userpool.UserPool;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Component
public class KafkaNewUserEventTopicRecordHandler implements KafkaChainHandler {
  //  @Autowiredc
   // ApiConsumer apiConsumer;
    @Autowired
  UserPool userPool;
    @Override
    public void handle(ToKafkaHandlerChainEntity entity, KafkaHandlerChain kafkaHandlerChain, int currentIndex) {

        try {
            //specialSystem.out.println("[KafkaNewUserEventTopicRecordHandler]");
            if (entity.getEntityType().equals(HandlerChainEntityType.TOPIC_NEW_RECORD)) {
                //specialSystem.out.println("[KafkaNewUserEventTopicRecordHandler]: new topic records "  );
                for (ConsumerRecord<String, String> record: entity.getRecords()) {
                    //kafka puts the timetamp at UTC 0 sa i convert it to compare with my local ,shine tiome
                  //  //specialSystem.out.println("Topic: "+record.topic()+" "+record.key()+" : "+record.value());//+"timestamptype: "+record.timestampType()+" "+record.timestamp()+" "+ZoneId.systemDefault().getRules().getOffset(Instant.now())+" "+ LocalDateTime.ofInstant(Instant.ofEpochMilli(record.timestamp()), ZoneId.systemDefault()));
                    //ecord.
                   // ZoneOffset o = ZoneId.systemDefault().getRules().getOffset(Instant.now());


                    try {
                        String topicName = record.topic();
                        List<String> tokens =  Arrays.asList(topicName.split("\\."));
                        //specialSystem.out.println("Tokens");
                        for ( String token : tokens)
                        {
                            //specialSystem.out.println(token);
                        }
                        if( tokens.size()>=3)
                        { //specialSystem.out.println("it is ok");
                            if( tokens.get(0).toLowerCase().equals("user"))
                            { //specialSystem.out.println("it is ok");
                                if( tokens.get(1).toLowerCase().equals("event"))
                                {
                                    //specialSystem.out.println("it is ok");
                                    UUID userId = UUID.fromString(tokens.get(2));
                                   // String attributeId = tokens.get(3);




                                    {
                                        ObjectMapper objectMapper = new ObjectMapper();
                                        JsonNode jsonNode = objectMapper.readValue(record.value(),JsonNode.class);
                                        //specialSystem.out.println("[device]"+ " "+jsonNode.get("type"));

                                        if(jsonNode.has("type"))
                                        {
                                            //specialSystem.out.println("[device]2"+ " "+jsonNode.get("type"));
                                            if(objectMapper.treeToValue(jsonNode.get("type"),String.class).equals("deviceAdded")) {
                                                    //specialSystem.out.println("[deviceAdded]");
                                                this.userPool.handleDeviceAdded(userId, (UUID.fromString(objectMapper.treeToValue(jsonNode.get("deviceAdded"),String.class))));
                                            }
                                            if(objectMapper.treeToValue(jsonNode.get("type"),String.class).equals("deviceRemoved")) {
                                                //specialSystem.out.println("[deviceRemoved]");
                                                this.userPool.handleDeviceRemoved(userId, (UUID.fromString(objectMapper.treeToValue(jsonNode.get("deviceRemoved"),String.class))));
                                            }
                                            if(objectMapper.treeToValue(jsonNode.get("type"),String.class).equals("userDeviceConfigurationUpdated")) {
                                                //specialSystem.out.println("[userDeviceConfigurationUpdated]");
                                                this.userPool.handleUserDeviceConfigurationUpdated(userId, jsonNode.get("userDeviceConfigurationUpdated"));
                                            }
                                        }
                                    }

                                }

                            }
                        }

                    } catch (Exception ex){
                        ex.printStackTrace();
                    }



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
