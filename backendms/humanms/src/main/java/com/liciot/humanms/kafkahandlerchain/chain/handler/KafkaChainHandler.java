package com.liciot.humanms.kafkahandlerchain.chain.handler;

import com.liciot.humanms.kafkahandlerchain.chain.KafkaHandlerChain;
import com.liciot.humanms.kafkahandlerchain.entity.ToKafkaHandlerChainEntity;

public interface KafkaChainHandler {
    public  void handle(ToKafkaHandlerChainEntity entity, KafkaHandlerChain webSocketHandlerChain, int currentIndex);
}
