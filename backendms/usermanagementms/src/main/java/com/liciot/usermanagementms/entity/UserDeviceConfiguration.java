package com.liciot.usermanagementms.entity;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "liciot_user_device_configuration", uniqueConstraints=
@UniqueConstraint(columnNames = {"user_id", "device_id"}))

public class UserDeviceConfiguration {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Type(type= "uuid-binary")
    UUID id;
    @JoinColumn(name = "user_id")
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)


    User user;
    @JoinColumn(name = "device_id")

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)

    Device device;

    String configurationJSON;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public String getConfigurationJSON() {
        return configurationJSON;
    }

    public void setConfigurationJSON(String configurationJSON) {
        this.configurationJSON = configurationJSON;
    }

    public UserDeviceConfiguration() {
    }

    public UserDeviceConfiguration(UUID id, User user, Device device, String configurationJSON) {
        this.id = id;
        this.user = user;
        this.device = device;
        this.configurationJSON = configurationJSON;
    }
}
