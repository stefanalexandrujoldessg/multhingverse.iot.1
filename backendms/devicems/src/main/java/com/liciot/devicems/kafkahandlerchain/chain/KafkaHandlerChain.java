package com.liciot.devicems.kafkahandlerchain.chain;

import com.liciot.devicems.kafkahandlerchain.chain.handler.KafkaChainHandler;
import com.liciot.devicems.kafkahandlerchain.entity.ToKafkaHandlerChainEntity;

import java.util.ArrayList;
import java.util.List;

//A stateless handler

public class KafkaHandlerChain {
    private List<KafkaChainHandler> kafkaChainHandlerList= new ArrayList<>();
    public void doHandle(ToKafkaHandlerChainEntity entity, int currentIndex) {
        int nextIndex = currentIndex+1;
        System.out.println("[KafkaHandlerChain] before if : "+ String.valueOf(this.kafkaChainHandlerList.size()) + " "+String.valueOf(nextIndex));

        if (this.kafkaChainHandlerList.size()  > nextIndex) {
            //System.out.println("[KafkaHandlerChain]: will call hjale on handler");
            this.kafkaChainHandlerList.get(nextIndex).handle(entity, this, nextIndex);
        }
    }
    public void doHandle(ToKafkaHandlerChainEntity entity) {
        //System.out.println("[KafkaHandlerChain] nr. of handlers: "+ this.kafkaChainHandlerList.size());
        this.doHandle(entity, -1);
    }
    //here is no concurrent access
    public void addKafkaChainHandler(KafkaChainHandler kafkaChainHandler)
    {
        this.kafkaChainHandlerList.add(kafkaChainHandler);
    }
}
