package com.liciot.usermanagementms.entity;

 
import org.hibernate.annotations.Columns;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Set;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "liciot_user")
@Inheritance(strategy = InheritanceType.JOINED)
public class User {

    /*@GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Type(type = "uuid-binary")
    */
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Type(type= "uuid-binary")
    UUID id;

    @Column(unique = true)
    String username;
    String password;
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "liciot_authority" )
     Set<Authority> authoritySet;

    //i neerd thos to fetch the devices but i do not need the list of devies thawt i am the admin for because i have a simple repository method findByAdminUser();
    //Or i csn keep a list because it wont need any etra workloadwill be bebeautifully handled bby the jpa ehn needded ith the same query as it ues for findDeviceByAdminUser
    @ManyToMany(  fetch = FetchType.LAZY)
    Set<Device> accessDevices;
    @OneToMany(  fetch = FetchType.LAZY,mappedBy = "adminUser")
    Set<Device> adminDevices;

    public User(UUID id, String username, String password, Set<Authority> authoritySet, Set<Device> accessDevices, Set<Device> adminDevices) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.authoritySet = authoritySet;
        this.accessDevices = accessDevices;
        this.adminDevices = adminDevices;
    }

    public User(UUID id, String username, String password, Set<Authority> authoritySet) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.authoritySet = authoritySet;
    }

    public User(String username, String password, Set<Authority> authoritySet) {
        this.username = username;
        this.password = password;
        this.authoritySet = authoritySet;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User() {
    }

    public Set<Device> getAccessDevices() {
        return accessDevices;
    }

    public void setAccessDevices(Set<Device> accessDevices) {
        this.accessDevices = accessDevices;
    }

    public Set<Device> getAdminDevices() {
        return adminDevices;
    }

    public void setAdminDevices(Set<Device> adminDevices) {
        this.adminDevices = adminDevices;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Set<Authority> getAuthoritySet() {
        return authoritySet;
    }

    public void setAuthoritySet(Set<Authority> authoritySet) {
        this.authoritySet = authoritySet;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
