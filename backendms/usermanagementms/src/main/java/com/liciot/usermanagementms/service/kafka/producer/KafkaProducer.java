package com.liciot.usermanagementms.service.kafka.producer;

import com.liciot.usermanagementms.apiprovider.ApiProvider;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class KafkaProducer {



   //org.apache.kafka.clients.producer.KafkaProducer<String,String> producer;
    Properties properties;
    org.apache.kafka.clients.producer.KafkaProducer<String,String> producer;
    @Autowired
    public KafkaProducer( ApiProvider apiProvider)
    {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, apiProvider.getLiciotKafkaBootstrapServers());
        //props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.RETRIES_CONFIG, 0);
       // props.put("linger.ms", 1);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
       // props.put("max.retry.count",0);
        props.put(ProducerConfig.MAX_BLOCK_MS_CONFIG,10000);//cam mic timpul acesta petru vcomunicar in internet 10s e ok
        this.properties = props;
         this.producer = new org.apache.kafka.clients.producer.KafkaProducer<String, String>(props);
    }
    public void produceRecord(String topic, String recordValue)
    {
       // org.apache.kafka.clients.producer.KafkaProducer<String,String> producer = new org.apache.kafka.clients.producer.KafkaProducer<String, String>(this.properties);
       //List<PartitionInfo> par = producer.partitionsFor(topic);
       System.out.println("Partitions ready");
        producer.send(new ProducerRecord<String, String>(topic, "default", recordValue),new Callback(){

            @Override
        public void onCompletion(RecordMetadata metadata, Exception exception) {

                //vai atentie functioneaza aia cu mad block ms
                //dar daca ca prostu arunic exepctii aiaic va fi probleme ca nu termina functia asat si cconsumerul continua la infinit fara sa se opreasca
                try {
                    System.out.println("[KafkaProducer.produceRecord] Callback: metadata: " + ((metadata!=null)?metadata.topic():null) + " exception: " +( (exception!=null)?exception.getMessage():null));
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                finally {
                    return;
                }

        }
    });
        //much more efficient like this
        //maybe check if the topic exists
//but it has aqueue so if iti waiting for the current message to m be ss4nd the othetr messages will wait sao it is badif the topic does not exists
      //  producer.close();//if the producer is per clas we can not close it here
        // if i don't close it will result in a memory leak but if i only have one producer instane per multple threads itwill be more efficient they saiy adn it isn natural to be like that
        //so i havbe to close the producer when i do not dneed it or ...the ideea is not to create multiple KafkaProducers and not dcall close() fter i do not need them anymore .

    }
}
