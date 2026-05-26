package com.liciot.devicems.pool.devicepool;

import com.liciot.devicems.kafka.consumer.KafkaConsumer;
import com.liciot.devicems.kafka.producer.KafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.UUID;
@Component
public class DevicePoolInjector {
    @Autowired
    KafkaConsumer kafkaConsumer;
    @Autowired
    KafkaProducer kafkaProducer;
    @Autowired
    DevicePool devicePool;
    public   void addDevice(UUID deviceId, String configurationJSON, WebSocketSession webSocketSession)
    {
            this.devicePool.addDevice(deviceId, configurationJSON,webSocketSession,kafkaConsumer,kafkaProducer);
    }

}
