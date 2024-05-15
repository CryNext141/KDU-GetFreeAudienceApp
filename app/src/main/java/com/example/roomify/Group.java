package com.example.roomify;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Group {
    @SerializedName("name")
    private String name;
    @SerializedName("ID")
    private String ID;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
