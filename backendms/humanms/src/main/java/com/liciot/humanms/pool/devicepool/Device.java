package com.liciot.humanms.pool.devicepool;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.JsonObject;
import com.liciot.humanms.apiprovider.ApiProvider;
import com.liciot.humanms.dto.response.device.DeviceConfigurationDTO;
import com.liciot.humanms.dto.response.user.UserInitializationDTO;
import com.liciot.humanms.kafka.consumer.KafkaConsumer;
import com.liciot.humanms.pool.devicepool.entity.Attribute;
import com.liciot.humanms.pool.devicepool.entity.DeviceConfiguration;
import com.liciot.humanms.pool.util.Observable;
import com.liciot.humanms.pool.util.ObservedStateEntity;
import com.liciot.humanms.pool.util.ObservedStateType;
import com.liciot.humanms.pool.util.Observer;
import com.liciot.humanms.service.ApiConsumer;
import com.liciot.humanms.wshandlerchain.entity.wsmessage.send.devices.DevicesMessageEntity;
import com.liciot.humanms.wshandlerchain.entity.wsmessage.send.stateUpdate.AttributeEntity;
import com.liciot.humanms.wshandlerchain.entity.wsmessage.send.stateUpdate.StateEntity;
import com.liciot.humanms.wshandlerchain.entity.wsmessage.send.stateUpdate.StateMessageEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Device extends Observable {


    UUID id;
    String configurationJSON;
    ObjectNode configuration;
    DevicePool devicePool;
    KafkaConsumer kafkaConsumer;
     //Consfiguration mappedconfiguration;
    //CUm sa fa oare cu typeu asta de exeplu la tun atribut daca se schimab display value de catre user pot prin doua variante sa distribui asta;
    //pdata modificat in configuration in db pot sa dau notifiatre pe device.event updated conf i atuni co refetuciesc tot
    //sau pot ad dispribui aceasta informatioe pe device .attrn
    //sa nu uiti ca ai zos ca orice va avea un type pentru sc alare de viitor cel putin
    //si atunic poti folosi asta ca sa nu te chinui sa mapex in java
    //dar staii stop pe attr vor trebui sa ie doar vbslori atatr !!@!!!gata ca la primul subscibing eucitesfc ultimukl record sida ca la nu cotine valoar nu e om
    //deci ARAMANEM CU EVNET ASA E si firesc

    public Device(UUID id , ApiConsumer apiConsumer, KafkaConsumer kafkaConsumer, DevicePool devicePool, ApiProvider apiProvider) {
        try {
            this.id = id;
            this.devicePool = devicePool;
            this.kafkaConsumer = kafkaConsumer;
            //specialSystem.out.println("[Device] ");
            //if something goes wrong the apiCOnsumer wil thorw the exception an it wil react the main ws chain handler w have to handleit here because if it reaches there tno other dfoloing devices will be added fro  thi8s request
            DeviceConfigurationDTO deviceConfigurationDTO = apiConsumer.getForObject(apiProvider.getLiciotUserManagementMicroservice()+"/crud/device/getConfigurationById/" + this.id, DeviceConfigurationDTO.class, new HttpHeaders());
            //specialSystem.out.println("[Device]: " + deviceConfigurationDTO.getConfigurationJSON());
            //this.configuration = configuration;
            this.configurationJSON = deviceConfigurationDTO.getConfigurationJSON();
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                this.configuration = objectMapper.readValue(deviceConfigurationDTO.getConfigurationJSON(), ObjectNode.class);
               System.out.println("[Device]: configuration: " +objectMapper.writerFor(ObjectNode.class).writeValueAsString(this.configuration));

                Map<String, Boolean> topics = new HashMap<>();
                //de aici ar trebui folosit idul dinnconfiguratie dar ma rog inca nu lam pus in exemple si este nul deci voi folosi pe celalta orium odat adaugat ddeviceul cand face update configuration ar trebui verifivcat nu me treebuie nimic verificat da pu si simplu un sicng in conf ai acolo vad a cui e idul de acolo il au si a tunic e binee
                topics.put("device.event."+this.id, false);
                //topics.put("device.capability."+this.id, false);
                topics.put("device.online."+this.id, true);


                Iterator<String> attributesIterator = this.configuration.get("state").fieldNames();
                while(attributesIterator.hasNext())
                {
                    topics.put("device.state."+this.id+"."+attributesIterator.next(), true);

                }
                kafkaConsumer.addSubscriptionTopics(topics);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        catch (Exception exception)
        {
            exception.printStackTrace();

        }
    }


    @Override
    public synchronized void addObserver(UUID uuid, Observer observer)
    {
        //
        super.addObserver(uuid,observer);
        //trimit configuratia


    }
    @Override
    public synchronized void removeObserver(UUID uuid)
    {
        //
        super.removeObserver(uuid);
        if(!super.hasObservers())
        {

            Set<String> topics = new HashSet<>();
            //de aici ar trebui folosit idul dinnconfiguratie dar ma rog inca nu lam pus in exemple si este nul deci voi folosi pe celalta orium odat adaugat ddeviceul cand face update configuration ar trebui verifivcat nu me treebuie nimic verificat da pu si simplu un sicng in conf ai acolo vad a cui e idul de acolo il au si a tunic e binee
            topics.add("device.event."+this.id);
            //topics.add("device.capability."+this.id);
            topics.add("device.online."+this.id);

            Iterator<String> attributesIterator = this.configuration.get("state").fieldNames();
            while(attributesIterator.hasNext())
            {
                topics.add("device.state."+this.id+"."+attributesIterator.next());

                //topics.add("device.state."+this.id+"."+attribute.getKey());

            }
            kafkaConsumer.removeSubscriptionTopics(topics);
             this.devicePool.removeDevice(this.id);
        }
        //trimit configuratia


    }
    public String prettyPrint()
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[Device ");
        stringBuilder.append(this.id+"] ");
        stringBuilder.append("observers: "+ super.observers.keySet());
        return stringBuilder.toString();
    }

    public synchronized  void handleStateUpdate(UUID deviceId, String attributeId, String value) throws Exception {
        if(deviceId.equals(this.id))
        {
            ObjectMapper objectMapper = new ObjectMapper();
            //objectMapper.writerFor()
            StateMessageEntity stateMessageEntity = new StateMessageEntity();
            AttributeEntity attributeEntity = new AttributeEntity();
            StateEntity stateEntity = new StateEntity();

            stateMessageEntity.setType("devices");// respectam conventia

            attributeEntity.setValue(objectMapper.readValue(value, ObjectNode.class).get("value"));
            stateEntity.getState().put(attributeId,attributeEntity);


            stateMessageEntity.getDevices().put(deviceId.toString(), stateEntity);



            String message = objectMapper.writerFor(StateMessageEntity.class).writeValueAsString(stateMessageEntity);
            ObservedStateEntity<String> observedStateEntity = new ObservedStateEntity();
            observedStateEntity.setType(ObservedStateType.PASS_TO_CLIENT);
            observedStateEntity.setPayload(message);


            ((ObjectNode)((ObjectNode) this.configuration.get("state")).get(attributeId)).set("value", objectMapper.readValue(value, ObjectNode.class).get("value"));

            this.updateObservers(observedStateEntity);

        }
        else
        {

            throw new Exception("[Device] inconsistent id kernel panic");
        }
    }
    public synchronized void handleOnlineUpdate(UUID deviceId, Boolean online) throws Exception {
        if(deviceId.equals(this.id))
        {                ObjectMapper objectMapper = new ObjectMapper();

            if(this.configuration.has("online"))
            {
                if(!(objectMapper.treeToValue(this.configuration.get("online"), Boolean.class).equals(online)))
                {

                    ((ObjectNode) this.configuration).put("online", online.booleanValue());
                    //this.configuration.put("online",online.booleanValue());

                    ObjectNode payload = objectMapper.createObjectNode();

                    ObjectNode devices = objectMapper.createObjectNode();
                    ObjectNode device = objectMapper.createObjectNode();
                    device.put("online", online);
                    devices.set(this.id.toString(),device);
                    payload.put("type", "devices");

                    payload.set("devices", devices);

                    ObservedStateEntity<String> observedStateEntity = new ObservedStateEntity<>();

                    observedStateEntity.setPayload(objectMapper.writer().writeValueAsString(payload));
                    observedStateEntity.setType(ObservedStateType.PASS_TO_CLIENT);
                    this.updateObservers(observedStateEntity);
                }
            }
            else
            {
                ((ObjectNode) this.configuration).put("online", online.booleanValue());

                //this.configuration.put("online",online.booleanValue());

                ObjectNode payload = objectMapper.createObjectNode();

                ObjectNode devices = objectMapper.createObjectNode();
                ObjectNode device = objectMapper.createObjectNode();
                device.put("online", online);
                devices.set(this.id.toString(),device);
                payload.put("type", "devices");

                payload.set("devices", devices);

                ObservedStateEntity<String> observedStateEntity = new ObservedStateEntity<>();

                observedStateEntity.setPayload(objectMapper.writer().writeValueAsString(payload));
                observedStateEntity.setType(ObservedStateType.PASS_TO_CLIENT);
                this.updateObservers(observedStateEntity);
            }
        }

    }


        @Override
    public synchronized ObservedStateEntity<String> getObservableStateForClient()     {

        try {

            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode objectNode = objectMapper.createObjectNode();
            objectNode.set(this.id.toString(), this.configuration);
            DevicesMessageEntity devicesMessageEntity = new DevicesMessageEntity();
            devicesMessageEntity.setType("devices");
            devicesMessageEntity.setDevices(objectNode);

            ObjectWriter objectWriter = new ObjectMapper().writerFor(DevicesMessageEntity.class);
            ObservedStateEntity<String> response = new ObservedStateEntity<>();
            response.setPayload(objectWriter.writeValueAsString(devicesMessageEntity));
            response.setType(ObservedStateType.PASS_TO_CLIENT);
            return response;
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public synchronized ObservedStateEntity<String> getObservableNullStateForClient()     {

        try {

            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode objectNode = objectMapper.createObjectNode();
            objectNode.set(this.id.toString(), null);
            DevicesMessageEntity devicesMessageEntity = new DevicesMessageEntity();
            devicesMessageEntity.setType("devices");
            devicesMessageEntity.setDevices(objectNode);

            ObjectWriter objectWriter = new ObjectMapper().writerFor(DevicesMessageEntity.class);
            ObservedStateEntity<String> response = new ObservedStateEntity<>();
            response.setPayload(objectWriter.writeValueAsString(devicesMessageEntity));
            response.setType(ObservedStateType.PASS_TO_CLIENT);
            return response;
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
    public synchronized void handleConfigurationUpdated(UUID deviceId, String configurationJSONInput)
    {
        //specialSystem.out.println("Configuration Updated"+ configurationJSONInput);
        if( deviceId.equals(this.id) && (!this.configurationJSON.equals(configurationJSONInput)))
        {
            try{
                Set<String> topics = new HashSet<>();
                //de aici ar trebui folosit idul dinnconfiguratie dar ma rog inca nu lam pus in exemple si este nul deci voi folosi pe celalta orium odat adaugat ddeviceul cand face update configuration ar trebui verifivcat nu me treebuie nimic verificat da pu si simplu un sicng in conf ai acolo vad a cui e idul de acolo il au si a tunic e binee
                topics.add("device.event."+this.id);
                //topics.add("device.capability."+this.id);
                topics.add("device.online."+this.id);

                Iterator<String> attributesIterator = this.configuration.get("state").fieldNames();
                while(attributesIterator.hasNext())
                {
                    topics.add("device.state."+this.id+"."+attributesIterator.next());

                    //topics.add("device.state."+this.id+"."+attribute.getKey());

                }
                kafkaConsumer.removeSubscriptionTopics(topics);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                //specialSystem.out.println("[Propblem C U]");




            }

            ObjectMapper objectMapper = new ObjectMapper();
            try {
                ObjectNode c = objectMapper.readValue(configurationJSONInput,ObjectNode.class);
                this.configurationJSON = configurationJSONInput;
                this.configuration = c;//objectMapper.readValue(deviceConfigurationDTO.getConfigurationJSON(), ObjectNode.class);
                //specialSystem.out.println("[Device]: configurationIn: " +objectMapper.writerFor(ObjectNode.class).writeValueAsString(this.configuration));

                Map<String, Boolean> topics = new HashMap<>();
                //de aici ar trebui folosit idul dinnconfiguratie dar ma rog inca nu lam pus in exemple si este nul deci voi folosi pe celalta orium odat adaugat ddeviceul cand face update configuration ar trebui verifivcat nu me treebuie nimic verificat da pu si simplu un sicng in conf ai acolo vad a cui e idul de acolo il au si a tunic e binee
                topics.put("device.event."+this.id, false);
                //topics.put("device.capability."+this.id, false);
                topics.put("device.online."+this.id, true);


                Iterator<String> attributesIterator = this.configuration.get("state").fieldNames();
                while(attributesIterator.hasNext())
                {
                    topics.put("device.state."+this.id+"."+attributesIterator.next(), true);

                }
                kafkaConsumer.addSubscriptionTopics(topics);
                this.updateObservers(this.getObservableNullStateForClient());
                ObservedStateEntity<String> o = this.getObservableStateForClient();
                o.setObservableId(this.id.toString());
                o.setType(ObservedStateType.PASS_TO_CLIENT_USER_UPDATE);
                this.updateObservers(o);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }
    public  synchronized  void handleDeviceDeleted(UUID deviceId )
    {
        if(deviceId.equals(this.id))
        {
            Set<String> topics = new HashSet<>();
            //de aici ar trebui folosit idul dinnconfiguratie dar ma rog inca nu lam pus in exemple si este nul deci voi folosi pe celalta orium odat adaugat ddeviceul cand face update configuration ar trebui verifivcat nu me treebuie nimic verificat da pu si simplu un sicng in conf ai acolo vad a cui e idul de acolo il au si a tunic e binee
            topics.add("device.event."+this.id);
            //topics.add("device.capability."+this.id);
            topics.add("device.online."+this.id);

            Iterator<String> attributesIterator = this.configuration.get("state").fieldNames();
            while(attributesIterator.hasNext())
            {
                topics.add("device.state."+this.id+"."+attributesIterator.next());

                //topics.add("device.state."+this.id+"."+attribute.getKey());

            }
            kafkaConsumer.removeSubscriptionTopics(topics);
            this.devicePool.removeDevice(this.id);


            try {
                super.releaseObservers(this.id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
