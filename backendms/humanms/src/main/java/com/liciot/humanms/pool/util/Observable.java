package com.liciot.humanms.pool.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public abstract  class Observable {
    protected   Map<UUID, Observer> observers = new ConcurrentHashMap<>();

    public Map<UUID, Observer> getObservers() {
        return observers;
    }

    public void setObservers(Map<UUID, Observer> observers) {
        this.observers = observers;
    }

    public Observable() {
    }

    public Observable(Map<UUID, Observer> observers) {
        this.observers = observers;
    }
    public void addObserver(UUID uuid, Observer observer)
    {
        this.observers.put(uuid,observer);

    }
    public void removeObserver(UUID uuid )
    {
        this.observers.remove(uuid);

    }
    public void updateObservers(ObservedStateEntity observedStateEntity) throws IOException {
        for (Map.Entry<UUID, Observer> observerEntry: this.observers.entrySet())
        {
            observerEntry.getValue().update(observedStateEntity);
        }
    }
    public void releaseObservers(UUID id)   {
        for (Map.Entry<UUID, Observer> observerEntry: this.observers.entrySet())
        {
            observerEntry.getValue().removeObservable(id);
        }
    }
    public abstract ObservedStateEntity<String> getObservableStateForClient()    ;
     public boolean hasObservers()
    {
        if(this.observers.size()==0)
        {
            return false;
        }
        else {
            return true;
        }
    }

}
