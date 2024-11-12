package com.lxt.framework.data.model.common;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.lxt.framework.common.global.Constant;

@Entity(tableName = Constant.USER_TABLE)
public class User {
    @PrimaryKey private int id;
    @ColumnInfo(name = "name") private String name;
    @ColumnInfo(name = "email") private String email;
    @ColumnInfo(name = "hotkeyCount") private int hotkeyCount;

    //     Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public int getHotkeyCount() {return hotkeyCount;}
    public void setHotkeyCount(int hotkeyCount) {this.hotkeyCount = hotkeyCount;}

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", hotkeyCount=" + hotkeyCount +
                '}';
    }
}
