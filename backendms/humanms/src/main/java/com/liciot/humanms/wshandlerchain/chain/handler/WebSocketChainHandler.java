package com.liciot.humanms.wshandlerchain.chain.handler;

import com.liciot.humanms.wshandlerchain.entity.ToWSHandlerChainEntity;
import com.liciot.humanms.wshandlerchain.chain.WebSocketHandlerChain;

public interface WebSocketChainHandler {
    public  void handle(ToWSHandlerChainEntity entity, WebSocketHandlerChain webSocketHandlerChain, int currentIndex);
}
