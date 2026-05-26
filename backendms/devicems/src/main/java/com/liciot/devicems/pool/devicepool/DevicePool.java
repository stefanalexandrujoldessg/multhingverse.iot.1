package com.liciot.devicems.pool.devicepool;

import com.fasterxml.jackson.databind.JsonNode;
import com.liciot.devicems.kafka.consumer.KafkaConsumer;
import com.liciot.devicems.kafka.producer.KafkaProducer;
import com.liciot.devicems.pool.devicepool.entity.WebSocketData.WebSocketData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
/// we need to have support for multi evice for websocket session thath means hub support
/// we need to have support for multi evice for websocket session thath means hub support /// we need to have support for multi evice for websocket session thath means hub support /// we need to have support for multi evice for websocket session thath means hub support
/// we need to have support for multi evice for websocket session thath means hub support
/// we need to have support for multi evice for websocket session thath means hub support
/// we need to have support for multi evice for websocket session thath means hub support /// we need to have support for multi evice for websocket session thath means hub support /// we need to have support for multi evice for websocket session thath means hub support




@Component
public class DevicePool {

    private Map<UUID, Device> deviceMap = new ConcurrentHashMap<UUID, Device>();
    private Map<String, WebSocketData> webSocketSessionMap = new HashMap<String,WebSocketData>();


    public   void addDevice( UUID deviceId,  String configurationJSON, WebSocketSession webSocketSession, KafkaConsumer kafkaConsumer, KafkaProducer kafkaProducer)
    {
        boolean createNewDevice = false;
        synchronized (this)
        {
            for(Map.Entry<UUID,Device> deviceEntry: this.deviceMap.entrySet())
            {
                if(!deviceEntry.getKey().equals(deviceId)) {
                    deviceEntry.getValue().removeWebSocketSession(webSocketSession);
                }
            }

            if(this.deviceMap.containsKey(deviceId))
            {
                Device device = this.deviceMap.get(deviceId);
                WebSocketSession old = device.replaceWebSocketSession(webSocketSession);
                if(old!=null) {
                    try{
                        old.close();
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    this.webSocketSessionMap.remove(old.getId());
                }

                WebSocketData webSocketData = new WebSocketData(device,webSocketSession, LocalDateTime.now().plusSeconds(20), LocalDateTime.now());
                this.webSocketSessionMap.put(webSocketSession.getId(), webSocketData);

            }
            else
            {
                //createNewUser  = true;
                Device device = new Device(deviceId,webSocketSession,configurationJSON,this,kafkaConsumer,kafkaProducer);
                this.deviceMap.put(deviceId,device);

                WebSocketData webSocketData = new WebSocketData(device,webSocketSession, LocalDateTime.now().plusSeconds(20), LocalDateTime.now());
                this.webSocketSessionMap.put(webSocketSession.getId(), webSocketData);

            }

        }
        // now it is user problem tosyncronize
        //iti is okm becouse if we reach here it means that we are not crewting the same user from different threads becauset he part before is synchronised
        //th problem eint synch will appear aain from here in devoce pool and maby in device \//but stop if i create new USer here and then add it to the map it sis not ok because nomeone can reacth toif and the creat ene same user
        //User user = new User(userId, adminDevicesIds, accessDevicesIds, webSocketSession);


    }
    public synchronized boolean isAuthorized(WebSocketSession webSocketSession) throws IOException {
        if(this.webSocketSessionMap.containsKey(webSocketSession.getId()))
        {
            this.webSocketSessionMap.get(webSocketSession.getId()).setExpire(LocalDateTime.now().plusSeconds(20));//new  AbstractMap.SimpleEntry<WebSocketSession, LocalDateTime>(webSocketSession, LocalDateTime.now().plusSeconds(20)));

            return true;
        }
        else
        {
            return false;
        }
    }
@Scheduled(fixedDelay = 60000)
public synchronized  void sendOnline() {
    for (Map.Entry<String, WebSocketData> webSocketDataEntry : this.webSocketSessionMap.entrySet())
    {
     WebSocketData webSocketData = webSocketDataEntry.getValue();
        if(this.deviceMap.containsKey(webSocketData.getDevice().id))
        {
            this.deviceMap.get(webSocketData.getDevice().id).sendOnline(true);
        }
        else {
            System.out.println("[Kernel Panic] device is not present in device map but it is in webSocketData");
            try {
                webSocketData.getWebSocketSession().close();
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }

    }
}
    @Scheduled(fixedDelay = 2000)
    public synchronized  void closeDeadWebSocketSessions()
    {
        System.out.println("Shc");
        Iterator<Map.Entry<String, WebSocketData>> iterator= this.webSocketSessionMap.entrySet().iterator();

        while (iterator.hasNext())
        {
            Map.Entry<String, WebSocketData>webSocketEntry = iterator.next();
            if(webSocketEntry.getValue().getExpire().isBefore(LocalDateTime.now()))
            {
                try {
                    System.out.println("Close]]");


                    for(Map.Entry<UUID, Device> deviceEntry: this.deviceMap.entrySet())
                    {
                        try {
                                deviceEntry.getValue().removeWebSocketSession(webSocketEntry.getValue().getWebSocketSession());
                            }catch (Exception e){e.printStackTrace();}
                    }



                    webSocketEntry.getValue().getWebSocketSession().close();
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
        for(Map.Entry<UUID, Device> deviceEntry: this.deviceMap.entrySet())
        {
            deviceEntry.getValue().removeWebSocketSession(webSocketSession);
        }
    }
    public  synchronized void removeDevice(UUID deviceId)
    {
        this.deviceMap.remove(deviceId);
    }
    public String prettyPrint()
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[Device Pool]: deviceMap: ");
        stringBuilder.append(this.deviceMap.keySet().toString());
        stringBuilder.append("\n[Device Pool]: webSocketMap: ");
        stringBuilder.append(this.webSocketSessionMap.keySet().toString());
        stringBuilder.append("\n");
        for(Map.Entry<UUID, Device> userEntry: this.deviceMap.entrySet())
        {
            stringBuilder.append(userEntry.getValue().prettyPrint());
        }
        return stringBuilder.toString();
    }

    public void sendCapability(UUID deviceId, String configurationJSON) throws IOException {
        this.deviceMap.get(deviceId).sendCapability(configurationJSON);
    }
    public synchronized void handleDeviceState(UUID deviceId, Map<String, String> attributeMap)
    {
          this.deviceMap.get(deviceId).handleDeviceState(attributeMap);

    }




}
