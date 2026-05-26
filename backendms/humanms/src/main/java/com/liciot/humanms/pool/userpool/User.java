package com.liciot.humanms.pool.userpool;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.JsonObject;
import com.liciot.humanms.apiprovider.ApiProvider;
import com.liciot.humanms.dto.response.device.DeviceConfigurationDTO;
import com.liciot.humanms.kafka.consumer.KafkaConsumer;
import com.liciot.humanms.kafka.producer.KafkaProducer;
import com.liciot.humanms.pool.devicepool.Device;
import com.liciot.humanms.pool.devicepool.DevicePool;
import com.liciot.humanms.pool.devicepool.DevicePoolInjector;
import com.liciot.humanms.pool.util.Observable;
import com.liciot.humanms.pool.util.ObservedStateEntity;
import com.liciot.humanms.pool.util.ObservedStateType;
import com.liciot.humanms.pool.util.Observer;
import com.liciot.humanms.service.ApiConsumer;
import com.liciot.humanms.wshandlerchain.entity.wsmessage.send.devices.DevicesMessageEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class User extends Observer {
    UUID id;
    Map<String, WebSocketSession> webSocketSessionMap = new ConcurrentHashMap<>();
    Map<UUID, String > devicesConfigurationsJSON = new ConcurrentHashMap<>();
    Map<UUID, JsonNode > devicesConfigurations= new ConcurrentHashMap<>();

    UserPool userPool;
    KafkaConsumer kafkaConsumer;
    KafkaProducer kafkaProducer;
    DevicePoolInjector devicePoolInjector;
    ApiConsumer apiConsumer;
    ApiProvider apiProvider;
     public User(UUID id, List<UUID> adminDevicesIds, List<UUID> accessDeviceIds, WebSocketSession webSocketSession, DevicePool devicePool, UserPool userPool, KafkaConsumer kafkaConsumer, KafkaProducer kafkaProducer, DevicePoolInjector devicePoolInjector, ApiConsumer apiConsumer, Long kafkaConsumeTimeOffset, ApiProvider apiProvider)
    {

        //trebuie si aici subscibe ;la topicurile userului si unsibscribe la distrugerea acestei entitati
        this.id = id;
        this.userPool = userPool;
        this.addWebSocketSession(webSocketSession);
        this.kafkaConsumer =kafkaConsumer;
        this.kafkaProducer = kafkaProducer;
        this.devicePoolInjector = devicePoolInjector;
        this.apiConsumer = apiConsumer;
        this.apiProvider = apiProvider;
        //device pool interaction
        for(UUID deviceId:adminDevicesIds) {
            Device device  = devicePoolInjector.getDevice(deviceId);
            device.addObserver(this.id, this);
            super.addObservable(deviceId, device);

        }
        for(UUID deviceId:accessDeviceIds) {
            Device device  = devicePoolInjector.getDevice(deviceId);
            device.addObserver(this.id, this);
            super.addObservable(deviceId, device);        }

        Map<String,Boolean> topics = new HashMap<>();
       // topics.put("user.event."+this.id,false);
        topics.put("user.notification."+this.id,false);

         kafkaConsumer.addSubscriptionTopics(topics);
        Map<String,Long> topicsTime = new HashMap<>();

        // un bug foarrte puternic acici din gereseala -60 secunde ei ei vai vai
         topicsTime.put("user.event."+this.id,kafkaConsumeTimeOffset);

        kafkaConsumer.addSubscriptionTopicsWithTime(topicsTime);
//it is shure that kafak will not send messeges to the client because nobody except the inside of this constructor can call methods of this oject utili it is a stable fully constructer object
        //so i can leg the topic subscription before this for

         for (Map.Entry<UUID,Observable> observableEntry: super.observables.entrySet())
         {
             try {
                 this.update(observableEntry.getValue().getObservableStateForClient());

             }catch (Exception e)
             {
                 e.printStackTrace();
             }

         }
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode jsonObject = apiConsumer.getForObject(apiProvider.getLiciotUserManagementMicroservice()+"/crud/user/getDevicesConfigurations/" + this.id, ObjectNode.class, new HttpHeaders());
            System.out.println("[USer dvc]: " + objectMapper.writer().writeValueAsString(jsonObject));
            JsonNode devicesConfigurations = jsonObject.get("devicesConfigurations");
            if(devicesConfigurations!=null)
            {
                Iterator<String> devicesIds = devicesConfigurations.fieldNames();
                while(devicesIds.hasNext())
                {
                    String deviceId = devicesIds.next();
                    JsonNode configurationObject = devicesConfigurations.get(deviceId);
                    if(configurationObject!=null)
                    {
                        JsonNodeType jsonNodeType = configurationObject.getNodeType();
                        if(jsonNodeType.equals(JsonNodeType.STRING))
                        {
                            try {
                                JsonNode configuration = objectMapper.readValue(configurationObject.textValue(), JsonNode.class);
                                UUID devId = UUID.fromString(deviceId);
                                if (super.observables.containsKey(devId)) {
                                    this.devicesConfigurationsJSON.put(UUID.fromString(deviceId), configurationObject.textValue());
                                    this.devicesConfigurations.put(UUID.fromString(deviceId), configuration);
                                    System.out.println("[Map]: " + this.devicesConfigurationsJSON);
                                    System.out.println("[Map]: " + this.devicesConfigurations);
                                    try {
                                        ObservedStateEntity<String> observedStateEntitty = new ObservedStateEntity<String>();
                                        observedStateEntitty.setType(ObservedStateType.PASS_TO_CLIENT);
                                        observedStateEntitty.setPayload(this.createDevicesMessage(UUID.fromString(deviceId), configuration, objectMapper));
                                        this.update(observedStateEntitty);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }
                            }catch(Exception e)
                                {
                                    e.printStackTrace();
                                }


                        }
                    }

                }

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
    public String createDevicesMessage(UUID deviceId, JsonNode configuration, ObjectMapper objectMapper) throws JsonProcessingException {

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.set(deviceId.toString(), configuration);
        DevicesMessageEntity devicesMessageEntity = new DevicesMessageEntity();
        devicesMessageEntity.setType("devices");
        devicesMessageEntity.setDevices(objectNode);

        ObjectWriter objectWriter = new ObjectMapper().writerFor(DevicesMessageEntity.class);
        return objectWriter.writeValueAsString(devicesMessageEntity);
    }

    public synchronized void handleDeviceAdded(UUID userId, UUID deviceId)
    {
        if(!super.observables.containsKey(deviceId)) {
            //specialSystem.out.println("[User - deviceAdded]");
            Device device = devicePoolInjector.getDevice(deviceId);
            device.addObserver(this.id, this);
            super.addObservable(deviceId, device);
            this.update(device.getObservableStateForClient());

        }
    }
    public synchronized void handleDeviceRemoved(UUID userId, UUID deviceId)
    {
        if(super.observables.containsKey(deviceId)) {
            //specialSystem.out.println("[User - deviceRemoved ]");

            super.observables.get(deviceId).removeObserver(this.id);
            super.observables.remove(deviceId);
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode payload = objectMapper.createObjectNode();
             ObjectNode device = objectMapper.createObjectNode();

            String n = null;
            device.put(deviceId.toString(),n);
            payload.put("type","devices");
             payload.set("devices",device);
            String message = null;
            try {
                message = objectMapper.writer().writeValueAsString(payload);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            //specialSystem.out.println("[DeviceRemoved] "+message );
            ObservedStateEntity<String> observedStateEntity = new ObservedStateEntity<>();
            observedStateEntity.setType(ObservedStateType.PASS_TO_CLIENT);
            observedStateEntity.setPayload(message);
            this.update(observedStateEntity);

        }
    }
    public void refreshDeviceList(List<UUID> adminDevicesIds, List<UUID> accessDevicesIds,DevicePoolInjector devicePoolInjector)
    {
        for(UUID deviceId:adminDevicesIds) {
            if(!super.observables.containsKey(deviceId))
            {
            Device device  = devicePoolInjector.getDevice(deviceId);
            device.addObserver(this.id, this);
            super.addObservable(deviceId, device);}

        }
        for(UUID deviceId:accessDevicesIds) {
            if (!super.observables.containsKey(deviceId)) {
                Device device = devicePoolInjector.getDevice(deviceId);
                device.addObserver(this.id, this);
                super.addObservable(deviceId, device);
            }
        }
    }
    public void addWebSocketSession(WebSocketSession webSocketSession)
    {
        this.webSocketSessionMap.put(webSocketSession.getId(), webSocketSession);

        for (Map.Entry<UUID,Observable> observableEntry: super.observables.entrySet())
        {
            try {
               // this.update(observableEntry.getValue().getObservableStateForClient());
                ObservedStateEntity<String> observedStateEntity = observableEntry.getValue().getObservableStateForClient();
               // observedStateEntity.setObservableId(observableEntry.getKey().toString());
              //  observedStateEntity.setType(ObservedStateType.PASS_TO_CLIENT_USER_UPDATE);
                if(observedStateEntity!=null)
                {

                    //not sure
                    try{
                    synchronized (webSocketSession) {
                        webSocketSession.sendMessage(new TextMessage(observedStateEntity.getPayload()));
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                }
            }catch (Exception e)
            {
                e.printStackTrace();
            }

        }
        ObjectMapper objectMapper = new ObjectMapper();
        for(Map.Entry<UUID, JsonNode> configurationEntry: this.devicesConfigurations.entrySet() )
        {
            try {
                synchronized (webSocketSession) {
                    webSocketSession.sendMessage(new TextMessage(this.createDevicesMessage(configurationEntry.getKey(), configurationEntry.getValue(), objectMapper)));
                }
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    //not synchiornise because the parnt context is already syncronized and no other entoty should havre directmacces to my an my methods
    public void removeWebSocketSession(WebSocketSession webSocketSession)
    {
        this.webSocketSessionMap.remove(webSocketSession.getId());
        if(this.webSocketSessionMap.size() == 0)
        {
            //solve the problem with the device via removeObserver
            for(Map.Entry<UUID,Observable> observableEntry: super.observables.entrySet())
            {
                observableEntry.getValue().removeObserver(this.id);
            }


            Set<String> topics = new HashSet<String>();
            topics.add("user.event."+this.id);
            topics.add("user.notification."+this.id);
            kafkaConsumer.removeSubscriptionTopics(topics);
            this.userPool.removeUser(this.id);

        }

    }
    @Override
    public   void update(ObservedStateEntity observedStateEntity) {
        synchronized (this) {
            //specialSystem.out.println("Trimitere user1 " + observedStateEntity.getPayload().toString());

            if (observedStateEntity != null) {


                if (observedStateEntity.getType().equals(ObservedStateType.PASS_TO_CLIENT)) {
                    //specialSystem.out.println("Trimitere user2 " + observedStateEntity.getPayload().toString());

                    for (Map.Entry<String, WebSocketSession> webSocketSessionEntry : this.webSocketSessionMap.entrySet()) {

                        try {
                            //not sure
                            synchronized ( webSocketSessionEntry.getValue()) {
                                webSocketSessionEntry.getValue().sendMessage(new TextMessage(observedStateEntity.getPayload().toString()));
                            }
                            //specialSystem.out.println("Trimitere user " + observedStateEntity.getPayload().toString());

                        } catch (IOException e) {
                            //specialSystem.out.println("Eroare trimitere user");
                            e.printStackTrace();
                        }
                    }
                }
                if(observedStateEntity.getType().equals(ObservedStateType.PASS_TO_CLIENT_USER_UPDATE))
                {


                    for (Map.Entry<String, WebSocketSession> webSocketSessionEntry : this.webSocketSessionMap.entrySet()) {

                        try {
                            //not sure
                            synchronized ( webSocketSessionEntry.getValue()) {
                                webSocketSessionEntry.getValue().sendMessage(new TextMessage(observedStateEntity.getPayload().toString()));
                            }
                            //specialSystem.out.println("Trimitere user " + observedStateEntity.getPayload().toString());

                        } catch (IOException e) {
                            //specialSystem.out.println("Eroare trimitere user");
                            e.printStackTrace();
                        }
                    }
                    ObjectMapper objectMapper = new ObjectMapper();
                    for (Map.Entry<String, WebSocketSession> webSocketSessionEntry : this.webSocketSessionMap.entrySet()) {

                        try {

                                if(observedStateEntity.getObservableId()!=null) {
                                    UUID deviceId = UUID.fromString(observedStateEntity.getObservableId());
                                    if (this.devicesConfigurations.containsKey( deviceId) ){
                                        String message = this.createDevicesMessage(deviceId, this.devicesConfigurations.get(deviceId), objectMapper);


                                        synchronized ( webSocketSessionEntry.getValue()) {
                                            webSocketSessionEntry.getValue().sendMessage(new TextMessage(message));
                                        }
                                    }
                                }
                            //specialSystem.out.println("Trimitere user " + observedStateEntity.getPayload().toString());

                        } catch (Exception ex) {
                            //specialSystem.out.println("Eroare trimitere user");
                            ex.printStackTrace();
                        }
                    }

                }


            }

        }
    }

    @Override
    public String toString()
    {
        return "[User] id: "+ this.id.toString()+" wsSessions: "+ this.webSocketSessionMap.toString();
    }


    public String prettyPrint()
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[User ");
        stringBuilder.append(this.id+"] ");
        stringBuilder.append("webSockets: ");
        stringBuilder.append(this.webSocketSessionMap.keySet()) ;
        stringBuilder.append("observables: "+ super.observables.keySet());

        stringBuilder.append("userDevConfJSON: "+ this.devicesConfigurationsJSON);
        stringBuilder.append("userDevConf: "+ this.devicesConfigurations);

        return stringBuilder.toString();
    }

    public void handleDeviceCapability(UUID deviceId, String capabilityJSON, WebSocketSession webSocketSession)
    {
        if(this.webSocketSessionMap.containsKey(webSocketSession.getId()))
        {
            if(super.observables.containsKey(deviceId))
            {
                this.kafkaProducer.produceRecord("device.capability."+deviceId.toString(), capabilityJSON);
            }
        }
    }
    public synchronized void handleUserDeviceConfigurationUpdated(UUID userId, JsonNode devicesConfigurations) {
        {
            if (devicesConfigurations != null) {
                ObjectMapper objectMapper = new ObjectMapper();

                Iterator<String> devicesIds = devicesConfigurations.fieldNames();
                while (devicesIds.hasNext()) {
                    String deviceId = devicesIds.next();
                    JsonNode configurationObject = devicesConfigurations.get(deviceId);
                    if (configurationObject != null) {
                        JsonNodeType jsonNodeType = configurationObject.getNodeType();
                        if (jsonNodeType.equals(JsonNodeType.STRING)) {
                            try {
                                JsonNode configuration = objectMapper.readValue(configurationObject.textValue(), JsonNode.class);
                                this.devicesConfigurationsJSON.put(UUID.fromString(deviceId), configurationObject.textValue());
                                this.devicesConfigurations.put(UUID.fromString(deviceId), configuration);
                                System.out.println("[Map]: " + this.devicesConfigurationsJSON);
                                System.out.println("[Map]: " + this.devicesConfigurations);
                                try {
                                    ObservedStateEntity<String> observedStateEntitty = new ObservedStateEntity<String>();
                                    observedStateEntitty.setType(ObservedStateType.PASS_TO_CLIENT);
                                    observedStateEntitty.setPayload(this.createDevicesMessage(UUID.fromString(deviceId), configuration, objectMapper));
                                    this.update(observedStateEntitty);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }

                }

            }
        }
    }
    @Override
    public void removeObservable(UUID id)
    {
        super.removeObservable(id);
        this.devicesConfigurations.remove(id);
        this.devicesConfigurationsJSON.remove(id);
        JsonNode a = null;
        try{
            ObservedStateEntity<String> observedStateEntity = new ObservedStateEntity<>();
            observedStateEntity.setType(ObservedStateType.PASS_TO_CLIENT);
            observedStateEntity.setPayload(   this.createDevicesMessage(id,a, new ObjectMapper()));
            this.update(observedStateEntity);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
