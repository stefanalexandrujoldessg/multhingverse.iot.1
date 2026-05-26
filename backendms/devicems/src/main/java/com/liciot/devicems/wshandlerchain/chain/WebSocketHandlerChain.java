package com.liciot.devicems.wshandlerchain.chain;

import com.liciot.devicems.wshandlerchain.chain.handler.WebSocketChainHandler;
import com.liciot.devicems.wshandlerchain.entity.ToWSHandlerChainEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

//A stateless handler

public class WebSocketHandlerChain {
    class HandlerEntity{
        WebSocketChainHandler webSocketChainHandler;
        ReentrantLock lock;
    }
    private List<HandlerEntity> handlerEntityList= new ArrayList<>();
    public void doHandle(ToWSHandlerChainEntity entity, int currentIndex) {
        int nextIndex = currentIndex+1;
        //specialSystem.out.println("[WebSocketHandlerChain] before if : "+ String.valueOf(this.webSocketChainHandlerList.size()) + " "+String.valueOf(nextIndex));
        System.out.println(currentIndex);

        if (this.handlerEntityList.size()  > nextIndex) {
            //System.out.println("[WebSocketHandlerChain]: will call hjale on handler");
            this.handlerEntityList.get(nextIndex).lock.lock();
            System.out.println(currentIndex);
            System.out.println("Is pass");

            if(currentIndex>=0) {

                if (this.handlerEntityList.get(currentIndex).lock.isHeldByCurrentThread()) {
                    System.out.println("Is mine");

                    this.handlerEntityList.get(currentIndex).lock.unlock();
                } else {
                    System.out.println("[PANIC]");
                    System.exit(100);
                }
            }

            System.out.println(currentIndex);

            try {
                this.handlerEntityList.get(nextIndex).webSocketChainHandler.handle(entity, this, nextIndex);
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
    public void doHandle(ToWSHandlerChainEntity entity) {
        //System.out.println("[WebSocketHandlerChain] nr. of handlers: "+ this.webSocketChainHandlerList.size());
        this.doHandle(entity, -1);
    }
    //here is no concurrent access
    public void addWebSocketChainHandler(WebSocketChainHandler webSocketChainHandler)
    {
        HandlerEntity handlerEntity = new HandlerEntity();
        handlerEntity.webSocketChainHandler = webSocketChainHandler;
        handlerEntity.lock = new ReentrantLock(true);
        this.handlerEntityList.add(handlerEntity);
      //  handlerEntity.lock.unlock();


       // this.webSocketChainHandlerList.add(webSocketChainHandler);
    }
}
