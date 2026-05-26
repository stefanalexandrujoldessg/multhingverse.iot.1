package com.liciot.humanms.kafkahandlerchain.chain;

import com.liciot.humanms.kafkahandlerchain.chain.handler.KafkaChainHandler;
import com.liciot.humanms.kafkahandlerchain.entity.ToKafkaHandlerChainEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

//A stateless handler

public class KafkaHandlerChain {
    class HandlerEntity{
        KafkaChainHandler kafkaChainHandler;
        ReentrantLock lock;
    }
    private List<HandlerEntity> handlerEntityList= new ArrayList<>();
    public void doHandle(ToKafkaHandlerChainEntity entity, int currentIndex) {
        int nextIndex = currentIndex+1;
        //special//specialSystem.out.println("[KafkaHandlerChain] before if : "+ String.valueOf(this.webSocketChainHandlerList.size()) + " "+String.valueOf(nextIndex));
        //specialSystem.out.println(currentIndex);

        if (this.handlerEntityList.size()  > nextIndex) {
            ////specialSystem.out.println("[KafkaHandlerChain]: will call hjale on handler");
            this.handlerEntityList.get(nextIndex).lock.lock();
            //specialSystem.out.println(currentIndex);
            //specialSystem.out.println("Is pass");

            if(currentIndex>=0) {

                if (this.handlerEntityList.get(currentIndex).lock.isHeldByCurrentThread()) {
                    //specialSystem.out.println("Is mine");

                    this.handlerEntityList.get(currentIndex).lock.unlock();
                } else {
                    //specialSystem.out.println("[PANIC]");
                    System.exit(100);
                }
            }

            //specialSystem.out.println(currentIndex);

            try {
                this.handlerEntityList.get(nextIndex).kafkaChainHandler.handle(entity, this, nextIndex);
            }catch(Exception e)
            {
                e.printStackTrace();
            }finally {
                if(currentIndex>=0 && this.handlerEntityList.get(nextIndex).lock.isHeldByCurrentThread()) {

                    this.handlerEntityList.get(nextIndex).lock.unlock();
                }
            }
        }
    }
    public void doHandle(ToKafkaHandlerChainEntity entity) {
        ////specialSystem.out.println("[KafkaHandlerChain] nr. of handlers: "+ this.webSocketChainHandlerList.size());
        this.doHandle(entity, -1);
    }
    //here is no concurrent access
    public void addKafkaChainHandler(KafkaChainHandler kafkaChainHandler)
    {
        HandlerEntity handlerEntity = new HandlerEntity();
        handlerEntity.kafkaChainHandler = kafkaChainHandler;
        handlerEntity.lock = new ReentrantLock(true);
        this.handlerEntityList.add(handlerEntity);
        //  handlerEntity.lock.unlock();


        // this.webSocketChainHandlerList.add(webSocketChainHandler);
    }
}
