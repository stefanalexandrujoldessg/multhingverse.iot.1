package com.liciot.humanms.pool.devicepool;

import com.liciot.humanms.apiprovider.ApiProvider;
import com.liciot.humanms.kafka.consumer.KafkaConsumer;
import com.liciot.humanms.pool.userpool.User;
import com.liciot.humanms.service.ApiConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component

public class DevicePool {
   @Autowired
   ApiConsumer apiConsumer;

   private Map<UUID, Device> deviceMap = new ConcurrentHashMap<UUID, Device>();

   public Device getDevice(UUID deviceId, KafkaConsumer kafkaConsumer, ApiProvider apiProvider)
   {
      synchronized (this) {

            if (this.deviceMap.containsKey(deviceId)) {


               return deviceMap.get(deviceId);
            } else {
               Device device = new Device(deviceId, apiConsumer, kafkaConsumer, this, apiProvider);
               this.deviceMap.put(deviceId, device);


               return device;
            }

      }
   }

   public synchronized void removeDevice(UUID uuid) {
      this.deviceMap.remove(uuid);
   }

   public String prettyPrint()
   {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("[DevicePool]: deviceMap = ");
      stringBuilder.append(this.deviceMap.keySet().toString());
      stringBuilder.append("\n");
      for(Map.Entry<UUID, Device> deviceEntry: this.deviceMap.entrySet())
      {
         stringBuilder.append(deviceEntry.getValue().prettyPrint());
      }
      return stringBuilder.toString();
   }

   public void handleStateUpdate(UUID deviceId, String attributeId, String value) throws Exception {
      this.deviceMap.get(deviceId).handleStateUpdate(deviceId,attributeId,value);
   }
   public void handleOnlineUpdate(UUID deviceId, Boolean online) throws Exception {
      this.deviceMap.get(deviceId).handleOnlineUpdate(deviceId,online);
   }
   public  void handleConfigurationUpdated(UUID deviceId , String configurationJSON)
   {
      this.deviceMap.get(deviceId).handleConfigurationUpdated(deviceId,configurationJSON);

   }
   public  synchronized  void handleDeviceDeleted(UUID deviceId )
   {
      this.deviceMap.get(deviceId).handleDeviceDeleted(deviceId);

   }

}
