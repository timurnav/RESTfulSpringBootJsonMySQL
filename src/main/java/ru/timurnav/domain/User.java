package ru.timurnav.domain;

public class User {

    private Long id;

    private String internalPicURL;

    private String name;

    private String email;

    public User(Long id, String internalPicURL, String name, String email) {
        this.id = id;
        this.internalPicURL = internalPicURL;
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

    public String getInternalPicURL() {
        return internalPicURL;
    }

    public void setInternalPicURL(String internalPicURL) {
        this.internalPicURL = internalPicURL;
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
