package com.liciot.devicems.kafkahandlerchain.chain.handler;

import com.liciot.devicems.kafkahandlerchain.chain.KafkaHandlerChain;
import com.liciot.devicems.kafkahandlerchain.entity.ToKafkaHandlerChainEntity;

public interface KafkaChainHandler {
    public  void handle(ToKafkaHandlerChainEntity entity, KafkaHandlerChain webSocketHandlerChain, int currentIndex);
}
