package com.liciot.devicems.pool.devicepool;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.liciot.devicems.kafka.consumer.KafkaConsumer;
import com.liciot.devicems.kafka.producer.KafkaProducer;
import com.liciot.devicems.pool.devicepool.entity.DeviceConfiguration;

import com.liciot.devicems.wshandlerchain.entity.wsmessage.send.CapabilityMessageEntity;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.*;

public class Device {
    UUID id;
    //you shoul fetch the configuration and validate every data that pass thorugh you but now we won't do that
    String configurationJSON;
    DeviceConfiguration configuration;
    //multi session not relevant yet
    //Map<String, WebSocketSession> webSocketSessionMap = new ConcurrentHashMap<>();
    WebSocketSession webSocketSession ;
    DevicePool devicePool;
    KafkaConsumer kafkaConsumer;
    KafkaProducer kafkaProducer;
     public Device(UUID id,  WebSocketSession webSocketSession, String configurationJSON,DevicePool devicePool, KafkaConsumer kafkaConsumer, KafkaProducer kafkaProducer)
    {

         this.id = id;
         this.devicePool = devicePool;
        this.webSocketSession = webSocketSession;
        this.configurationJSON = configurationJSON;
        this.kafkaConsumer =kafkaConsumer;
        this.kafkaProducer = kafkaProducer;



        Map<String,Boolean> topics = new HashMap<>();
        topics.put("device.capability."+this.id,false);
        //ai mai pute sa subscrii la state pentru a relua ultima stare inregistrata dar nu cred ca ete neapqart acum lasasm fara
        //topics.put("user.notification."+this.id,false);
         kafkaConsumer.addSubscriptionTopics(topics);
         sendOnline(true);
    }

    //not synchiornise because the parnt context is already syncronized and no other entoty should havre directmacces to my an my methods



    public void setWebSocketSession(WebSocketSession webSocketSession) {
        this.webSocketSession = webSocketSession;
    }
    public WebSocketSession replaceWebSocketSession(WebSocketSession webSocketSession) {

         WebSocketSession old = this.webSocketSession;
        this.webSocketSession = webSocketSession;
        if(!(old.getId().equals(webSocketSession.getId()))) {
            return old;
        }
        return null;
    }

    public void removeWebSocketSession(WebSocketSession webSocketSession)
    {
        if(this.webSocketSession.getId().equals(webSocketSession.getId()))
        {
            Set<String  > topics = new HashSet<>();
            topics.add("device.capability."+this.id);

            kafkaConsumer.removeSubscriptionTopics(topics);
            sendOnline(false);
            this.devicePool.removeDevice(this.id);

        }

    }


    @Override
    public String toString()
    {
        return "[Device] id: "+ this.id.toString()+" wsSessions:  "+ this.webSocketSession.toString();
    }


    public String prettyPrint()
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[Device] ");
        stringBuilder.append(this.id+"] ");
        stringBuilder.append("webSocket: ");
        stringBuilder.append(this.webSocketSession.getId());
        stringBuilder.append("configurationJSON: " + this.configurationJSON);
        return stringBuilder.toString();
    }
    public void sendCapability(String capabilityJSON) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        CapabilityMessageEntity capabilityMessageEntity = new CapabilityMessageEntity();

        ObjectNode deviceCapability  = objectMapper.createObjectNode();
        deviceCapability.put("id", this.id.toString());
        deviceCapability.set("capability",objectMapper.readValue(capabilityJSON, JsonNode.class));
        capabilityMessageEntity.setDeviceCapability(deviceCapability);
        capabilityMessageEntity.setType("deviceCapability");

        objectMapper.writerFor(CapabilityMessageEntity.class);
        synchronized (this.webSocketSession) {
            this.webSocketSession.sendMessage(new TextMessage(objectMapper.writerFor(CapabilityMessageEntity.class).writeValueAsString(capabilityMessageEntity)));
        }
    }

    public  void handleDeviceState(Map<String, String> attributeMap) {
         for (Map.Entry<String,String> attributeEntry : attributeMap.entrySet()) {
             String topicName = "device.state." + this.id.toString() +"."  + attributeEntry.getKey();
             System.out.println(attributeEntry.getValue());
             this.kafkaProducer.produceRecord(topicName, attributeEntry.getValue());
         }
    }


    public void sendOnline(boolean online)
    {
        String topicName = "device.online." + this.id.toString() ;
        String message = "{\"online\":"+online+"}";
        System.out.println("[Device "+this.id.toString()+"] produced "+message);
        this.kafkaProducer.produceRecord(topicName, message);
    }
}
