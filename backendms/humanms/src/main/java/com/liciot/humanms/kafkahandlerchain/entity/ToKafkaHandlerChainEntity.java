package com.liciot.humanms.kafkahandlerchain.entity;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.internals.Topic;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

public class ToKafkaHandlerChainEntity {
    HandlerChainEntityType entityType;
    Topic topic;
    ConsumerRecords<String, String> records;

    public ToKafkaHandlerChainEntity(HandlerChainEntityType entityType, Topic topic, ConsumerRecords<String, String> record) {
        this.entityType = entityType;
        this.topic = topic;
        this.records = record;
    }

    public ToKafkaHandlerChainEntity() {
    }

    public HandlerChainEntityType getEntityType() {
        return entityType;
    }
    public void setEntityType(HandlerChainEntityType entityType) {
        this.entityType = entityType;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public ConsumerRecords<String, String> getRecords() {
        return records;
    }

    public void setRecords(ConsumerRecords<String, String> record) {
        this.records = record;
    }
}