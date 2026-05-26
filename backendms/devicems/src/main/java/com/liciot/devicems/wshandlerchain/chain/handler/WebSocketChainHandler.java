package com.liciot.devicems.wshandlerchain.chain.handler;

import com.liciot.devicems.wshandlerchain.entity.ToWSHandlerChainEntity;
import com.liciot.devicems.wshandlerchain.chain.WebSocketHandlerChain;

public interface WebSocketChainHandler {
    public  void handle(ToWSHandlerChainEntity entity, WebSocketHandlerChain webSocketHandlerChain, int currentIndex);
}
