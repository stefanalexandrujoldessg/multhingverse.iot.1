package com.liciot.devicems.kafka.consumer;

import com.liciot.devicems.kafkahandlerchain.chain.KafkaHandlerChain;
 import com.liciot.devicems.kafkahandlerchain.entity.HandlerChainEntityType;
import com.liciot.devicems.kafkahandlerchain.entity.ToKafkaHandlerChainEntity;

import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndTimestamp;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;


import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;


public class KafkaConsumer implements Runnable{
    public static    enum SubscriptionTopicState {
        JOINED,
        JOINED_FOR_TIME,
        INITIALIZED
    }
    private final AtomicBoolean pool = new AtomicBoolean(true);

    private final AtomicBoolean closed = new AtomicBoolean(false);
    private Map<String, Integer> subscriptionTopics = new ConcurrentHashMap<String, Integer>();
    private Map<String, SubscriptionTopicState> subscriptionTopicsState = new ConcurrentHashMap<String, SubscriptionTopicState>();
    private Map<String, Long> subscriptionTopicsTime = new ConcurrentHashMap<String, Long>();

    private org.apache.kafka.clients.consumer.KafkaConsumer<String,String> consumer;
    private ExecutorService executorService;
    private KafkaHandlerChain kafkaHandlerChain;
    private Object lock = new Object();
    public KafkaConsumer(org.apache.kafka.clients.consumer.KafkaConsumer consumer, ExecutorService executorService, KafkaHandlerChain kafkaChainHandler)
    {
        this.consumer = consumer;
        this.executorService = executorService;
        this.kafkaHandlerChain = kafkaChainHandler;
        Thread t = new Thread(this);

        t.start();
    }


