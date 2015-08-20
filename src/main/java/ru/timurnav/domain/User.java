package ru.timurnav.domain;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;

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

    public User(Long id, String internalAvatarURL, String name, String email) {
        this.id = id;
        this.internalAvatarURL = internalAvatarURL;
        this.name = name;
        this.email = email;
    }

    public User() {
    }

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
}
