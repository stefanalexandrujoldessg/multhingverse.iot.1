package com.liciot.usermanagementms.entity;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Entity
@Table(name = "liciot_human_user")
@Inheritance(strategy = InheritanceType.JOINED)
public class HumanUser extends User{
    String name;

    public HumanUser(String username, String password, String name) {
        super(username, password);
        this.name = name;
    }

    public HumanUser() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
