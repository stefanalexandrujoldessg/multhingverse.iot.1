package com.liciot.humanms.util.asyncexecutor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

@Configuration
public class AsyncExecutorConfiguration {

    @Bean
     public ExecutorService getExecutorService()
    {
        return new ThreadPoolExecutor(2,10,2L, TimeUnit.SECONDS,new LinkedBlockingQueue<Runnable>());
    }

}
