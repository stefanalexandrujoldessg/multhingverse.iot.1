package com.liciot.humanms.wshandlerchain.chain.handler;

import com.liciot.humanms.pool.devicepool.DevicePool;
import com.liciot.humanms.pool.userpool.UserPool;
import com.liciot.humanms.wshandlerchain.chain.WebSocketHandlerChain;
import com.liciot.humanms.wshandlerchain.entity.HandlerChainEntityType;
import com.liciot.humanms.wshandlerchain.entity.ToWSHandlerChainEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WebSocketPrettyPrintHandler implements WebSocketChainHandler{
 @Autowired
    DevicePool devicePool;
 @Autowired
    UserPool userPool;

    @Override
    public void handle(ToWSHandlerChainEntity entity, WebSocketHandlerChain webSocketHandlerChain, int currentIndex) {



        try {
               System.out.println(this.userPool.prettyPrint());
               System.out.println( this.devicePool.prettyPrint());
        }catch (Exception e)
        {

            //specialSystem.out.println("[WebSocketPrettyPrintHandler] exception parsing the message");
        }
        finally {

                webSocketHandlerChain.doHandle(entity, currentIndex);

        }

    }
}
