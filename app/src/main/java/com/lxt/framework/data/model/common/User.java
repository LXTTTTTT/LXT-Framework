package com.lxt.framework.data.model.common;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

//@Entity(tableName = "users")
public class User {
//    @PrimaryKey
    private int id;
    private String name;
    private String email;

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}