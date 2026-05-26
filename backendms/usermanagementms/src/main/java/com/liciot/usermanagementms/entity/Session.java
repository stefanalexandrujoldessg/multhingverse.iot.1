package com.liciot.usermanagementms.entity;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
@Entity
@Table(name = "liciot_session")
public class Session {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Type (type= "uuid-binary")
    UUID token;

    @ManyToOne (fetch = FetchType.EAGER)
    User user;

    @Column(name = "expiration_date_time")
    LocalDateTime expirationDateTime;

    public Session(UUID token, User user, LocalDateTime expirationDateTime) {
        this.token = token;
        this.user = user;
        this.expirationDateTime = expirationDateTime;
    }

    public Session(UUID token, User user) {
        this.token = token;
        this.user = user;
    }

    public Session(UUID token) {
        this.token = token;
    }

    public Session() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getExpirationDateTime() {
        return expirationDateTime;
    }

    public void setExpirationDateTime(LocalDateTime expirationDateTime) {
        this.expirationDateTime = expirationDateTime;
    }

    public UUID getToken() {
        return token;
    }

    public void setToken(UUID token) {
        this.token = token;
    }
}
