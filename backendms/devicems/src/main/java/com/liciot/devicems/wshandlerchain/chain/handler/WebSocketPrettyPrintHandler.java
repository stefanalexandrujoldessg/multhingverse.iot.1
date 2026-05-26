package com.liciot.devicems.wshandlerchain.chain.handler;

import com.liciot.devicems.pool.devicepool.DevicePool;
//import com.liciot.devicems.pool.userpool.UserPool;
import com.liciot.devicems.wshandlerchain.chain.WebSocketHandlerChain;
import com.liciot.devicems.wshandlerchain.entity.ToWSHandlerChainEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WebSocketPrettyPrintHandler implements WebSocketChainHandler{
 @Autowired
 DevicePool devicePool;


    @Override
    public void handle(ToWSHandlerChainEntity entity, WebSocketHandlerChain webSocketHandlerChain, int currentIndex) {



        try {
              // System.out.println(this.userPool.prettyPrint());
               System.out.println( this.devicePool.prettyPrint());
        }catch (Exception e)
        {

            System.out.println("[WebSocketPrettyPrintHandler] exception parsing the message");
        }
        finally {

                webSocketHandlerChain.doHandle(entity, currentIndex);

        }

    }
}
