package com.liciot.humanms.pool.userpool;

import com.fasterxml.jackson.databind.JsonNode;
import com.liciot.humanms.apiprovider.ApiProvider;
import com.liciot.humanms.kafka.consumer.KafkaConsumer;
import com.liciot.humanms.kafka.producer.KafkaProducer;
import com.liciot.humanms.pool.devicepool.DevicePool;
import com.liciot.humanms.pool.devicepool.DevicePoolInjector;
import com.liciot.humanms.service.ApiConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class UserPool {

    //Atentiao device offline is not supported in case of service failure you need to meke a schedule action here to veryfy the last timdstampt for online message
    @Autowired
    DevicePool devicePool;
    @Autowired
    ApiConsumer apiConsumer;

    private Map<UUID, User> userMap = new ConcurrentHashMap<UUID, User>();
    private Map<String, Map.Entry<WebSocketSession, LocalDateTime>> webSocketSessionMap = new HashMap<String, Map.Entry<WebSocketSession, LocalDateTime>>();


    public   void addUser(UUID userId, List<UUID> adminDevicesIds, List<UUID> accessDevicesIds, WebSocketSession webSocketSession, KafkaConsumer kafkaConsumer, KafkaProducer kafkaProducer, DevicePoolInjector devicePoolInjector, Long kafkaConsumeTimeOffset, ApiProvider apiProvider)
    {
        boolean createNewUser = false;
        synchronized (this)
        {
            for(Map.Entry<UUID,User> userEntry: this.userMap.entrySet())
            {
                if(!userEntry.getKey().equals(userId)) {
                    userEntry.getValue().removeWebSocketSession(webSocketSession);
                }
            }
            this.webSocketSessionMap.put(webSocketSession.getId(),new  AbstractMap.SimpleEntry<WebSocketSession, LocalDateTime>(webSocketSession, LocalDateTime.now().plusSeconds(20)));

            if(this.userMap.containsKey(userId))
            {

                User user = this.userMap.get(userId);
                user.addWebSocketSession(webSocketSession);
                user.refreshDeviceList(adminDevicesIds,accessDevicesIds,devicePoolInjector);

            }
            else
            {
                //createNewUser  = true;
                User user = new User(userId, adminDevicesIds, accessDevicesIds, webSocketSession,devicePool,this, kafkaConsumer, kafkaProducer, devicePoolInjector, apiConsumer, kafkaConsumeTimeOffset, apiProvider);
                this.userMap.put(userId,user);

            }
        }
        // now it is user problem tosyncronize
        //iti is okm becouse if we reach here it means that we are not crewting the same user from different threads becauset he part before is synchronised
        //th problem eint synch will appear aain from here in devoce pool and maby in device \//but stop if i create new USer here and then add it to the map it sis not ok because nomeone can reacth toif and the creat ene same user
        //User user = new User(userId, adminDevicesIds, accessDevicesIds, webSocketSession);


    }
    public synchronized boolean isAuthorized(WebSocketSession webSocketSession)
    {
        if(this.webSocketSessionMap.containsKey(webSocketSession.getId()))
        {
            this.webSocketSessionMap.get(webSocketSession.getId()).setValue(LocalDateTime.now().plusSeconds(20));//new  AbstractMap.SimpleEntry<WebSocketSession, LocalDateTime>(webSocketSession, LocalDateTime.now().plusSeconds(20)));

            return true;
        }
        else
        {
            return false;
        }
    }

    @Scheduled(fixedDelay = 2000)
            public synchronized  void closeDeadWebSocketSessions()
    {
        System.out.println("Shc");
        Iterator<Map.Entry<String, Map.Entry<WebSocketSession, LocalDateTime>>> iterator= this.webSocketSessionMap.entrySet().iterator();
        while (iterator.hasNext())//Map.Entry<String, Map.Entry<WebSocketSession, LocalDateTime>>webSocketEntry: this.webSocketSessionMap.entrySet())
        {
            Map.Entry<String, Map.Entry<WebSocketSession, LocalDateTime>> webSocketEntry = iterator.next();
            if(webSocketEntry.getValue().getValue().isBefore(LocalDateTime.now()))
            {
                try {
                    System.out.println("Close]]");

                    for(Map.Entry<UUID, User> userEntry : this.userMap.entrySet())
                    {
                        try {
                            userEntry.getValue().removeWebSocketSession(webSocketEntry.getValue().getKey());
                        }catch (Exception e){e.printStackTrace();}
                    }
                    webSocketEntry.getValue().getKey().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                iterator.remove();
                this.prettyPrint();
                //we base thet the closing opperation will trigger the ws hain and the web sdocket wil be removed or we shoudent rely on thet
               // this.removeWebSocketSession(webSocketEntry.getKey().get);
                //for the moment we can rely on that because it remains in the map
                //and at the next shecdulign it is going to be closed again if t=not for the first time
            }
        }
    }
    public synchronized void removeWebSocketSession(WebSocketSession webSocketSession)
    {
        this.webSocketSessionMap.remove(webSocketSession.getId());
        for(Map.Entry<UUID, User> userEntry : this.userMap.entrySet())
        {
            userEntry.getValue().removeWebSocketSession(webSocketSession);
        }
    }
    public synchronized void removeUser(UUID userId)
    {
        this.userMap.remove(userId);
    }
    public String prettyPrint()
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[UserPool]: userMap: ");
        stringBuilder.append(this.userMap.keySet().toString());
        stringBuilder.append("\n[UserPool]: webSocketMap: ");
        stringBuilder.append(this.webSocketSessionMap.keySet().toString());
        stringBuilder.append("\n");
        for(Map.Entry<UUID, User> userEntry: this.userMap.entrySet())
        {
            stringBuilder.append(userEntry.getValue().prettyPrint());
        }
        return stringBuilder.toString();
    }

    public synchronized  void handleDeviceCapability(UUID deviceId , String capabilityJSON, WebSocketSession webSocketSession) {
        for(Map.Entry<UUID, User> userEntry: this.userMap.entrySet())
        {
            userEntry.getValue().handleDeviceCapability(  deviceId ,   capabilityJSON,webSocketSession);
        }
    }

    public synchronized void handleDeviceAdded(UUID userId, UUID deviceId){
        if(this.userMap.containsKey(userId)) {
            this.userMap.get(userId).handleDeviceAdded(userId, deviceId);
        }
    }
    public synchronized void handleDeviceRemoved(UUID userId, UUID deviceId){
        if(this.userMap.containsKey(userId)) {
            this.userMap.get(userId).handleDeviceRemoved(userId, deviceId);
        }
    }
    public synchronized void handleUserDeviceConfigurationUpdated(UUID userId, JsonNode userDeviceConfiguration){
        if(this.userMap.containsKey(userId)) {
            this.userMap.get(userId).handleUserDeviceConfigurationUpdated(userId, userDeviceConfiguration);
        }
    }

}
