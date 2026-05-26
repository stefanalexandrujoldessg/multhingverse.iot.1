package com.liciot.usermanagementms.entity;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "liciot_factorydevice")

public class FactoryDevice {
    @Id

    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Type(type= "uuid-binary")


            UUID deviceId;
    //String configurationJSON;


    public FactoryDevice(UUID deviceId) {
        this.deviceId = deviceId;
    }


    public UUID getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(UUID deviceId) {
        this.deviceId = deviceId;
    }


}