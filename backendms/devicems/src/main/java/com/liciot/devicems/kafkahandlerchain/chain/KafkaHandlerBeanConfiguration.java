package com.liciot.devicems.kafkahandlerchain.chain;

import com.liciot.devicems.kafkahandlerchain.chain.handler.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaHandlerBeanConfiguration {

@Autowired
KafkaNewTopicRecordPrinterHandler kafkaNewTopicRecordPrinterHandler;

@Autowired
KafkaNewCapabilityTopicRecordHandler kafkaNewCapabilityTopicRecordHandler;
    @Bean
    public KafkaHandlerChain getKafkaHandlerChain()
    {
        KafkaHandlerChain kafkaHandlerChain= new KafkaHandlerChain();
        kafkaHandlerChain.addKafkaChainHandler(this.kafkaNewTopicRecordPrinterHandler);
        kafkaHandlerChain.addKafkaChainHandler(this.kafkaNewCapabilityTopicRecordHandler);

        return  kafkaHandlerChain;

    }
}
