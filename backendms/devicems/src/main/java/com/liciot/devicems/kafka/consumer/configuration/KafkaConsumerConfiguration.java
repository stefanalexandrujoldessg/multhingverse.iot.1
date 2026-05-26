package com.liciot.devicems.kafka.consumer.configuration;

import com.liciot.devicems.apiprovider.ApiProvider;
import com.liciot.devicems.kafka.consumer.KafkaConsumer;
import com.liciot.devicems.kafkahandlerchain.chain.KafkaHandlerChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutorService;

@Configuration
public class KafkaConsumerConfiguration {
@Autowired
    ExecutorService executorService;
@Autowired
KafkaHandlerChain kafkaChainHandler;
@Autowired
    ApiProvider apiProvider;
    @Bean
    public KafkaConsumer getKafkaConsumer()
    {

        Properties props = new Properties();
        props.setProperty("bootstrap.servers", apiProvider.getLiciotKafkaBootstrapServers());
        props.setProperty("group.id", UUID.randomUUID().toString());
        props.setProperty("enable.auto.commit", "false");
     //   props.setProperty("auto.commit.interval.ms", "1000");
        props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        org.apache.kafka.clients.consumer.KafkaConsumer<String, String> consumer = new org.apache.kafka.clients.consumer.KafkaConsumer<String, String>(props);
        //consumer.subscribe(Arrays.asList("foo", "bar"));
        KafkaConsumer kafkaConsumer = new KafkaConsumer(consumer, executorService, kafkaChainHandler);
        return kafkaConsumer;
    }
}
