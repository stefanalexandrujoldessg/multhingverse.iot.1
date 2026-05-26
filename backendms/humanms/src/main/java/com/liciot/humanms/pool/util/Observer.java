package com.liciot.humanms.pool.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public abstract class Observer {
    protected Map<UUID, Observable> observables = new ConcurrentHashMap<>();
public abstract void update(ObservedStateEntity observedStateEntity) ;
public void addObservable(UUID uuid, Observable observable)
{this.observables.put(uuid,observable);}
    public void removeObservable(UUID uuid)
    {this.observables.remove(uuid);}
}
 
