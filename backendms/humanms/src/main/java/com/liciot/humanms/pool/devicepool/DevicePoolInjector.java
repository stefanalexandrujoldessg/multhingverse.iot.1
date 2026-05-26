package com.liciot.humanms.pool.devicepool;

import com.liciot.humanms.apiprovider.ApiProvider;
import com.liciot.humanms.kafka.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DevicePoolInjector {
    @Autowired
    KafkaConsumer kafkaConsumer;
    @Autowired
    DevicePool devicePool;
    @Autowired
    ApiProvider apiProvider;
    public Device getDevice(UUID deviceId){
        return this.devicePool.getDevice(deviceId,kafkaConsumer, apiProvider);
    }
}
