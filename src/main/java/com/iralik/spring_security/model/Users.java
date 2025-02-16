package com.iralik.spring_security.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "appuser")
public class Users {

    @Id
    @Column(name = "user_id")
    private int id;

    @Column(name = "password")
    @NotNull
    private String password;

    @Column(name = "user_name")
    @NotNull
    private String userName;


    public void setId(int id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    // Getter for password
    public String getPassword() {
        return password;
    }

    // Getter for username
    public String getUserName() {
        return userName;
    }
    // Getter for id
    public int getId() {
        return id;
    }
}