    @Override
    public void run() {


        while (!closed.get()) {
            try {
                synchronized (this.lock){
                    if (this.pool.get() && this.consumer.subscription().size() > 0) {
                        ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100000));
                        for (ConsumerRecord<String, String> record : records) {
                            System.out.println("[Consumed new message]: from topic" + record.topic() + " : " + record.value());
                            if(!this.subscriptionTopicsState.get(record.topic()).equals(SubscriptionTopicState.INITIALIZED)) {
                                this.subscriptionTopicsState.put(record.topic(), SubscriptionTopicState.INITIALIZED);
                            }
                        }
                        if(!records.isEmpty()) {
                            ToKafkaHandlerChainEntity entity = new ToKafkaHandlerChainEntity();
                            entity.setEntityType(HandlerChainEntityType.TOPIC_NEW_RECORD);
                            entity.setRecords(records);
                            executorService.execute(() -> {
                                this.kafkaHandlerChain.doHandle(entity);
                            });
                        }

                    }
                    else{
                        this.lock.wait();
                    }
                    //for (TopicPartition topicPartition : consumer.assignment())
                    ///{
                    // if(this.subscriptionTopicsState.containsKey(topicPartition.topic()) && this.subscriptionTopicsState.get(topicPartition.topic()).equals(SubscriptionTopicState.JOINED))
                    //{
                    //consumer.seek(topicPartition, );
                    // consumer.
                    // }
                    // }
                }
                    /*
                        // Handle new records
                        for (ConsumerRecord<String, String> record : records) {
                            System.out.println("[Consumed new message]: from topic" + record.topic() + " : " + record.value());
                        }
                        this.synchronizeSubscriptions();
                    } else {
                        Thread.sleep(1000);
                        this.synchronizeSubscriptions();

                    }

                     */


            } catch(WakeupException e){
                // Ignore exception if closing
                if (!closed.get()) e.printStackTrace();
            } catch(Exception e){
                e.printStackTrace();
            } finally{
                //consumer.close();
            }
        }
        consumer.close();
    }

    // Shutdown hook which can be called from a separate thread
    public void shutdown() {
        closed.set(true);
        consumer.wakeup();
    }
    private void synchronizeSubscriptions()
    {
        List<String> topics = List.copyOf(this.subscriptionTopicsState.keySet());
        //for (Map.Entry<String , SubscriptionTopicState> topic: this.subscriptionTopicsState.entrySet())
        {
            //if(topic.getValue().equals(SubscriptionTopicState))

        }
        this.pool.set(false);

        this.consumer.wakeup();// aici e smeheria
        //setea pool false ma asigur ca de acu  daac mai vrea sa intre nu intra in if
        //apoi ii dau wake
        //si apoi intru aici in sycronised
        //daca pun wake in syncrinised practic ii da wake numai and termina ea poolu ee ce eu nu vrewau
        //e super ce ai facut
        synchronized (this.lock) {
            //this.pool.set(false);
            //aici pracrtic nimic doar astepti poti totusi sa lasi o sintructiune sa te asiguti
            // this.consumer.wakeup();


            this.consumer.subscribe(topics, new MyConsumerRebalanaceListener(this));
            // asta s-ar putea sa dureze si ar trbeui faucta numai acnd ai noi topicuri de ti pcreated
            this.pool.set(true);
            this.lock.notifyAll();
        }
    }

    @Deprecated
    public  synchronized void addSubscriptionTopics (Set<String> subscriptionTopics)
    {
        boolean shouldSynchronize =false;
        for (String topic : subscriptionTopics)
        {
            if(!this.subscriptionTopics.containsKey(topic)) {
                shouldSynchronize = true;
                this.subscriptionTopics.put(topic, 1);
                this.subscriptionTopicsState.put(topic, SubscriptionTopicState.JOINED);

            }
            else
            {
                int number = this.subscriptionTopics.get(topic);
                number = number+1;
                this.subscriptionTopics.put(topic, number);
            }
        }
        if(shouldSynchronize)
        {
            this.synchronizeSubscriptions();
        }
    }


    public  synchronized void addSubscriptionTopics (Map<String,Boolean> subscriptionTopics)
    {
        boolean shouldSynchronize =false;
        for (Map.Entry<String,Boolean> topicEntry : subscriptionTopics.entrySet())
        {
            if(!this.subscriptionTopics.containsKey(topicEntry.getKey())) {
                shouldSynchronize = true;
                this.subscriptionTopics.put(topicEntry.getKey(), 1);
                this.subscriptionTopicsState.put(topicEntry.getKey(), (topicEntry.getValue())?SubscriptionTopicState.JOINED:SubscriptionTopicState.INITIALIZED);

            }
            else
            {
                int number = this.subscriptionTopics.get(topicEntry.getKey());
                number = number+1;
                this.subscriptionTopics.put(topicEntry.getKey(), number);
            }
        }
        if(shouldSynchronize)
        {
            this.synchronizeSubscriptions();
        }

    }

    public  synchronized void addSubscriptionTopicsWithTime (Map<String,Long> subscriptionTopics)
    {
        boolean shouldSynchronize =false;
        for (Map.Entry<String,Long> topicEntry : subscriptionTopics.entrySet())
        {
            if(!this.subscriptionTopics.containsKey(topicEntry.getKey())) {
                shouldSynchronize = true;
                this.subscriptionTopics.put(topicEntry.getKey(), 1);
                this.subscriptionTopicsState.put(topicEntry.getKey(), SubscriptionTopicState.JOINED_FOR_TIME);
                this.subscriptionTopicsTime.put(topicEntry.getKey(),topicEntry.getValue());

            }
            else
            {
                int number = this.subscriptionTopics.get(topicEntry.getKey());
                number = number+1;
                this.subscriptionTopics.put(topicEntry.getKey(), number);
            }
        }
        if(shouldSynchronize)
        {
            this.synchronizeSubscriptions();
        }

    }
    public synchronized  void removeSubscriptionTopics (Set<String> subscriptionTopics)
    {
        boolean shouldSynchronize =false;
        for (String topic : subscriptionTopics)
        {
            if(!this.subscriptionTopics.containsKey(topic)) {
                //shouldSynchronize = true;
                //this.subscriptionTopics.put(topic, 1);
                //this.subscriptionTopicsState.put(topic, SubscriptionTopicState.JOINED);

            }
            else {
                int number = this.subscriptionTopics.get(topic);
                number = number - 1;
                if (number <= 0) {
                    this.subscriptionTopics.remove(topic);
                    this.subscriptionTopicsState.remove(topic);
                    this.subscriptionTopicsTime.remove(topic);
                    shouldSynchronize = true;
                } else {


                    this.subscriptionTopics.put(topic, number);
                }
            }
        }
        if(shouldSynchronize)
        {
            this.synchronizeSubscriptions();
        }
    }

    private static List<String> formatPartitions(Collection<TopicPartition> partitions) {
        return partitions.stream().map(topicPartition ->
                String.format("topic: %s, partition: %s\n", topicPartition.topic(), topicPartition.partition()))
                .collect(Collectors.toList());
    }

    public AtomicBoolean getPool() {
        return pool;
    }

    public AtomicBoolean getClosed() {
        return closed;
    }

    public Map<String, Integer> getSubscriptionTopics() {
        return subscriptionTopics;
    }

    public void setSubscriptionTopics(Map<String, Integer> subscriptionTopics) {
        this.subscriptionTopics = subscriptionTopics;
    }

    public Map<String, SubscriptionTopicState> getSubscriptionTopicsState() {
        return subscriptionTopicsState;
    }

    public void setSubscriptionTopicsState(Map<String, SubscriptionTopicState> subscriptionTopicsState) {
        this.subscriptionTopicsState = subscriptionTopicsState;
    }

    public org.apache.kafka.clients.consumer.KafkaConsumer<String, String> getConsumer() {
        return consumer;
    }

    public void setConsumer(org.apache.kafka.clients.consumer.KafkaConsumer<String, String> consumer) {
        this.consumer = consumer;
    }




    private class MyConsumerRebalanaceListener implements ConsumerRebalanceListener
    {
        KafkaConsumer kafkaConsumer;
        public MyConsumerRebalanaceListener(KafkaConsumer kafkaConsumer) {
            this.kafkaConsumer = kafkaConsumer;
        }
        @Override
        public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
            System.out.println("onPartitionsRevoked - consumerName: , partitions:"+formatPartitions(partitions));
        }

        @Override
        public synchronized void onPartitionsAssigned(Collection<TopicPartition> partitions) {
            System.out.println("onPartitionsAssigned - consumerName:, partitions: "+formatPartitions(partitions));
            List<TopicPartition> newTopicPartitions = new ArrayList<TopicPartition>();
            Map<TopicPartition,Long> newTopicPartitionsTime = new HashMap<>();
            for(TopicPartition topicPartition: partitions)
            {
                if(kafkaConsumer.getSubscriptionTopicsState().containsKey(topicPartition.topic()))
                {
                    if(kafkaConsumer.getSubscriptionTopicsState().get(topicPartition.topic()).equals(SubscriptionTopicState.JOINED))
                    {
                        newTopicPartitions.add(topicPartition);
                        //kafkaConsumer.getConsumer().seek(topicPartition,Long.max(kafkaConsumer.getConsumer().endOffsets(Arrays.asList(topicPartition)).get(topicPartition)-1,0L));
                        //kafkaConsumer.getSubscriptionTopicsState().put(topicPartition.topic(), SubscriptionTopicState.INITIALIZED);
                    }
                    if(kafkaConsumer.getSubscriptionTopicsState().get(topicPartition.topic()).equals(SubscriptionTopicState.JOINED_FOR_TIME))
                    {
                        newTopicPartitionsTime.put(topicPartition,kafkaConsumer.subscriptionTopicsTime.get(topicPartition));
                        //kafkaConsumer.getConsumer().seek(topicPartition,Long.max(kafkaConsumer.getConsumer().endOffsets(Arrays.asList(topicPartition)).get(topicPartition)-1,0L));
                        //kafkaConsumer.getSubscriptionTopicsState().put(topicPartition.topic(), SubscriptionTopicState.INITIALIZED);
                    }

                }
            }

            Map<TopicPartition, Long> topicPartitionsEndOffsets = this.kafkaConsumer.consumer.endOffsets(newTopicPartitions);
            if(topicPartitionsEndOffsets.size()!=newTopicPartitions.size())
            {
                System.out.println("[.onPartitionsAssigned] different sizes");

            }

            for(Map.Entry<TopicPartition,Long> topicPartitionEntry:topicPartitionsEndOffsets.entrySet())
            {
                System.out.println("[Topic partition seek] "+ topicPartitionEntry.getKey().topic()+" offset: "+topicPartitionEntry.getValue());
                kafkaConsumer.getConsumer().seek(topicPartitionEntry.getKey(),Long.max( topicPartitionEntry.getValue()-1,0L));
                //poate doar daac ma ingr3eruoe aii altfel ar trebui sa raman untd e trebuie offsetul pentru a este INITIALIZED
                //  kafkaConsumer.getSubscriptionTopicsState().put(topicPartitionEntry.getKey().topic(), SubscriptionTopicState.INITIALIZED);
                //problema nu este la kafka lcient conumer este latine in felul urmator:
                //tu vi aii si fai la primul partitionsAssigned seek unde trebuie si setzi INITILIZED ii dar daa ininte sA SE ONSUME ULTIMUL MESAJ SE fae din nou wakeup() repede se ajunge din nou aici si se va muta ba nu stai
                //da ma aici e greseala
                //tu vi il pui la consum pe ultimul
                //dar intre timp se mai da o dat apartitions reasigned si atentie asta inseamna a sunt alte entitiati de partitie diferite si tu aiic lea ai deja in map ac fiind INITIALIZATE si prati nu le mai muta offseturile la noile partiii si uite sa nu mai onsumi nimic hehen
                ///problema nu este ka kafka
                //est implemementatn si trata foarte bine wakeup in poll
            }






            Map<TopicPartition, OffsetAndTimestamp> topicPartitionsEndOffsetsTime = this.kafkaConsumer.consumer.offsetsForTimes(newTopicPartitionsTime);
            if(topicPartitionsEndOffsetsTime.size()!=newTopicPartitionsTime.size())
            {
                System.out.println("[.onPartitionsAssigned] different sizes");

            }

            for(Map.Entry<TopicPartition,OffsetAndTimestamp> topicPartitionEntry:topicPartitionsEndOffsetsTime.entrySet())
            {
                System.out.println("[Topic partition time] "+topicPartitionEntry.getKey().topic()+" "+topicPartitionEntry.getValue().offset()+" "+topicPartitionEntry.getValue().timestamp());
               // System.out.println("[Topic partition time] "+ topicPartitionEntry.getKey().topic()+" offset: "+topicPartitionEntry.getValue());
                //kafkaConsumer.getConsumer().seek(topicPartitionEntry.getKey(),Long.max( topicPartitionEntry.getValue()-1,0L));
                //poate doar daac ma ingr3eruoe aii altfel ar trebui sa raman untd e trebuie offsetul pentru a este INITIALIZED
                //  kafkaConsumer.getSubscriptionTopicsState().put(topicPartitionEntry.getKey().topic(), SubscriptionTopicState.INITIALIZED);
                //problema nu este la kafka lcient conumer este latine in felul urmator:
                //tu vi aii si fai la primul partitionsAssigned seek unde trebuie si setzi INITILIZED ii dar daa ininte sA SE ONSUME ULTIMUL MESAJ SE fae din nou wakeup() repede se ajunge din nou aici si se va muta ba nu stai
                //da ma aici e greseala
                //tu vi il pui la consum pe ultimul
                //dar intre timp se mai da o dat apartitions reasigned si atentie asta inseamna a sunt alte entitiati de partitie diferite si tu aiic lea ai deja in map ac fiind INITIALIZATE si prati nu le mai muta offseturile la noile partiii si uite sa nu mai onsumi nimic hehen
                ///problema nu este ka kafka
                //est implemementatn si trata foarte bine wakeup in poll
            }
        }

    }

}
