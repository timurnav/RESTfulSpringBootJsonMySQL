package ru.timurnav.domain;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue
    private long id;

    @Column(name = "avatar")
    private String internalAvatarURL;

    @Length(min = 2)
    private String name;

    @Column(unique = true)
    @Email
    private String email;

    @Column(name = "online")
    private boolean online;

    @Column(name = "status_timestamp")
    private Timestamp statusTimestamp;

    public long getId() {
        return id;
    }

    public User setId(Long id) {
        this.id = id;
        return this;
    }

    public String getInternalAvatarURL() {
        return internalAvatarURL;
    }

    public User setInternalAvatarURL(String internalAvatarURL) {
        this.internalAvatarURL = internalAvatarURL;
        return this;
    }

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public boolean isOnline() {
        return online;
    }

    public User setOnline(boolean online) {
        this.online = online;
        return this;
    }

    public Timestamp getStatusTimestamp() {
        return statusTimestamp;
    }

    public User setStatusTimestamp(Timestamp statusTimestamp) {
        this.statusTimestamp = statusTimestamp;
        return this;
    }
}
