package com.liciot.humanms.pool.userpool;

import com.liciot.humanms.apiprovider.ApiProvider;
import com.liciot.humanms.kafka.consumer.KafkaConsumer;
import com.liciot.humanms.kafka.producer.KafkaProducer;
import com.liciot.humanms.pool.devicepool.DevicePoolInjector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;
import java.util.UUID;

@Component
public class UserPoolInjector {

    @Autowired
    UserPool userPool;
    @Autowired
    KafkaProducer kafkaProducer;
    @Autowired
    KafkaConsumer kafkaConsumer;
    @Autowired
    DevicePoolInjector devicePoolInjector;
    @Autowired
    ApiProvider apiProvider;
    public   void addUser(UUID userId, List<UUID> adminDevicesIds, List<UUID> accessDevicesIds, WebSocketSession webSocketSession, Long kafkaConsumeTimeOffset)
    {
        this.userPool.addUser(userId,adminDevicesIds,accessDevicesIds,webSocketSession,kafkaConsumer,kafkaProducer, devicePoolInjector, kafkaConsumeTimeOffset, apiProvider);
    }
}
