package com.liciot.humanms.pool.util;

import java.util.UUID;

public class ObservedStateEntity<T> {
        ObservedStateType type;
        T payload;
        String observableId;

    public ObservedStateEntity(ObservedStateType type, T payload, String observableId) {
        this.type = type;
        this.payload = payload;
        this.observableId = observableId;
    }

    public ObservedStateEntity(ObservedStateType type, T payload     ) {
        this.type = type;
        this.payload = payload;

    }




    public ObservedStateEntity() {
    }

    public ObservedStateType getType() {
        return type;
    }

    public void setType(ObservedStateType type) {
        this.type = type;
    }

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }

    public String getObservableId() {
        return observableId;
    }

    public void setObservableId(String observableId) {
        this.observableId = observableId;
    }
}
