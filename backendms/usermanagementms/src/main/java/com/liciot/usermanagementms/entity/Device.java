package com.liciot.usermanagementms.entity;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.context.annotation.Configuration;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "liciot_device")
//Poti ori sa reusesti sa ui id sris de tine ori asa a configureeszi prein spoecificarea numelor de coloanae foreign key insere un  nou camp aici unui device serfival number or deviceid id
/*Id(generate=TABLE, generator="ADDRESS_TABLE_GENERATOR")
@TableGenerator(
        name="ADDRESS_TABLE_GENERATOR",
        tableName="EMPLOYEE_GENERATOR_TABLE",
        pkColumnValue="ADDRESS_SEQ"
)
@Column(name="ADDRESS_ID")
*/
public class Device {
    @Id
    /*
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Type(type= "uuid-binary")
    */

            UUID id;
            @Column(columnDefinition = "text")
    String configurationJSON;

    @ManyToOne( fetch = FetchType.EAGER)
            @JoinColumn(updatable = false)//sa zi un workaround pt problema de la inserare
    User adminUser;
/***De aici a plecat problema cu adaugatul ccesUserilor la dviceuri deic dacq anu pui mapped by wse crereaza doua tabele,
    Oribil ar poate necesar in unele situatii,
    De astemenea tabelul care contine mapped by daca se adauga in lista tinuta de el (De la manytomany) entitati acestea nu vor i persistate in db
        //entitatil# noi ntrebuie adugte in lista tabelului caer a necesitai primcipal creatrew tabeleupui de lagtura many to many ca tu practi pein referenced by ii spi la orm ca sa nu mai creeze unca -n tabel ca exista deja iompus de entitatea cuta re carereferentgias lista de mine this prin variabila instanta ...mapBy="instance var name"3

 ***/
@ManyToMany(  fetch = FetchType.LAZY,mappedBy = "accessDevices")
     Set<User> accessUsers;

    public Device(UUID id, String configurationJSON, User adminUser, Set<User> accessUsers) {
        this.id = id;
        this.configurationJSON = configurationJSON;
        this.adminUser = adminUser;
        this.accessUsers = accessUsers;
    }

    public Device(UUID id, String configurationJSON, User adminUser) {
        this.id = id;
        this.configurationJSON = configurationJSON;
        this.adminUser = adminUser;
    }

    public Device() {

    }

    public Set<User> getAccessUsers() {
        return accessUsers;
    }

    public void setAccessUsers(Set<User> accessUsers) {
        this.accessUsers = accessUsers;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getConfigurationJSON() {
        return configurationJSON;
    }

    public void setConfigurationJSON(String configurationJSON) {
        this.configurationJSON = configurationJSON;
    }

    public User getAdminUser() {
        return adminUser;
    }

    public void setAdminUser(User adminUser) {
        this.adminUser = adminUser;
    }

}