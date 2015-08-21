package ru.timurnav.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "avatar")
    private String internalAvatarURL;

    @Length(min = 5)
    private String name;

    @Column(unique = true)
    @Email
    private String email;

    @Column(name = "online")
    private boolean online;

    @Column(name = "status_timestamp")
    private Timestamp statusTimestamp;

    public User(@JsonProperty("url") String internalAvatarURL,
                @JsonProperty("name")String name,
                @JsonProperty("email")String email) {
        this.internalAvatarURL = internalAvatarURL;
        this.name = name;
        this.email = email;
        this.online = false;
        this.statusTimestamp = new Timestamp((new Date()).getTime());
    }

    public User(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInternalAvatarURL() {
        return internalAvatarURL;
    }

    public void setInternalAvatarURL(String internalAvatarURL) {
        this.internalAvatarURL = internalAvatarURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public Timestamp getStatusTimestamp() {
        return statusTimestamp;
    }

    public void setStatusTimestamp(Timestamp statusTimestamp) {
        this.statusTimestamp = statusTimestamp;
    }
}
