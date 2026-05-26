package com.liciot.humanms.kafkahandlerchain.chain;

import com.liciot.humanms.kafkahandlerchain.chain.handler.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaHandlerBeanConfiguration {

@Autowired
KafkaNewTopicRecordPrinterHandler kafkaNewTopicRecordPrinterHandler;
@Autowired
KafkaNewStateTopicRecordHandler kafkaNewStateTopicRecordHandler;
    @Autowired
    KafkaNewOnlineTopicRecordHandler kafkaNewOnlineTopicRecordHandler;
    @Autowired
    KafkaNewUserEventTopicRecordHandler kafkaNewUserEventTopicRecordHandler;
    @Autowired
    KafkaNewDeviceEventTopicRecordHandler kafkaNewDeviceEventTopicRecordHandler;
    @Bean
    public KafkaHandlerChain getKafkaHandlerChain()
    {
        KafkaHandlerChain kafkaHandlerChain= new KafkaHandlerChain();
        kafkaHandlerChain.addKafkaChainHandler(this.kafkaNewTopicRecordPrinterHandler);
        kafkaHandlerChain.addKafkaChainHandler(this.kafkaNewStateTopicRecordHandler);

        kafkaHandlerChain.addKafkaChainHandler(this.kafkaNewOnlineTopicRecordHandler);
        kafkaHandlerChain.addKafkaChainHandler(this.kafkaNewUserEventTopicRecordHandler);
        kafkaHandlerChain.addKafkaChainHandler(this.kafkaNewDeviceEventTopicRecordHandler);

        return  kafkaHandlerChain;

    }
}
